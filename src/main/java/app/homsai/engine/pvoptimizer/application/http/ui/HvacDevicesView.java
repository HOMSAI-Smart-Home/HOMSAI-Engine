package app.homsai.engine.pvoptimizer.application.http.ui;

import app.homsai.engine.common.application.http.ui.components.MainLayout;
import app.homsai.engine.common.domain.utils.constants.Consts;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.pvoptimizer.domain.models.OptimizerHVACDevice;
import app.homsai.engine.pvoptimizer.domain.services.cache.PVOptimizerCacheService;
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

    private PVOptimizerCacheService pvoptimizerCacheService;

    final Grid<OptimizerHVACDevice> grid;

    public HvacDevicesView(PVOptimizerCacheService pvoptimizerCacheService, EntitiesQueriesApplicationService entitiesQueriesApplicationService) {
        this.pvoptimizerCacheService = pvoptimizerCacheService;
        this.grid = new Grid<>(OptimizerHVACDevice.class);
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
        grid.addColumn(OptimizerHVACDevice::getAreaId).setHeader("Area");
        grid.addColumn(device -> String.format("%.2f", device.getPowerConsumption())+Consts.HOME_ASSISTANT_WATT).setHeader("Init Consumption").setSortable(true);;
        grid.addColumn(device -> !device.getActive()?Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION:Consts.HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION).setHeader("Status").setSortable(true);;
        grid.addColumn(device -> String.format("%.2f", device.getSetTemperature())).setHeader("Desired Temperature").setSortable(true);;
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void listDevices() {
        List<OptimizerHVACDevice> optimizerHVACDeviceDtos = new ArrayList<>(pvoptimizerCacheService.getHvacDevicesCache().values());
        grid.setItems(optimizerHVACDeviceDtos);
    }

    private HorizontalLayout createButtonLayout(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        if(pvoptimizerCacheService.getHvacDevicesCache().size() == 0)
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
