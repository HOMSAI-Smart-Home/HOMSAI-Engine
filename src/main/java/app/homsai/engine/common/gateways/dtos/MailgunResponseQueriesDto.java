package app.homsai.engine.common.gateways.dtos;

public class MailgunResponseQueriesDto {

    private String responseMessage;
    private int responseCode;

    public MailgunResponseQueriesDto(String responseMessage, int responseCode) {
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
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
