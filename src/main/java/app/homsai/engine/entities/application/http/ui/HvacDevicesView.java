package app.homsai.engine.entities.application.http.ui;

import app.homsai.engine.common.application.http.ui.components.MainLayout;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.optimizations.application.http.ui.HvacPVOptimizationView;
import app.homsai.engine.optimizations.domain.models.HvacDevice;
import app.homsai.engine.optimizations.infrastructure.cache.HVACRunningDevicesCacheRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@PageTitle("HVAC Devices")
@Route(value = "hvacdevices", layout= MainLayout.class)
public class HvacDevicesView extends VerticalLayout {

    private HVACRunningDevicesCacheRepository hvacRunningDevicesCacheRepository;

    final Grid<HvacDevice> grid;

    public HvacDevicesView(HVACRunningDevicesCacheRepository hvacRunningDevicesCacheRepository, EntitiesQueriesApplicationService entitiesQueriesApplicationService) {
        this.hvacRunningDevicesCacheRepository = hvacRunningDevicesCacheRepository;
        this.grid = new Grid<>(HvacDevice.class);
        UI.getCurrent().setPollInterval(5000);
        UI.getCurrent().addPollListener(pollEvent -> listDevices());
        addClassName("hvac-devices-view");
        setSizeFull();
        H2 pageTitle = new H2("HVAC Devices");
        configureGrid();
        add(pageTitle, createButtonLayout(), grid);
        listDevices();
    }

    private void configureGrid() {
        grid.addClassNames("hvac-devices-grid");
        grid.setSizeFull();
        grid.setColumns("entityId");
        grid.addColumn(HvacDevice::getAreaId).setHeader("Area");
        grid.addColumn(device -> String.format("%.2f", device.getPowerConsumption())+Consts.HOME_ASSISTANT_WATT).setHeader("Init Consumption").setSortable(true);;
        grid.addColumn(device -> !device.getActive()?Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION:Consts.HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION).setHeader("Status").setSortable(true);;
        grid.addColumn(device -> String.format("%.2f", device.getSetTemperature())).setHeader("Desired Temperature").setSortable(true);;
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void listDevices() {
        List<HvacDevice> hvacDeviceDtos = new ArrayList<>(hvacRunningDevicesCacheRepository.getHvacDevicesCache().values());
        grid.setItems(hvacDeviceDtos);
    }

    private HorizontalLayout createButtonLayout(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        if(hvacRunningDevicesCacheRepository.getHvacDevicesCache().size() == 0)
            horizontalLayout.add(createInitDevicesButton());
        else
            horizontalLayout.add(createOptButton(), createInitDevicesButton());
        return horizontalLayout;
    }

    private Button createInitDevicesButton(){
        Button button = new Button("Initialize Devices");
        button.addClickListener( e -> UI.getCurrent().navigate(HvacDevicesInitView.class));
        return button;
    }


    private Button createOptButton(){
        Button button = new Button("PV Optimizer");
        button.addClickListener( e -> UI.getCurrent().navigate(HvacPVOptimizationView.class));
        return button;
    }
}
