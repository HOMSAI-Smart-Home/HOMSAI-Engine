package app.homsai.engine.common.gateways;

import javax.mail.MessagingException;
import java.util.List;

public interface MailGateway {

    Object sendMail(String mailTo, String mailSubject, String mailText);


    Object sendMailHtml(String mailTo, String mailSubject, String html, List<String> args)
            throws MessagingException;
}
