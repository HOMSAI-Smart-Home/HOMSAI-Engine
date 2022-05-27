package app.homsai.engine.common.application.services;

import app.homsai.engine.common.application.http.dtos.MailCreateCommandDto;
import app.homsai.engine.common.application.http.dtos.MailQueriesDto;
import app.homsai.engine.common.gateways.MailgunGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class BaseCommandsApplicationServiceImpl implements BaseCommandsApplicationService {

    @Autowired
    MailgunGateway mailgunGateway;

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
}
