package app.homsai.engine.pvoptimizer.application.http.ui;

import app.homsai.engine.common.application.http.ui.components.CustomConfirmDialog;
import app.homsai.engine.common.application.http.ui.components.CustomErrorDialog;
import app.homsai.engine.common.application.http.ui.components.MainLayout;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.common.domain.utils.EnText;
import app.homsai.engine.pvoptimizer.application.services.PVOptimizerCommandsApplicationService;
import app.homsai.engine.pvoptimizer.domain.exceptions.ClimateEntityNotFoundException;
import app.homsai.engine.pvoptimizer.domain.services.cache.HomsaiOptimizerHVACDeviceInitializationCacheService;
import app.homsai.engine.pvoptimizer.application.http.dtos.HvacOptimizerDeviceInitializationCacheDto;
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

    private HomsaiOptimizerHVACDeviceInitializationCacheService homsaiOptimizerHVACDeviceInitializationCacheService;
    private PVOptimizerCommandsApplicationService pvOptimizerCommandsApplicationService;

    final Grid<HvacOptimizerDeviceInitializationCacheDto> grid;
    final Span logLabel;

    public HvacDevicesInitView(HomsaiOptimizerHVACDeviceInitializationCacheService homsaiOptimizerHVACDeviceInitializationCacheService, PVOptimizerCommandsApplicationService pvOptimizerCommandsApplicationService) {
        this.homsaiOptimizerHVACDeviceInitializationCacheService = homsaiOptimizerHVACDeviceInitializationCacheService;
        this.pvOptimizerCommandsApplicationService = pvOptimizerCommandsApplicationService;
        this.grid = new Grid<>(HvacOptimizerDeviceInitializationCacheDto.class);
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
        List<HvacOptimizerDeviceInitializationCacheDto> hvacOptimizerDeviceInitializationCacheDtos = Collections.singletonList(homsaiOptimizerHVACDeviceInitializationCacheService.getHvacDeviceCacheDto());
        grid.setItems(hvacOptimizerDeviceInitializationCacheDtos);
        logLabel.getElement().setProperty("innerHTML", (homsaiOptimizerHVACDeviceInitializationCacheService.getHvacDeviceCacheDto().getLog()));
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
        if(homsaiOptimizerHVACDeviceInitializationCacheService.getHvacDeviceCacheDto().getInProgress()) {
            startInitButton.setText("Initialization in progress...");
            startInitButton.setEnabled(false);
        }
        addButtonListener(startInitButton);
        return startInitButton;
    }


    private void addButtonListener(Button startInitButton){
        if(homsaiOptimizerHVACDeviceInitializationCacheService.getHvacDeviceCacheDto().getInProgress())
            return;
        startInitButton.addClickListener( e -> {
            int timeRequiredMinutes = pvOptimizerCommandsApplicationService.getHvacDeviceInitTimeSeconds(Consts.PV_OPTIMIZATION_MODE_SUMMER)/60;
            CustomConfirmDialog d1 = new CustomConfirmDialog(EnText.START_HVAC_DEVICE_INIT_TITLE, EnText.START_HVAC_DEVICE_INIT_DESCRIPTION, Collections.singletonList(Integer.toString(timeRequiredMinutes)));
            d1.setOnConfirmListener(() -> {
                try {
                    pvOptimizerCommandsApplicationService.initHVACDevices(Consts.PV_OPTIMIZATION_MODE_WINTER);
                    startInitButton.setEnabled(false);
                    startInitButton.setText("Initialization in progress...");
                } catch (InterruptedException | ClimateEntityNotFoundException ex) {
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
