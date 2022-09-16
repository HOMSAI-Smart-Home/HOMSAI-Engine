package app.homsai.engine.users;

import app.homsai.engine.common.application.http.dtos.SettingsDto;
import app.homsai.engine.entities.application.services.EntitiesScheduledApplicationService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantRestAPIGateway;
import app.homsai.engine.pvoptimizer.application.services.PVOptimizerScheduledApplicationService;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerEngineService;
import app.homsai.engine.pvoptimizer.gateways.HomsaiAIServiceGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(classes = {EntitiesScheduledApplicationService.class, HomeAssistantRestAPIGateway.class, HomsaiAIServiceGateway.class, PVOptimizerScheduledApplicationService.class})
public class SettingsCRUDTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static String token;

    @Autowired
    private Environment env;

    private final String readSettingsEndpoint = "/settings";
    private final String updateSettingsEndpoint = "/settings";



    @Test
    public void whenUpdateSettings_thenReadRightValues() {

        // Check right init values
        ResponseEntity<SettingsDto> defaultSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readSettingsEndpoint, SettingsDto.class);
        assertThat(defaultSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(defaultSettings.getBody()).getGeneralPowerMeterId()).isEqualTo("sensor.general");
        assertThat(Objects.requireNonNull(defaultSettings.getBody()).getHvacPowerMeterId()).isEqualTo("sensor.hvac");
        assertThat(Objects.requireNonNull(defaultSettings.getBody()).getPvProductionSensorId()).isEqualTo("sensor.production");
        assertThat(Objects.requireNonNull(defaultSettings.getBody()).getPvStorageSensorId()).isEqualTo("sensor.storage");
        assertThat(Objects.requireNonNull(defaultSettings.getBody()).getLatitude()).isNull();
        assertThat(Objects.requireNonNull(defaultSettings.getBody()).getLongitude()).isNull();
        assertThat(Objects.requireNonNull(defaultSettings.getBody()).getPvPeakPower()).isNull();
        assertThat(Objects.requireNonNull(defaultSettings.getBody()).getPvInstallDate()).isNull();

        // Updating settings
        String generalPowerMeterId = "generalPowerMeterId";
        String hvacPowerMeterId = "hvacPowerMeterId";
        String getPvProductionSensorId = "getPvProductionSensorId";
        String getPvStorageSensorId = "getPvStorageSensorId";
        Double getLatitude = 10d;
        Double getLongitude = 20d;
        Double getPvPeakPower = 30d;
        Instant getPvInstallDate = Instant.EPOCH;
        SettingsDto settingsDto = new SettingsDto();
        settingsDto.setGeneralPowerMeterId(generalPowerMeterId);
        settingsDto.setHvacPowerMeterId(hvacPowerMeterId);
        settingsDto.setPvProductionSensorId(getPvProductionSensorId);
        settingsDto.setPvStorageSensorId(getPvStorageSensorId);
        settingsDto.setLatitude(getLatitude);
        settingsDto.setLongitude(getLongitude);
        settingsDto.setPvPeakPower(getPvPeakPower);
        settingsDto.setPvInstallDate(getPvInstallDate);
        HttpEntity<SettingsDto> request = new HttpEntity<>(settingsDto);
        ResponseEntity<SettingsDto> updateSettingsResponse  =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + updateSettingsEndpoint,
                        request, SettingsDto.class);
        assertThat(updateSettingsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(updateSettingsResponse.getBody()).getHvacSummerPowerMeterId()).isEqualTo(hvacPowerMeterId);
        assertThat(Objects.requireNonNull(updateSettingsResponse.getBody()).getHvacWinterPowerMeterId()).isEqualTo(hvacPowerMeterId);

        // check read settings values
        ResponseEntity<SettingsDto> newSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readSettingsEndpoint, SettingsDto.class);
        assertThat(newSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getGeneralPowerMeterId()).isEqualTo(generalPowerMeterId);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getHvacPowerMeterId()).isEqualTo(hvacPowerMeterId);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getPvProductionSensorId()).isEqualTo(getPvProductionSensorId);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getPvStorageSensorId()).isEqualTo(getPvStorageSensorId);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getLatitude()).isEqualTo(getLatitude);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getLongitude()).isEqualTo(getLongitude);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getPvPeakPower()).isEqualTo(getPvPeakPower);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getPvInstallDate()).isEqualTo(getPvInstallDate);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getHvacSummerPowerMeterId()).isEqualTo(hvacPowerMeterId);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getHvacWinterPowerMeterId()).isEqualTo(hvacPowerMeterId);

        // set settings back to null
        settingsDto.setGeneralPowerMeterId(null);
        settingsDto.setHvacPowerMeterId(null);
        settingsDto.setHvacSummerPowerMeterId(null);
        settingsDto.setHvacWinterPowerMeterId(null);
        settingsDto.setPvProductionSensorId(null);
        settingsDto.setPvStorageSensorId(null);
        settingsDto.setLatitude(null);
        settingsDto.setLongitude(null);
        settingsDto.setPvPeakPower(null);
        settingsDto.setPvInstallDate(null);
        request = new HttpEntity<>(settingsDto);
        updateSettingsResponse  =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + updateSettingsEndpoint,
                        request, SettingsDto.class);
        assertThat(updateSettingsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check right null values
        ResponseEntity<SettingsDto> nullSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readSettingsEndpoint, SettingsDto.class);
        assertThat(nullSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(nullSettings.getBody()).getGeneralPowerMeterId()).isNull();
        assertThat(Objects.requireNonNull(nullSettings.getBody()).getHvacPowerMeterId()).isNull();
        assertThat(Objects.requireNonNull(nullSettings.getBody()).getHvacSummerPowerMeterId()).isNull();
        assertThat(Objects.requireNonNull(nullSettings.getBody()).getHvacWinterPowerMeterId()).isNull();
        assertThat(Objects.requireNonNull(nullSettings.getBody()).getPvProductionSensorId()).isNull();
        assertThat(Objects.requireNonNull(nullSettings.getBody()).getPvStorageSensorId()).isNull();
        assertThat(Objects.requireNonNull(nullSettings.getBody()).getLatitude()).isNull();
        assertThat(Objects.requireNonNull(nullSettings.getBody()).getLongitude()).isNull();
        assertThat(Objects.requireNonNull(nullSettings.getBody()).getPvPeakPower()).isNull();
        assertThat(Objects.requireNonNull(nullSettings.getBody()).getPvInstallDate()).isNull();
    }
}
