package app.homsai.engine.common.application.services;

import app.homsai.engine.common.application.http.dtos.LoggedDto;
import app.homsai.engine.common.application.http.dtos.TokenDto;
import app.homsai.engine.common.domain.exceptions.TokenIsNullException;
import app.homsai.engine.common.gateways.dtos.MailCreateCommandDto;
import app.homsai.engine.common.gateways.dtos.MailQueriesDto;
import app.homsai.engine.common.gateways.MailgunGateway;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class BaseCommandsApplicationServiceImpl implements BaseCommandsApplicationService {

    @Autowired
    MailgunGateway mailgunGateway;

    //ToDO move HomeInfo to Common
    @Autowired
    EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    //ToDO move HomeInfo to Common
    @Autowired
    EntitiesCommandsApplicationService entitiesCommandsApplicationService;

    @Override
    public MailQueriesDto sendMail(MailCreateCommandDto mailCreateCommandDto) {
        String mailTo = mailCreateCommandDto.getMailTo();
        String mailSubject = mailCreateCommandDto.getMailSubject();
        String mailText = mailCreateCommandDto.getMailText();

        Object mailgunResponseQueriesDto = mailgunGateway.sendMail(mailTo, mailSubject, mailText);

        MailQueriesDto mailQueriesDto = new MailQueriesDto();

        mailQueriesDto.setMailTo(mailCreateCommandDto.getMailTo());
        mailQueriesDto.setMailSubject(mailCreateCommandDto.getMailSubject());
        mailQueriesDto.setMailText(mailCreateCommandDto.getMailText());

        return mailQueriesDto;
    }

    @Override
    public MailQueriesDto sendMailHtml(MailCreateCommandDto mailCreateCommandDto) {
        String mailTo = mailCreateCommandDto.getMailTo();
        String mailSubject = mailCreateCommandDto.getMailSubject();
        String mailText = mailCreateCommandDto.getMailText();
        List<String> args = mailCreateCommandDto.getArgs();

        try {
            Object mailgunResponseQueriesDto =
                    mailgunGateway.sendMailHtml(mailTo, mailSubject, mailText, args);
        } catch (IOException e) {
            // e.printStackTrace();
        }

        MailQueriesDto mailQueriesDto = new MailQueriesDto();

        mailQueriesDto.setMailTo(mailCreateCommandDto.getMailTo());
        mailQueriesDto.setMailSubject(mailCreateCommandDto.getMailSubject());
        mailQueriesDto.setMailText(mailCreateCommandDto.getMailText());

        return mailQueriesDto;
    }

    @Override
    public void injectToken(TokenDto tokenDto) throws TokenIsNullException {
        if(tokenDto.getToken() == null || tokenDto.getRefreshToken() == null)
            throw new TokenIsNullException();
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        homeInfo.setAiserviceToken(tokenDto.getToken());
        homeInfo.setAiserviceRefreshToken(tokenDto.getRefreshToken());
        entitiesCommandsApplicationService.saveHomeInfo(homeInfo);
    }

    @Override
    public void deleteToken() {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        homeInfo.setAiserviceToken(null);
        homeInfo.setAiserviceRefreshToken(null);
        entitiesCommandsApplicationService.saveHomeInfo(homeInfo);
    }

    @Override
    public LoggedDto isLogged() {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        LoggedDto loggedDto = new LoggedDto();
        loggedDto.setLogged(homeInfo.getAiserviceToken() != null && homeInfo.getAiserviceRefreshToken() != null);
        return loggedDto;
    }


}
