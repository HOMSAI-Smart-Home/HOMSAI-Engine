package app.homsai.engine.entities.application.http.controllers;

import app.homsai.engine.common.domain.models.DocsConsts;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.exceptions.HvacEntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EntitiesQueriesController {

    @Autowired
    private EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @RequestMapping(value = "/entities/hass", method = RequestMethod.GET)
    public ResponseEntity getAllHomeAssistantEntities(@PageableDefault(sort = {"uuid"}) Pageable pageRequest,
                                         @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(
                entitiesQueriesApplicationService.getAllHomeAssistantEntities(pageRequest, search));
    }

    @RequestMapping(value = "/entities/homsai", method = RequestMethod.GET)
    public ResponseEntity getAllHomsaiEntities(@PageableDefault(sort = {"uuid"}) Pageable pageRequest,
                                         @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(
                entitiesQueriesApplicationService.getAllHomsaiEntities(pageRequest, search));
    }

    @RequestMapping(value = "/entities/history/homsai", method = RequestMethod.GET)
    public ResponseEntity getAllHomsaiHistoricalStates(@PageableDefault(sort = {"uuid"}) Pageable pageRequest,
                                               @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(
                entitiesQueriesApplicationService.getAllHomsaiHistoricalStates(pageRequest, search));
    }

    @RequestMapping(value = "/entities/homsai/hvac/init/status", method = RequestMethod.GET)
    public ResponseEntity getInitStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(
                entitiesQueriesApplicationService.getHvacInitStatus());
    }

    @RequestMapping(value = "/entities/homsai/hvac", method = RequestMethod.GET)
    public ResponseEntity getHvacEntities() {
        return ResponseEntity.status(HttpStatus.OK).body(
                entitiesQueriesApplicationService.getHvacEntities());
    }

    @RequestMapping(value = "/entities/homsai/hvac/{entityUuid}", method = RequestMethod.GET)
    public ResponseEntity getOneHvacEntity(
            @PathVariable("entityUuid") String entityUuid) throws HvacEntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(entitiesQueriesApplicationService.getOneHvacEntity(entityUuid));
    }

    @RequestMapping(value = "/entities/homsai/home/settings", method = RequestMethod.GET)
    public ResponseEntity getHomsaiHvacSettings() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(entitiesQueriesApplicationService.getHomsaiHvacSettings());
    }
}
