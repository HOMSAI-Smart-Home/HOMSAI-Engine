package app.homsai.engine.entities.application.http.ui;

import app.homsai.engine.common.application.http.ui.MainLayout;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.http.cache.HomsaiEntityShowCacheRepository;
import app.homsai.engine.entities.application.http.cache.HomsaiHVACDeviceCacheRepository;
import app.homsai.engine.entities.application.http.dtos.HVACDeviceDto;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "hvacdevices", layout= MainLayout.class)
public class HvacDevicesView extends VerticalLayout {

    private HomsaiHVACDeviceCacheRepository homsaiHVACDeviceCacheRepository;
    private EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    final Grid<HVACDeviceDto> grid;

    public HvacDevicesView(HomsaiHVACDeviceCacheRepository homsaiHVACDeviceCacheRepository, EntitiesQueriesApplicationService entitiesQueriesApplicationService) {
        this.homsaiHVACDeviceCacheRepository = homsaiHVACDeviceCacheRepository;
        this.entitiesQueriesApplicationService = entitiesQueriesApplicationService;
        this.grid = new Grid<>(HVACDeviceDto.class);
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
        grid.addColumn(device -> device.getArea().getName()).setHeader("Area");
        grid.addColumn(device -> String.format("%.2f", device.getPowerConsumption())+Consts.HOME_ASSISTANT_WATT).setHeader("AVG Consumption");
        grid.addColumn(device -> device.getType().equals(Consts.HVAC_DEVICE_CONDITIONING)?Consts.HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION:Consts.HOME_ASSISTANT_HVAC_DEVICE_HEATING_FUNCTION).setHeader("Type");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void listDevices() {
        List<HVACDeviceDto> hvacDeviceDtos = entitiesQueriesApplicationService.getAllHomsaiHvacDevices(Consts.HVAC_DEVICE_CONDITIONING);
        grid.setItems(hvacDeviceDtos);
    }

    private Button createInitDevicesButton(){
        Button button = new Button("Initialize Devices");
        button.addClickListener( e -> UI.getCurrent().navigate(HvacDevicesInitView.class));
        return button;
    }
}
