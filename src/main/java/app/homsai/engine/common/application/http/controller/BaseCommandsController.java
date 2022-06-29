package app.homsai.engine.common.application.http.controller;

import app.homsai.engine.common.application.http.dtos.SettingsDto;
import app.homsai.engine.common.application.http.dtos.TokenDto;
import app.homsai.engine.common.domain.exceptions.TokenIsNullException;
import app.homsai.engine.common.gateways.dtos.MailCreateCommandDto;
import app.homsai.engine.common.application.services.BaseCommandsApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        logger.debug("[BaseCommandsController] GET /version");
        return ResponseEntity.status(HttpStatus.OK).body(buildProperties.getVersion());
    }


    @RequestMapping(value = "/sendmail", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendMail(@RequestBody MailCreateCommandDto mailCreateCommandDto) {
        logger.debug("[BaseCommandsController] POST /sendmail");

        return ResponseEntity.status(HttpStatus.OK)
                .body(baseCommandsApplicationService.sendMail(mailCreateCommandDto));
    }


    @RequestMapping(value = "/sendmailhtml", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendMailHtml(@RequestBody MailCreateCommandDto mailCreateCommandDto) {
        logger.debug("[BaseCommandsController] POST /sendmailhtml");

        return ResponseEntity.status(HttpStatus.OK)
                .body(baseCommandsApplicationService.sendMailHtml(mailCreateCommandDto));
    }

    @RequestMapping(value = "/auth/token/inject", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity injectToken(@RequestBody TokenDto tokenDto) throws TokenIsNullException {
        logger.debug("[BaseCommandsController] POST /auth/token/inject");
        baseCommandsApplicationService.injectToken(tokenDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @RequestMapping(value = "/auth/token/remove", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity injectToken() {
        logger.debug("[BaseCommandsController] POST /auth/token/remove");
        baseCommandsApplicationService.deleteToken();
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @RequestMapping(value = "/auth/islogged", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity isLogged() {
        logger.debug("[BaseCommandsController] GET /auth/islogged");
        return ResponseEntity.status(HttpStatus.OK).body(baseCommandsApplicationService.isLogged());
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateSettings(@RequestBody SettingsDto settingsDto) {
        logger.debug("[BaseCommandsController] POST /settings");
        return ResponseEntity.status(HttpStatus.OK)
                .body(baseCommandsApplicationService.updateSettings(settingsDto));
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ResponseEntity readSettings() {
        logger.debug("[BaseCommandsController] GET /settings");
        return ResponseEntity.status(HttpStatus.OK)
                .body(baseCommandsApplicationService.readSettings());
    }
}
