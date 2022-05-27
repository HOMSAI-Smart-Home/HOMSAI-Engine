package app.homsai.engine.common.gateways;

import app.homsai.engine.common.application.http.dtos.MailgunResponseQueriesDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface MailgunGateway {

    MailgunResponseQueriesDto sendMail(String mailTo, String mailSubject, String mailText);


    MailgunResponseQueriesDto sendMailHtml(String mailTo, String mailSubject, String html,
            List<String> args) throws IOException;

    MailgunResponseQueriesDto sendMailHtmlWithAttachments(String mailTo, String mailSubject,
            String html, List<String> args, List<File> files, String body, boolean buttonVisibility,
            String buttonLink) throws IOException;
}
