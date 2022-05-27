package app.homsai.engine.common.application.services;

import app.homsai.engine.common.application.http.dtos.MailCreateCommandDto;
import app.homsai.engine.common.application.http.dtos.MailQueriesDto;

public interface BaseCommandsApplicationService {

    MailQueriesDto sendMail(MailCreateCommandDto mailCreateCommandDto);

    MailQueriesDto sendMailHtml(MailCreateCommandDto mailCreateCommandDto);
}
