package app.homsai.engine.common.gateways;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class MailGatewayImpl implements MailGateway {

    @Value("${mail.domain}")
    private String host;
    @Value("${mail.port}")
    private int port;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.mailFrom}")
    private String mailFrom;


    @Override
    public Object sendMail(String mailTo, String mailSubject, String mailText) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(mailTo);
        mailMessage.setSubject(mailSubject);
        mailMessage.setText(mailText);
        // Send mail
        mailSender.send(mailMessage);

        return null;
    }

    @Override
    public Object sendMailHtml(String mailTo, String mailSubject, String html, List<String> args)
            throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);


        helper.setFrom(mailFrom);
        helper.setTo(mailTo);
        helper.setSubject(mailSubject);
        helper.setText(html, true);
        // Send mail
        mailSender.send(mimeMessage);

        return null;

    }
}
