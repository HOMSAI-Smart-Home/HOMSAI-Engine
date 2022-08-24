package app.homsai.engine.pvoptimizer.application.http.ui;

import app.homsai.engine.common.application.http.ui.components.CustomErrorDialog;
import app.homsai.engine.common.application.http.ui.components.MainLayout;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.common.domain.utils.EnText;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.pvoptimizer.application.http.ui.components.OptimizerSettingsEditor;
import app.homsai.engine.pvoptimizer.domain.models.OptimizerHVACDevice;
import app.homsai.engine.pvoptimizer.domain.services.cache.PVOptimizerCacheService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Photovoltaic Optimizer")
@Route(value = "hvacdevicesopt", layout= MainLayout.class)
public class HvacPVOptimizationView extends VerticalLayout {

    private PVOptimizerCacheService pvoptimizerCacheService;
    private EntitiesQueriesApplicationService entitiesQueriesApplicationService;
    private EntitiesCommandsService entitiesCommandsService;
    private final OptimizerSettingsEditor optimizerSettingsEditor;

    final Grid<OptimizerHVACDevice> grid;
    final Checkbox enabled;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());


    public HvacPVOptimizationView(PVOptimizerCacheService pvoptimizerCacheService, EntitiesQueriesApplicationService entitiesQueriesApplicationService, EntitiesCommandsService entitiesCommandsService, OptimizerSettingsEditor optimizerSettingsEditor) {
        this.pvoptimizerCacheService = pvoptimizerCacheService;
        this.optimizerSettingsEditor = optimizerSettingsEditor;
        this.entitiesQueriesApplicationService = entitiesQueriesApplicationService;
        this.entitiesCommandsService = entitiesCommandsService;
        this.grid = new Grid<>(OptimizerHVACDevice.class);
        UI.getCurrent().setPollInterval(5000);
        UI.getCurrent().addPollListener(pollEvent -> onRefresh());

        addClassName("hvac-devices-view");
        setSizeFull();
        configureGrid();
        H2 pageTitle = new H2("Photovoltaic Optimizer");
        enabled = new Checkbox("Enabled");
        enabled.addValueChangeListener(v -> onChangeEnabled(v.getValue()));
        grid.asSingleSelect().addValueChangeListener(e -> {
            this.optimizerSettingsEditor.editHvacDeviceOptSetting(e.getValue());
        });
        add(pageTitle, createButtonLayout(), enabled, grid, optimizerSettingsEditor);
        listDevices();
    }

    private void configureGrid() {
        grid.addClassNames("hvac-devices-opt-grid");
        grid.setColumns("entityId");
        grid.addColumn(OptimizerHVACDevice::getAreaId).setHeader("Area").setSortable(true);
        grid.addColumn(OptimizerHVACDevice::isEnabled).setHeader("Enabled").setSortable(true);
        grid.addColumn(device -> device.isManual()?"Manual":"Auto").setHeader("Current Mode").setSortable(true);
        grid.addColumn(device -> String.format("%.2f", device.getActualPowerConsumption())+Consts.HOME_ASSISTANT_WATT).setHeader("Current Consumption").setSortable(true);
        grid.addColumn(device -> !device.getActive()?Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION:Consts.HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION).setHeader("Current Function").setSortable(true);
        grid.addColumn(device -> String.format("%.2f", device.getCurrentTemperature())).setHeader("Current Temperature").setSortable(true);
        grid.addColumn(device -> String.format("%.2f", device.getDeltaTemperature())).setHeader("Delta Temperature").setSortable(true);
        grid.addColumn(device -> device.getStartTime()==null?null:formatter.format(device.getStartTime())).setHeader("Switch-on Time").setSortable(true);
        grid.addColumn(device -> device.getEndTime().equals(Instant.EPOCH)?null:formatter.format(device.getEndTime())).setHeader("Switch-off Time").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void listDevices() {
        List<OptimizerHVACDevice> optimizerHVACDeviceDtos = new ArrayList<>(pvoptimizerCacheService.getHvacDevicesCache().values());
        grid.setItems(optimizerHVACDeviceDtos);

        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        enabled.setValue(homeInfo.getPvOptimizationsEnabled());
    }

    private void onRefresh(){
        if(optimizerSettingsEditor.isVisible())
            return;
        else
            listDevices();
    }

    private HorizontalLayout createButtonLayout(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(createBackButton());
        return horizontalLayout;
    }

    private Button createBackButton(){
        Button button = new Button("Back");
        button.addClickListener( e -> UI.getCurrent().navigate(HvacDevicesView.class));
        return button;
    }

    private void onChangeEnabled(boolean enabledValue){
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        if(enabledValue){
            if(homeInfo.getGeneralPowerMeterId() == null || homeInfo.getHvacPowerMeterId() == null || homeInfo.getPvProductionSensorId() == null){
                new CustomErrorDialog(EnText.ERROR_TITLE, EnText.ERROR_PV_OPT_START_DESCRIPTION, null).open();
                enabled.setValue(false);
                return;
            }
        }
        homeInfo.setPvOptimizationsEnabled(enabledValue);
        try {
            entitiesCommandsService.updateHomeInfo(homeInfo);
        } catch (BadHomeInfoException e) {
            e.printStackTrace();
        }
    }

}
