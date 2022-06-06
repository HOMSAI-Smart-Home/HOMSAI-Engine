package app.homsai.engine.common.application.http.controller;

import app.homsai.engine.common.application.http.dtos.MailCreateCommandDto;
import app.homsai.engine.common.application.http.dtos.MailQueriesDto;
import app.homsai.engine.common.application.services.BaseCommandsApplicationService;
import app.homsai.engine.common.domain.models.DocsConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseCommandsController {

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private BaseCommandsApplicationService baseCommandsApplicationService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public ResponseEntity getVersion() {
        logger.info("[BaseCommandsController] GET /version");
        return ResponseEntity.status(HttpStatus.OK).body(buildProperties.getVersion());
    }


    @RequestMapping(value = "/sendmail", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendMail(@RequestBody MailCreateCommandDto mailCreateCommandDto) {
        logger.info("[BaseCommandsController] POST /sendmail");

        return ResponseEntity.status(HttpStatus.OK)
                .body(baseCommandsApplicationService.sendMail(mailCreateCommandDto));
    }


    @RequestMapping(value = "/sendmailhtml", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendMailHtml(@RequestBody MailCreateCommandDto mailCreateCommandDto) {
        logger.info("[BaseCommandsController] POST /sendmailhtml");

        return ResponseEntity.status(HttpStatus.OK)
                .body(baseCommandsApplicationService.sendMailHtml(mailCreateCommandDto));
    }
}
