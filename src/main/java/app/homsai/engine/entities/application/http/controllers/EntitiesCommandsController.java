package app.homsai.engine.entities.application.http.controllers;

import app.homsai.engine.common.domain.models.DocsConsts;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = DocsConsts.DOCS_TAGS_COMMON)
@RestController
public class EntitiesCommandsController {

    @Autowired
    private EntitiesCommandsApplicationService entitiesCommandsApplicationService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @RequestMapping(value = "/entities", method = RequestMethod.POST)
    public ResponseEntity syncHomeAssistantEntities() {
        entitiesCommandsApplicationService.syncHomeAssistantEntities();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
