package app.homsai.engine.entities.application.http.ui;

import app.homsai.engine.common.application.http.ui.MainLayout;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.http.cache.HomsaiHVACDeviceCacheRepository;
import app.homsai.engine.entities.application.http.dtos.HVACDeviceDto;
import app.homsai.engine.entities.application.http.dtos.HvacDeviceCacheDto;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Route(value = "hvacdevicesinit", layout= MainLayout.class)
public class HvacDevicesInitView extends VerticalLayout {

    private HomsaiHVACDeviceCacheRepository homsaiHVACDeviceCacheRepository;
    private EntitiesCommandsApplicationService entitiesCommandsApplicationService;

    final Grid<HvacDeviceCacheDto> grid;
    final Span logLabel;
    final Button startInitButton;

    public HvacDevicesInitView(HomsaiHVACDeviceCacheRepository homsaiHVACDeviceCacheRepository, EntitiesCommandsApplicationService entitiesCommandsApplicationService) {
        this.homsaiHVACDeviceCacheRepository = homsaiHVACDeviceCacheRepository;
        this.entitiesCommandsApplicationService = entitiesCommandsApplicationService;
        this.grid = new Grid<>(HvacDeviceCacheDto.class);
        this.logLabel = new Span();
        this.startInitButton = new Button("Start initialization");
        if(homsaiHVACDeviceCacheRepository.getHvacDeviceCacheDto().getInProgress())
            this.startInitButton.setText("Initialization in progress...");
        UI.getCurrent().setPollInterval(5000);
        UI.getCurrent().addPollListener(pollEvent -> listInitStatus());
        addClassName("hvac-devices-init-view");
        configureGrid();
        listInitStatus();
        addButtonListener();
        add(startInitButton, grid, logLabel);
    }

    private void configureGrid() {
        grid.addClassNames("hvac-devices-init-grid");
        grid.setHeight("100px");
        grid.setColumns("inProgress", "elapsedTimeSeconds", "remainingTimeSeconds", "totalTimeSeconds");
        grid.addColumn(device -> String. format("%.2f", device.getElapsedPercent())+"%").setHeader("Elapsed %");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void listInitStatus() {
        List<HvacDeviceCacheDto> hvacDeviceCacheDtos = Collections.singletonList(homsaiHVACDeviceCacheRepository.getHvacDeviceCacheDto());
        grid.setItems(hvacDeviceCacheDtos);
        logLabel.getElement().setProperty("innerHTML", (homsaiHVACDeviceCacheRepository.getHvacDeviceCacheDto().getLog()));
    }

    private void addButtonListener(){
        if(!homsaiHVACDeviceCacheRepository.getHvacDeviceCacheDto().getInProgress())
            startInitButton.addClickListener( e -> {
                System.out.println("passa");
                try {
                    entitiesCommandsApplicationService.initHVACDevices(Consts.HVAC_DEVICE_CONDITIONING);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (HvacPowerMeterIdNotSet ex) {
                    ex.printStackTrace();
                }
            });
        //TODO show error message
    }

}
