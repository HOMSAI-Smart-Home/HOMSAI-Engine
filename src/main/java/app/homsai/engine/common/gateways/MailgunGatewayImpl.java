package app.homsai.engine.common.gateways;

import app.homsai.engine.common.gateways.dtos.MailgunResponseQueriesDto;
import net.sargue.mailgun.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class MailgunGatewayImpl implements MailgunGateway {

    @Value("${mailgun.domain:}")
    private String domain;

    @Value("${mailgun.apikey:}")
    private String apiKey;

    @Value("${mailgun.from:}")
    private String from;

    @Value("${mailgun.mailfrom:}")
    private String mailFrom;


    @Override
    public MailgunResponseQueriesDto sendMail(String mailTo, String mailSubject, String mailText) {
        Configuration configuration =
                new Configuration().domain(domain).apiKey(apiKey).from(from, mailFrom);

        Response response = Mail.using(configuration).to(mailTo).subject(mailSubject).text(mailText)
                .build().send();

        return new MailgunResponseQueriesDto(response.responseMessage(), response.responseCode());
    }

    @Override
    public MailgunResponseQueriesDto sendMailHtml(String mailTo, String mailSubject, String html,
            List<String> args) {
        Configuration configuration =
                new Configuration().domain(domain).apiKey(apiKey).from(from, mailFrom);
        int counter = 0;
        for (String arg : args) {
            html = html.replace("%%%ARG" + counter + "%%%", arg);
            counter++;
        }
        Response response =
                Mail.using(configuration).to(mailTo).subject(mailSubject).html(html).build().send();

        return new MailgunResponseQueriesDto(response.responseMessage(), response.responseCode());
    }

    @Override
    public MailgunResponseQueriesDto sendMailHtmlWithAttachments(String mailTo, String mailSubject,
            String html, List<String> args, List<File> files, String body, boolean buttonVisibility,
            String buttonLink) throws IOException {
        Configuration configuration =
                new Configuration().domain(domain).apiKey(apiKey).from(from, mailFrom);
        int counter = 0;
        for (String arg : args) {
            html = html.replace("%%%ARG" + counter + "%%%", arg);
            counter++;
        }
        html = html.replace("%%%BODY%%%", body);
        html = html.replace("%%%BTN_VISIBILITY%%%", buttonVisibility ? "block" : "none");
        html = html.replace("%%%LINK%%%", buttonLink);
        MailBuilder mailBuilder =
                Mail.using(configuration).to(mailTo).subject(mailSubject).html(html);
        MultipartBuilder multipartBuilder = null;
        Mail mail = null;
        if (files != null && files.size() > 0) {
            multipartBuilder = mailBuilder.multipart();
            for (File file : files) {
                multipartBuilder = multipartBuilder.attachment(file);
            }
            mail = multipartBuilder.build();
        } else {
            mail = mailBuilder.build();
        }
        Response response = mail.send();

        return new MailgunResponseQueriesDto(response.responseMessage(), response.responseCode());
    }
}
