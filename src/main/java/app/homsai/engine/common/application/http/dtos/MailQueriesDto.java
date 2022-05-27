package app.homsai.engine.common.application.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MailQueriesDto {

    @JsonProperty("mail_to")
    private String mailTo;

    @JsonProperty("mail_subject")
    private String mailSubject;

    @JsonProperty("mail_text")
    private String mailText;

    @JsonProperty("response_message")
    private String responseMessage;

    @JsonProperty("response_code")
    private int responseCode;

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

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
