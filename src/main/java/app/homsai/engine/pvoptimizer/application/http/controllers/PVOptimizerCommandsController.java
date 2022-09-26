package app.homsai.engine.pvoptimizer.application.http.controllers;

import app.homsai.engine.common.domain.exceptions.BadRequestException;
import app.homsai.engine.entities.domain.exceptions.BadHomeInfoException;
import app.homsai.engine.entities.domain.exceptions.BadIntervalsException;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import app.homsai.engine.pvoptimizer.application.http.dtos.HomeHvacSettingsDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HomeHvacSettingsUpdateDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HvacDeviceSettingDto;
import app.homsai.engine.pvoptimizer.application.services.PVOptimizerCommandsApplicationService;
import app.homsai.engine.pvoptimizer.domain.exceptions.ClimateEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PVOptimizerCommandsController {

    @Autowired
    PVOptimizerCommandsApplicationService pvOptimizerCommandsApplicationService;


    @RequestMapping(value = "/entities/homsai/hvac/init", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity initHVACDevices(@RequestParam(value = "type", required = true) Integer type) throws InterruptedException, HvacPowerMeterIdNotSet, ClimateEntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(pvOptimizerCommandsApplicationService.initHVACDevices(type));
    }

    @RequestMapping(value = "/entities/homsai/hvac/settings/{entityId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editHvacDeviceSettings(@RequestBody HvacDeviceSettingDto hvacDeviceSettingDto,
                                                 @PathVariable("entityId") String entityUuid) throws BadIntervalsException {
        return ResponseEntity.status(HttpStatus.OK).body(pvOptimizerCommandsApplicationService.updateHvacDeviceSetting(entityUuid, hvacDeviceSettingDto));
    }


    @RequestMapping(value = "/entities/homsai/home/settings", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editHomeHvacSettings(@RequestBody HomeHvacSettingsUpdateDto homeHvacSettingsUpdateDto) throws BadRequestException, BadHomeInfoException {
        return ResponseEntity.status(HttpStatus.OK).body(pvOptimizerCommandsApplicationService.updateHomeHvacSettings(homeHvacSettingsUpdateDto));
    }

}
