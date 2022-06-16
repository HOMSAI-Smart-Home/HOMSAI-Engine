package app.homsai.engine.entities.application.http.ui;

import app.homsai.engine.common.application.http.ui.MainLayout;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.http.cache.HomsaiEntityShowCacheRepository;
import app.homsai.engine.entities.application.http.cache.HomsaiHVACDeviceCacheRepository;
import app.homsai.engine.entities.application.http.dtos.HVACDeviceDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.optimizations.domain.models.HvacDevice;
import app.homsai.engine.optimizations.infrastructure.cache.HVACRunningDevicesCacheRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

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
        configureGrid();
        add(createInitDevicesButton(), grid);
        listDevices();
    }

    private void configureGrid() {
        grid.addClassNames("hvac-devices-grid");
        grid.setSizeFull();
        grid.setColumns("entityId");
        grid.addColumn(HvacDevice::getAreaId).setHeader("Area");
        grid.addColumn(device -> String.format("%.2f", device.getPowerConsumption())+Consts.HOME_ASSISTANT_WATT).setHeader("Init Consumption");
        grid.addColumn(device -> String.format("%.2f", device.getActualPowerConsumption())+Consts.HOME_ASSISTANT_WATT).setHeader("Current Consumption");
        grid.addColumn(device -> !device.getActive()?Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION:Consts.HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION).setHeader("Running");
        grid.addColumn(HvacDevice::getCurrentTemperature).setHeader("Temperature");
        grid.addColumn(HvacDevice::getSetTemperature).setHeader("Desired Temperature");
        grid.addColumn(HvacDevice::getDeltaTemperature).setHeader("Delta Temperature");
        grid.addColumn(HvacDevice::getStartTime).setHeader("Start");
        grid.addColumn(HvacDevice::getEndTime).setHeader("End");
        grid.addColumn(HvacDevice::isEnabled).setHeader("Enabled");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void listDevices() {
        List<HvacDevice> hvacDeviceDtos = new ArrayList<>(hvacRunningDevicesCacheRepository.getHvacDevicesCache().values());
        grid.setItems(hvacDeviceDtos);
    }

    private Button createInitDevicesButton(){
        Button button = new Button("Initialize Devices");
        button.addClickListener( e -> UI.getCurrent().navigate(HvacDevicesInitView.class));
        return button;
    }
}
