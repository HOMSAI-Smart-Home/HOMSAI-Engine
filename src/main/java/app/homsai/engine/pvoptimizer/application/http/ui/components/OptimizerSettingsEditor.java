package app.homsai.engine.pvoptimizer.application.http.ui.components;

import app.homsai.engine.common.domain.utils.EnText;
import app.homsai.engine.common.domain.utils.constants.Consts;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.pvoptimizer.domain.models.HVACDevice;
import app.homsai.engine.pvoptimizer.domain.models.HvacDeviceInterval;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.entities.domain.services.EntitiesQueriesService;
import app.homsai.engine.pvoptimizer.domain.models.OptimizerHVACDevice;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerCommandsService;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerQueriesService;
import app.homsai.engine.pvoptimizer.domain.services.cache.PVOptimizerCacheService;
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

import static app.homsai.engine.common.domain.utils.constants.Consts.HVAC_MODE_WINTER_ID;

@SpringComponent
@UIScope
public class OptimizerSettingsEditor extends VerticalLayout implements KeyNotifier {

    private PVOptimizerCacheService pvoptimizerCacheService;
    private EntitiesQueriesService entitiesQueriesService;
    private EntitiesCommandsService entitiesCommandsService;
    private PVOptimizerQueriesService pvOptimizerQueriesService;
    private PVOptimizerCommandsService pvOptimizerCommandsService;

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
    public OptimizerSettingsEditor(EntitiesQueriesService entitiesQueriesService, EntitiesCommandsService entitiesCommandsService, PVOptimizerCacheService pvoptimizerCacheService, PVOptimizerCommandsService pvOptimizerCommandsService) {
        this.entitiesQueriesService = entitiesQueriesService;
        this.entitiesCommandsService = entitiesCommandsService;
        this.pvoptimizerCacheService = pvoptimizerCacheService;
        this.pvOptimizerCommandsService = pvOptimizerCommandsService;
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
            pvoptimizerCacheService.getHvacDevicesCache().get(hvacDevice.getEntityId()).setManual(!autoMode);
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
            hvacDevice = pvOptimizerCommandsService.updateHvacDevice(hvacDevice);
            area = entitiesCommandsService.updateArea(area);
        }catch (Exception e){
            error.getStyle().set("color", "#FF0000");
            error.setText(EnText.ERROR_DATA_SAVE);
            return;
        }
        error.getStyle().set("color", "#00FF00");
        error.setText(EnText.DATA_SAVED);

    }


    @Transactional
    public void editHvacDeviceOptSetting(OptimizerHVACDevice h) {
        String entityId;
        if (h == null){
            setVisible(false);
            return;
        }
        else
            entityId = h.getEntityId();
        error.setText(null);
        HomeInfo homeInfo = entitiesQueriesService.findHomeInfo();
        Integer type = homeInfo.getOptimizerMode() != null ? homeInfo.getOptimizerMode() : HVAC_MODE_WINTER_ID;
        hvacDevice = pvOptimizerQueriesService.findOneHvacDeviceByEntityIdAndType(entityId, type);
        area = hvacDevice.getArea();
        cancel.setVisible(true);
        auto.setValue(!pvoptimizerCacheService.getHvacDevicesCache().get(entityId).isManual());
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
