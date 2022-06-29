package app.homsai.engine.common.application.services;

import app.homsai.engine.common.application.http.dtos.LoggedDto;
import app.homsai.engine.common.application.http.dtos.TokenDto;
import app.homsai.engine.common.domain.exceptions.TokenIsNullException;
import app.homsai.engine.common.gateways.dtos.MailCreateCommandDto;
import app.homsai.engine.common.gateways.dtos.MailQueriesDto;

public interface BaseCommandsApplicationService {

    MailQueriesDto sendMail(MailCreateCommandDto mailCreateCommandDto);

    MailQueriesDto sendMailHtml(MailCreateCommandDto mailCreateCommandDto);

    void injectToken(TokenDto tokenDto) throws TokenIsNullException;

    void deleteToken();

    LoggedDto isLogged();
}
