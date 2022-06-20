package app.homsai.engine.entities.application.http.ui;

import app.homsai.engine.common.application.http.ui.components.CustomConfirmDialog;
import app.homsai.engine.common.application.http.ui.components.CustomErrorDialog;
import app.homsai.engine.common.application.http.ui.components.MainLayout;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.common.domain.utils.EnText;
import app.homsai.engine.entities.application.http.cache.HomsaiHVACDeviceCacheRepository;
import app.homsai.engine.entities.application.http.dtos.HvacDeviceCacheDto;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collections;
import java.util.List;

@PageTitle("HVAC Devices Initialization")
@Route(value = "hvacdevicesinit", layout= MainLayout.class)
public class HvacDevicesInitView extends VerticalLayout {

    private HomsaiHVACDeviceCacheRepository homsaiHVACDeviceCacheRepository;
    private EntitiesCommandsApplicationService entitiesCommandsApplicationService;

    final Grid<HvacDeviceCacheDto> grid;
    final Span logLabel;

    public HvacDevicesInitView(HomsaiHVACDeviceCacheRepository homsaiHVACDeviceCacheRepository, EntitiesCommandsApplicationService entitiesCommandsApplicationService) {
        this.homsaiHVACDeviceCacheRepository = homsaiHVACDeviceCacheRepository;
        this.entitiesCommandsApplicationService = entitiesCommandsApplicationService;
        this.grid = new Grid<>(HvacDeviceCacheDto.class);
        this.logLabel = new Span();
        UI.getCurrent().setPollInterval(5000);
        UI.getCurrent().addPollListener(pollEvent -> listInitStatus());
        addClassName("hvac-devices-init-view");
        H2 pageTitle = new H2("HVAC Devices Initialization");
        configureGrid();
        listInitStatus();
        add(pageTitle, createButtonLayout(), grid, logLabel);
    }

    private void configureGrid() {
        grid.addClassNames("hvac-devices-init-grid");
        grid.setHeight("100px");
        grid.setColumns("inProgress", "elapsedTimeSeconds", "remainingTimeSeconds", "totalTimeSeconds");
        grid.addColumn(device -> String.format("%.2f", device.getElapsedPercent())+"%").setHeader("Elapsed %");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void listInitStatus() {
        List<HvacDeviceCacheDto> hvacDeviceCacheDtos = Collections.singletonList(homsaiHVACDeviceCacheRepository.getHvacDeviceCacheDto());
        grid.setItems(hvacDeviceCacheDtos);
        logLabel.getElement().setProperty("innerHTML", (homsaiHVACDeviceCacheRepository.getHvacDeviceCacheDto().getLog()));
    }

    private HorizontalLayout createButtonLayout(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(createBackButton(), createStartInitButton());
        return horizontalLayout;
    }

    private Button createBackButton(){
        Button button = new Button("Back");
        button.addClickListener( e -> UI.getCurrent().navigate(HvacDevicesView.class));
        return button;
    }

    private Button createStartInitButton(){
        Button startInitButton = new Button("Start initialization");
        if(homsaiHVACDeviceCacheRepository.getHvacDeviceCacheDto().getInProgress())
            startInitButton.setText("Initialization in progress...");
        addButtonListener(startInitButton);
        return startInitButton;
    }


    private void addButtonListener(Button startInitButton){
        if(homsaiHVACDeviceCacheRepository.getHvacDeviceCacheDto().getInProgress())
            return;
        startInitButton.addClickListener( e -> {
            Integer timeRequiredMinutes = entitiesCommandsApplicationService.getHvacDeviceInitTimeSeconds()/60;
            CustomConfirmDialog d1 = new CustomConfirmDialog(EnText.START_HVAC_DEVICE_INIT_TITLE, EnText.START_HVAC_DEVICE_INIT_DESCRIPTION, Collections.singletonList(timeRequiredMinutes.toString()));
            d1.setOnConfirmListener(() -> {
                try {
                    entitiesCommandsApplicationService.initHVACDevices(Consts.HVAC_DEVICE_CONDITIONING);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (HvacPowerMeterIdNotSet ex) {
                    d1.close();
                    new CustomErrorDialog(EnText.ERROR_TITLE, EnText.ERROR_HVAC_DEVICE_INIT_DESCRIPTION, null).open();
                }
            });
            d1.open();
        });
        //TODO show error message
    }

}
