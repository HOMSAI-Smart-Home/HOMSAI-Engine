package app.homsai.engine.homeassistant.application.http.controller;

import app.homsai.engine.common.domain.models.DocsConsts;
import app.homsai.engine.homeassistant.application.services.HomeAssistantQueriesApplicationService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static app.homsai.engine.homeassistant.gateways.HomeAssistantDomains.CLIMATE;

@Api(tags = DocsConsts.DOCS_TAGS_COMMON)
@RestController
public class HomeAssistantQueriesController {

    @Autowired
    private HomeAssistantQueriesApplicationService homeAssistantQueriesApplicationService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @RequestMapping(value = "/entities/climate", method = RequestMethod.GET)
    public ResponseEntity getClimateEntities() {
        return ResponseEntity.status(HttpStatus.OK).body(homeAssistantQueriesApplicationService.getHomeAssistantEntities(CLIMATE));
    }

}
