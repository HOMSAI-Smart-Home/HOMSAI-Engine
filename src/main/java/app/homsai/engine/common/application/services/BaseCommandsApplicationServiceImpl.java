package app.homsai.engine.common.application.services;

import app.homsai.engine.common.application.http.dtos.LoggedDto;
import app.homsai.engine.common.application.http.dtos.SettingsDto;
import app.homsai.engine.common.application.http.dtos.TokenDto;
import app.homsai.engine.common.domain.exceptions.TokenIsNullException;
import app.homsai.engine.common.gateways.dtos.MailCreateCommandDto;
import app.homsai.engine.common.gateways.dtos.MailQueriesDto;
import app.homsai.engine.common.gateways.MailgunGateway;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class BaseCommandsApplicationServiceImpl implements BaseCommandsApplicationService {

    @Autowired
    MailgunGateway mailgunGateway;

    //ToDO move HomeInfo to Common
    @Autowired
    EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    //ToDO move HomeInfo to Common
    @Autowired
    EntitiesCommandsApplicationService entitiesCommandsApplicationService;

    @Autowired
    ModelMapper modelMapper;

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

    @Override
    public void injectToken(TokenDto tokenDto) throws TokenIsNullException {
        if(tokenDto.getToken() == null || tokenDto.getRefreshToken() == null || tokenDto.getEmail() == null)
            throw new TokenIsNullException();
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        homeInfo.setAiserviceToken(tokenDto.getToken());
        homeInfo.setAiserviceRefreshToken(tokenDto.getRefreshToken());
        homeInfo.setAiserviceEmail(tokenDto.getEmail());
        entitiesCommandsApplicationService.saveHomeInfo(homeInfo);
    }

    @Override
    public void deleteToken() {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        homeInfo.setAiserviceToken(null);
        homeInfo.setAiserviceRefreshToken(null);
        homeInfo.setAiserviceEmail(null);
        entitiesCommandsApplicationService.saveHomeInfo(homeInfo);
    }

    @Override
    public LoggedDto isLogged() {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        LoggedDto loggedDto = new LoggedDto();
        loggedDto.setLogged(homeInfo.getAiserviceToken() != null && homeInfo.getAiserviceRefreshToken() != null);
        return loggedDto;
    }

    @Override
    public SettingsDto updateSettings(SettingsDto settingsDto) {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        homeInfo.setGeneralPowerMeterId(settingsDto.getGeneralPowerMeterId());
        homeInfo.setHvacPowerMeterId(settingsDto.getHvacPowerMeterId());
        homeInfo.setHvacSummerPowerMeterId(settingsDto.getHvacSummerPowerMeterId());
        homeInfo.setHvacWinterPowerMeterId(settingsDto.getHvacWinterPowerMeterId());
        homeInfo.setPvProductionSensorId(settingsDto.getPvProductionSensorId());
        homeInfo.setPvStorageSensorId(settingsDto.getPvStorageSensorId());
        homeInfo.setLatitude(settingsDto.getLatitude());
        homeInfo.setLongitude(settingsDto.getLongitude());
        homeInfo.setPvPeakPower(settingsDto.getPvPeakPower());
        homeInfo.setPvInstallDate(settingsDto.getPvInstallDate());
        entitiesCommandsApplicationService.saveHomeInfo(homeInfo);
        return getSettingsDtoWithPowerMeterIds(settingsDto);
    }

    @Override
    public SettingsDto readSettings() {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        SettingsDto settingsDto = modelMapper.map(homeInfo, SettingsDto.class);
        return getSettingsDtoWithPowerMeterIds(settingsDto);
    }

    private SettingsDto getSettingsDtoWithPowerMeterIds(SettingsDto settingsDto) {
        if (settingsDto.getHvacPowerMeterId() != null) {
            if (settingsDto.getHvacSummerPowerMeterId() == null) {
                settingsDto.setHvacSummerPowerMeterId(settingsDto.getHvacPowerMeterId());
            }
            if (settingsDto.getHvacWinterPowerMeterId() == null) {
                settingsDto.setHvacWinterPowerMeterId(settingsDto.getHvacPowerMeterId());
            }
        }
        return settingsDto;
    }

}
