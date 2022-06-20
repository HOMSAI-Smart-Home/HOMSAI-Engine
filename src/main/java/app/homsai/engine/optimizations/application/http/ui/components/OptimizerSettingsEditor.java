package app.homsai.engine.optimizations.application.http.ui.components;

import app.homsai.engine.entities.application.http.cache.HomsaiHVACDeviceCacheRepository;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HVACDevice;
import app.homsai.engine.entities.domain.models.HvacDeviceInterval;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.entities.domain.services.EntitiesQueriesService;
import app.homsai.engine.optimizations.domain.models.HvacDevice;
import app.homsai.engine.optimizations.infrastructure.cache.HVACRunningDevicesCacheRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Collections;

@SpringComponent
@UIScope
public class OptimizerSettingsEditor extends VerticalLayout implements KeyNotifier {

    private HVACRunningDevicesCacheRepository hvacRunningDevicesCacheRepository;
    private EntitiesQueriesService entitiesQueriesService;
    private EntitiesCommandsService entitiesCommandsService;

    private HVACDevice hvacDevice;
    private Area area;

    Checkbox enabled = new Checkbox("Enabled");
    Checkbox auto = new Checkbox("Auto");

    TextField setTemp = new TextField("Desired Temperature");
    TimePicker startTime = new TimePicker("Restrict start hour - leave blank for no limit");
    TimePicker endTime = new TimePicker("Restrict end hour - leave blank for no limit");
    Label error = new Label();
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Close");
    HorizontalLayout actions = new HorizontalLayout(save, cancel);

    @Autowired
    public OptimizerSettingsEditor(EntitiesQueriesService entitiesQueriesService, EntitiesCommandsService entitiesCommandsService, HVACRunningDevicesCacheRepository hvacRunningDevicesCacheRepository) {
        this.entitiesQueriesService = entitiesQueriesService;
        this.entitiesCommandsService = entitiesCommandsService;
        this.hvacRunningDevicesCacheRepository = hvacRunningDevicesCacheRepository;
        HorizontalLayout checkboxes = new HorizontalLayout(enabled, auto);
        add(checkboxes, setTemp, startTime, endTime, error, actions);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        cancel.addClickListener(e -> setVisible(false));
        setVisible(false);
    }


    void save() {
        try {
            Boolean enabledValue = enabled.getValue();
            Boolean autoMode = auto.getValue();
            String desiredTemperature = setTemp.getValue();
            LocalTime startTimeValue = startTime.getValue();
            LocalTime endTimeValue = endTime.getValue();
            hvacDevice.setEnabled(enabledValue);
            hvacRunningDevicesCacheRepository.getHvacDevicesCache().get(hvacDevice.getEntityId()).setManual(!autoMode);
            hvacDevice.getArea().setDesiredSummerTemperature(Double.parseDouble(desiredTemperature));
            if(startTimeValue != null && endTimeValue != null) {
                LocalTime start = startTimeValue.minusSeconds(OffsetDateTime.now().getOffset().getTotalSeconds());
                LocalTime end = endTimeValue.minusSeconds(OffsetDateTime.now().getOffset().getTotalSeconds());
                HvacDeviceInterval hvacInterval = new HvacDeviceInterval(start, end);
                hvacDevice.setIntervals(Collections.singletonList(hvacInterval));
            }
            else if(startTimeValue == null && endTimeValue == null)
                hvacDevice.setIntervals(null);
            else throw new Exception();
            hvacDevice = entitiesCommandsService.updateHvacDevice(hvacDevice);
            area = entitiesCommandsService.updateArea(area);
        }catch (Exception e){
            error.getStyle().set("color", "#FF0000");
            error.setText("Error: please check input values");
            return;
        }
        error.getStyle().set("color", "#00FF00");
        error.setText("Data saved correctly");

    }


    @Transactional
    public void editHvacDeviceOptSetting(HvacDevice h) {
        String entityId;
        if (h == null){
            setVisible(false);
            return;
        }
        else
            entityId = h.getEntityId();
        error.setText(null);
        hvacDevice = entitiesQueriesService.findOneHvacDeviceByEntityId(entityId);
        area = hvacDevice.getArea();
        cancel.setVisible(true);
        auto.setValue(!hvacRunningDevicesCacheRepository.getHvacDevicesCache().get(entityId).isManual());
        enabled.setValue(hvacDevice.getEnabled());
        if(hvacDevice.getArea().getDesiredSummerTemperature() != null)
            setTemp.setValue(hvacDevice.getArea().getDesiredSummerTemperature().toString());
        else
            setTemp.setValue(entitiesQueriesService.getHomeArea().getDesiredSummerTemperature().toString());
        if(hvacDevice.getIntervals().size() > 0) {
            LocalTime start = hvacDevice.getIntervals().get(0).getStartTime().plusSeconds(OffsetDateTime.now().getOffset().getTotalSeconds());
            LocalTime end = hvacDevice.getIntervals().get(0).getEndTime().plusSeconds(OffsetDateTime.now().getOffset().getTotalSeconds());
            startTime.setValue(start);
            endTime.setValue(end);
            startTime.setClearButtonVisible(true);
            endTime.setClearButtonVisible(true);
        } else {
            startTime.setValue(null);
            endTime.setValue(null);
        }

        setVisible(true);
        enabled.focus();
    }
}
