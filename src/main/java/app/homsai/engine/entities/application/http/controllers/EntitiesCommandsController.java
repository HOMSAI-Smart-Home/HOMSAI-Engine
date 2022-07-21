package app.homsai.engine.entities.application.http.controllers;

import app.homsai.engine.common.domain.exceptions.BadRequestException;
import app.homsai.engine.common.domain.models.DocsConsts;
import app.homsai.engine.entities.application.http.dtos.HomeHvacSettingsDto;
import app.homsai.engine.entities.application.http.dtos.HvacDeviceSettingDto;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.exceptions.BadIntervalsException;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EntitiesCommandsController {

    @Autowired
    private EntitiesCommandsApplicationService entitiesCommandsApplicationService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @RequestMapping(value = "/entities/hass", method = RequestMethod.POST)
    public ResponseEntity syncHomeAssistantEntities() throws InterruptedException {
        entitiesCommandsApplicationService.syncHomeAssistantEntities();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "/entities/homsai", method = RequestMethod.POST)
    public ResponseEntity syncHomsaiEntities() throws AreaNotFoundException {
        entitiesCommandsApplicationService.syncHomsaiEntitiesValues();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "/entities/hass/excluded", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addExcludedHAEntities(@RequestBody List<String> excludedIds) throws InterruptedException {
        entitiesCommandsApplicationService.addExcludedHAEntities(excludedIds);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "/entities/homsai/hvac/init", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity initHVACDevices(@RequestParam(value = "type", required = true) Integer type) throws InterruptedException, HvacPowerMeterIdNotSet {
        return ResponseEntity.status(HttpStatus.OK).body(entitiesCommandsApplicationService.initHVACDevices(type));
    }

    @RequestMapping(value = "/entities/homsai/hvac/settings/{entityId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editHvacDeviceSettings(@RequestBody HvacDeviceSettingDto hvacDeviceSettingDto,
                                                 @PathVariable("entityId") String entityUuid) throws BadIntervalsException {
        return ResponseEntity.status(HttpStatus.OK).body(entitiesCommandsApplicationService.updateHvacDeviceSetting(entityUuid, hvacDeviceSettingDto));
    }


    @RequestMapping(value = "/entities/homsai/home/settings", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editHomeHvacSettings(@RequestBody HomeHvacSettingsDto homeHvacSettingsDto) throws BadRequestException, BadHomeInfoException {
        return ResponseEntity.status(HttpStatus.OK).body(entitiesCommandsApplicationService.updateHomeHvacSettings(homeHvacSettingsDto));
    }

}
