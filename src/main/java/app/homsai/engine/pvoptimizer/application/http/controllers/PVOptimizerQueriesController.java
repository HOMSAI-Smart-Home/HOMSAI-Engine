package app.homsai.engine.pvoptimizer.application.http.controllers;


import app.homsai.engine.pvoptimizer.application.services.PVOptimizerQueriesApplicationService;
import app.homsai.engine.pvoptimizer.domain.exceptions.HvacEntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PVOptimizerQueriesController {

    @Autowired
    private PVOptimizerQueriesApplicationService pvOptimizerQueriesApplicationService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @RequestMapping(value = "/entities/homsai/hvac/init/status", method = RequestMethod.GET)
    public ResponseEntity getInitStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(
                pvOptimizerQueriesApplicationService.getHvacInitStatus());
    }

    @RequestMapping(value = "/entities/homsai/hvac/init/estimated", method = RequestMethod.GET)
    public ResponseEntity getInitEstimatedTime(@RequestParam(value = "type", required = true) Integer type) {
        return ResponseEntity.status(HttpStatus.OK).body(
                pvOptimizerQueriesApplicationService.getHvacInitEstimated(type));
    }

    @RequestMapping(value = "/entities/homsai/hvac", method = RequestMethod.GET)
    public ResponseEntity getHvacEntities() {
        return ResponseEntity.status(HttpStatus.OK).body(
                pvOptimizerQueriesApplicationService.getHvacEntities());
    }

    @RequestMapping(value = "/entities/homsai/hvac/{entityUuid}", method = RequestMethod.GET)
    public ResponseEntity getOneHvacEntity(
            @PathVariable("entityUuid") String entityUuid) throws HvacEntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(pvOptimizerQueriesApplicationService.getOneHvacEntity(entityUuid));
    }

    @RequestMapping(value = "/entities/homsai/home/settings", method = RequestMethod.GET)
    public ResponseEntity getHomsaiHvacSettings() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(pvOptimizerQueriesApplicationService.getHomsaiHvacSettings());
    }

    @RequestMapping(value = "/entities/homsai/equipments", method = RequestMethod.GET)
    public ResponseEntity getHvacEquipments(@RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(
                pvOptimizerQueriesApplicationService.getHvacEquipments(search));
    }
}

