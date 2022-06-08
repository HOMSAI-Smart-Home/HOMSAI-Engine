package app.homsai.engine.entities.application.http.controllers;

import app.homsai.engine.common.domain.models.DocsConsts;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
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
    public ResponseEntity initHVACDevices(@RequestParam(value = "type", required = true) Integer type) throws InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(entitiesCommandsApplicationService.initHVACDevices(type));
    }

}
