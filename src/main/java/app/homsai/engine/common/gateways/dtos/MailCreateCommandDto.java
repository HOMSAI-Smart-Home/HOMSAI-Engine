package app.homsai.engine.common.gateways.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MailCreateCommandDto {
    @JsonProperty("mail_to")
    private String mailTo;

    @JsonProperty("mail_subject")
    private String mailSubject;

    @JsonProperty("mail_text")
    private String mailText;

    @JsonProperty("arguments")
    private List<String> args;

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailText() {
        return mailText;
    }

    public void setMailText(String mailText) {
        this.mailText = mailText;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
