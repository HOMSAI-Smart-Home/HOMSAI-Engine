package app.homsai.engine.entities.application.http.ui;

import app.homsai.engine.common.application.http.ui.components.MainLayout;
import app.homsai.engine.common.domain.utils.EnText;
import app.homsai.engine.entities.application.http.dtos.HAEntityDto;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.application.services.EntitiesScheduledApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Pageable;

import java.util.List;

@PageTitle("Settings")
@Route(value = "settings", layout= MainLayout.class)
public class SettingsView extends VerticalLayout {

    private EntitiesQueriesApplicationService entitiesQueriesApplicationService;
    private EntitiesCommandsApplicationService entitiesCommandsApplicationService;
    private EntitiesScheduledApplicationService entitiesScheduledApplicationService;

    private Select<HAEntityDto> storageMeterSensorSelect;
    private Select<HAEntityDto> generalMeterSensorSelect;
    private Select<HAEntityDto> hvacMeterSensorSelect;
    private Select<HAEntityDto> pvMeterSensorSelect;
    private final Label error;

    public SettingsView(EntitiesQueriesApplicationService entitiesQueriesApplicationService, EntitiesCommandsApplicationService entitiesCommandsApplicationService, EntitiesScheduledApplicationService entitiesScheduledApplicationService) {
        this.entitiesQueriesApplicationService = entitiesQueriesApplicationService;
        this.entitiesCommandsApplicationService = entitiesCommandsApplicationService;
        this.entitiesScheduledApplicationService = entitiesScheduledApplicationService;
        addClassName("settings-view");
        setSizeFull();
        H2 pageTitle = new H2("Settings");
        error = new Label();
        add(pageTitle, createSettingsLayout(), error, createButtonLayout());
        populateSelects();
    }

    private VerticalLayout createSettingsLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        ComponentRenderer<Component, HAEntityDto>  renderer = new ComponentRenderer<>(haEntityDto -> new Label(haEntityDto.getName()));
        generalMeterSensorSelect = new Select<>();
        hvacMeterSensorSelect = new Select<>();
        pvMeterSensorSelect = new Select<>();
        storageMeterSensorSelect = new Select<>();
        generalMeterSensorSelect.setLabel(EnText.SELECT_GENERAL_PM_LABEL);
        hvacMeterSensorSelect.setLabel(EnText.SELECT_HVAC_PM_LABEL);
        pvMeterSensorSelect.setLabel(EnText.SELECT_PV_PRODUCTION_PM_LABEL);
        storageMeterSensorSelect.setLabel(EnText.SELECT_STORAGE_PM_LABEL);
        generalMeterSensorSelect.setRenderer(renderer);
        hvacMeterSensorSelect.setRenderer(renderer);
        pvMeterSensorSelect.setRenderer(renderer);
        storageMeterSensorSelect.setRenderer(renderer);
        generalMeterSensorSelect.setEmptySelectionAllowed(true);
        hvacMeterSensorSelect.setEmptySelectionAllowed(true);
        pvMeterSensorSelect.setEmptySelectionAllowed(true);
        storageMeterSensorSelect.setEmptySelectionAllowed(true);
        generalMeterSensorSelect.setEmptySelectionCaption(EnText.NO_SENSOR);
        hvacMeterSensorSelect.setEmptySelectionCaption(EnText.NO_SENSOR);
        pvMeterSensorSelect.setEmptySelectionCaption(EnText.NO_SENSOR);
        storageMeterSensorSelect.setEmptySelectionCaption(EnText.NO_SENSOR);
        verticalLayout.add(generalMeterSensorSelect, hvacMeterSensorSelect, pvMeterSensorSelect, storageMeterSensorSelect);
        return verticalLayout;
    }

    private void populateSelects() {
        List<HAEntityDto> powerMeterEntities = entitiesQueriesApplicationService.getAllHomeAssistantEntities(Pageable.unpaged(), "device_class:power").getContent();
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        generalMeterSensorSelect.setItems(powerMeterEntities);
        hvacMeterSensorSelect.setItems(powerMeterEntities);
        pvMeterSensorSelect.setItems(powerMeterEntities);
        storageMeterSensorSelect.setItems(powerMeterEntities);
        if(homeInfo.getGeneralPowerMeterId() == null)
            generalMeterSensorSelect.setValue(null);
        else
            generalMeterSensorSelect.setValue(findHAEntityDtoByEntityId(powerMeterEntities, homeInfo.getGeneralPowerMeterId()));

        if(homeInfo.getHvacPowerMeterId() == null)
            hvacMeterSensorSelect.setValue(null);
        else
            hvacMeterSensorSelect.setValue(findHAEntityDtoByEntityId(powerMeterEntities, homeInfo.getHvacPowerMeterId()));

        if(homeInfo.getPvProductionSensorId() == null)
            pvMeterSensorSelect.setValue(null);
        else
            pvMeterSensorSelect.setValue(findHAEntityDtoByEntityId(powerMeterEntities, homeInfo.getPvProductionSensorId()));

        if(homeInfo.getPvStorageSensorId() == null)
            storageMeterSensorSelect.setValue(null);
        else
            storageMeterSensorSelect.setValue(findHAEntityDtoByEntityId(powerMeterEntities, homeInfo.getPvStorageSensorId()));
    }

    private HAEntityDto findHAEntityDtoByEntityId(List<HAEntityDto> haEntityDtos, String entityId){
        return haEntityDtos.stream()
                .filter(haEntityDto -> haEntityDto.getEntityId().equals(entityId))
                .findFirst().orElse(null);
    }

    private HorizontalLayout createButtonLayout(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(createSyncButton(), createSaveButton());
        return horizontalLayout;
    }


    private Button createSaveButton(){
        Button button = new Button(EnText.SAVE);
        button.addClickListener( e -> save());
        return button;
    }

    private Button createSyncButton(){
        Button button = new Button(EnText.SYNC_DEVICES);
        button.addClickListener( e -> syncHassDevices());
        return button;
    }

    private void syncHassDevices() {
        error.setText(EnText.SYNC_IN_PROGRESS);
        entitiesScheduledApplicationService.getAllHomeAssistantEntities();
        entitiesScheduledApplicationService.getAllHomsaiEntityValues();
        error.setText(EnText.SYNC_DONE);
    }

    private void save() {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();

        if(generalMeterSensorSelect.getValue() == null)
            homeInfo.setGeneralPowerMeterId(null);
        else
            homeInfo.setGeneralPowerMeterId(generalMeterSensorSelect.getValue().getEntityId());

        if(hvacMeterSensorSelect.getValue() == null)
            homeInfo.setHvacPowerMeterId(null);
        else
            homeInfo.setHvacPowerMeterId(hvacMeterSensorSelect.getValue().getEntityId());

        if(pvMeterSensorSelect.getValue() == null)
            homeInfo.setPvProductionSensorId(null);
        else
            homeInfo.setPvProductionSensorId(pvMeterSensorSelect.getValue().getEntityId());

        if(storageMeterSensorSelect.getValue() == null)
            homeInfo.setPvStorageSensorId(null);
        else
            homeInfo.setPvStorageSensorId(storageMeterSensorSelect.getValue().getEntityId());

        entitiesCommandsApplicationService.saveHomeInfo(homeInfo);

        error.getStyle().set("color", "#00FF00");
        error.setText(EnText.DATA_SAVED);
    }

}
