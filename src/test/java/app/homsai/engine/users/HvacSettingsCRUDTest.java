package app.homsai.engine.users;

import app.homsai.engine.pvoptimizer.application.http.dtos.HomeHvacSettingsDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HvacDeviceSettingDto;
import app.homsai.engine.entities.application.services.EntitiesScheduledApplicationService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantRestAPIGateway;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantAttributesDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.OptimizerHVACDeviceDto;
import app.homsai.engine.pvoptimizer.application.services.PVOptimizerScheduledApplicationService;
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

import java.time.LocalTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(classes = {EntitiesScheduledApplicationService.class, PVOptimizerScheduledApplicationService.class, HomeAssistantRestAPIGateway.class})
public class HvacSettingsCRUDTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Environment env;

    private final String readSettingsEndpoint = "/entities/homsai/hvac/climate.area1";
    private final String updateSettingsEndpoint = "/entities/homsai/hvac/settings/climate.area1";
    private final String readHomeSettingsEndpoint = "/entities/homsai/home/settings";
    private final String updateHomeSettingsEndpoint = "/entities/homsai/home/settings";

    @Autowired
    HomeAssistantRestAPIGateway homeAssistantRestAPIGateway;


    @Test
    public void whenUpdateHvacSettings_thenReadRightValues() {
        configureMockServices();

        // Check right init values
        ResponseEntity<OptimizerHVACDeviceDto> defaultHvacSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readSettingsEndpoint, OptimizerHVACDeviceDto.class);
        assertThat(defaultHvacSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).getActualPowerConsumption()).isZero();
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).getPowerConsumption()).isEqualTo(1090.63);
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).getActive()).isFalse();
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).getEnabled()).isTrue();
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).isManual()).isFalse();
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).getAreaId()).isEqualTo("area1");
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).getCurrentTemperature()).isNull();
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).getSetTemperature()).isEqualTo(26.0);
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).getIntervals().size()).isZero();

        // Updating settings
        HvacDeviceSettingDto hvacDeviceSettingDto = new HvacDeviceSettingDto();
        hvacDeviceSettingDto.setEnabled(false);
        hvacDeviceSettingDto.setManual(true);
        hvacDeviceSettingDto.setSetTemperature(30.0);
        hvacDeviceSettingDto.setStartTime(LocalTime.of(10, 0));
        hvacDeviceSettingDto.setEndTime(LocalTime.of(15, 0));
        HttpEntity<HvacDeviceSettingDto> request = new HttpEntity<>(hvacDeviceSettingDto);
        ResponseEntity<String> updateSettingsResponse  =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + updateSettingsEndpoint,
                        request, String.class);
        assertThat(updateSettingsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // check read settings values
        ResponseEntity<OptimizerHVACDeviceDto> newSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readSettingsEndpoint, OptimizerHVACDeviceDto.class);
        assertThat(newSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getActualPowerConsumption()).isZero();
        assertThat(Objects.requireNonNull(newSettings.getBody()).getPowerConsumption()).isEqualTo(1090.63);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getActive()).isFalse();
        assertThat(Objects.requireNonNull(newSettings.getBody()).getEnabled()).isFalse();
        assertThat(Objects.requireNonNull(newSettings.getBody()).isManual()).isTrue();
        assertThat(Objects.requireNonNull(newSettings.getBody()).getAreaId()).isEqualTo("area1");
        assertThat(Objects.requireNonNull(newSettings.getBody()).getCurrentTemperature()).isNull();
        assertThat(Objects.requireNonNull(newSettings.getBody()).getSetTemperature()).isEqualTo(30.0);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getIntervals().get(0).getStartTime()).isEqualToIgnoringSeconds(LocalTime.of(10, 0));
        assertThat(Objects.requireNonNull(newSettings.getBody()).getIntervals().get(0).getEndTime()).isEqualToIgnoringSeconds(LocalTime.of(15, 0));


        hvacDeviceSettingDto = new HvacDeviceSettingDto();
        hvacDeviceSettingDto.setEnabled(false);
        hvacDeviceSettingDto.setManual(true);
        hvacDeviceSettingDto.setSetTemperature(26.0);
        hvacDeviceSettingDto.setStartTime(null);
        hvacDeviceSettingDto.setEndTime(null);
        request = new HttpEntity<>(hvacDeviceSettingDto);
        updateSettingsResponse  =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + updateSettingsEndpoint,
                        request, String.class);
        assertThat(updateSettingsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        newSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readSettingsEndpoint, OptimizerHVACDeviceDto.class);
        assertThat(newSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getActualPowerConsumption()).isZero();
        assertThat(Objects.requireNonNull(newSettings.getBody()).getPowerConsumption()).isEqualTo(1090.63);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getActive()).isFalse();
        assertThat(Objects.requireNonNull(newSettings.getBody()).getEnabled()).isFalse();
        assertThat(Objects.requireNonNull(newSettings.getBody()).isManual()).isTrue();
        assertThat(Objects.requireNonNull(newSettings.getBody()).getAreaId()).isEqualTo("area1");
        assertThat(Objects.requireNonNull(newSettings.getBody()).getCurrentTemperature()).isNull();
        assertThat(Objects.requireNonNull(newSettings.getBody()).getSetTemperature()).isEqualTo(26.0);
        assertThat(Objects.requireNonNull(newSettings.getBody()).getIntervals()).isNull();

    }

    @Test
    public void whenUpdateHomeSettings_thenReadRightValues() {

        // Check right init values
        ResponseEntity<HomeHvacSettingsDto> defaultHvacSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readHomeSettingsEndpoint, HomeHvacSettingsDto.class);
        assertThat(defaultHvacSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).getSetTemperature()).isEqualTo(26.0);
        assertThat(Objects.requireNonNull(defaultHvacSettings.getBody()).getOptimizerEnabled()).isEqualTo(false);

        HomeHvacSettingsDto homeHvacSettingsDto = new HomeHvacSettingsDto();
        homeHvacSettingsDto.setOptimizerEnabled(true);
        homeHvacSettingsDto.setSetTemperature(30.0);
        HttpEntity<HomeHvacSettingsDto> request = new HttpEntity<>(homeHvacSettingsDto);
        ResponseEntity<HomeHvacSettingsDto> updateSettingsResponse  =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + updateHomeSettingsEndpoint,
                        request, HomeHvacSettingsDto.class);
        assertThat(updateSettingsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<HomeHvacSettingsDto> newHvacSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readHomeSettingsEndpoint, HomeHvacSettingsDto.class);
        assertThat(newHvacSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(newHvacSettings.getBody()).getSetTemperature()).isEqualTo(30);
        assertThat(Objects.requireNonNull(newHvacSettings.getBody()).getOptimizerEnabled()).isEqualTo(true);

        homeHvacSettingsDto.setOptimizerEnabled(null);
        homeHvacSettingsDto.setSetTemperature(null);
        request = new HttpEntity<>(homeHvacSettingsDto);
        updateSettingsResponse  =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + updateHomeSettingsEndpoint,
                        request, HomeHvacSettingsDto.class);
        assertThat(updateSettingsResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);


        homeHvacSettingsDto.setOptimizerEnabled(false);
        homeHvacSettingsDto.setSetTemperature(26.0);
        request = new HttpEntity<>(homeHvacSettingsDto);
        updateSettingsResponse  =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + updateHomeSettingsEndpoint,
                        request, HomeHvacSettingsDto.class);
        assertThat(updateSettingsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


    private void configureMockServices(){
        HomeAssistantEntityDto homeAssistantEntityDto = new HomeAssistantEntityDto();
        HomeAssistantAttributesDto homeAssistantAttributesDto = new HomeAssistantAttributesDto();
        homeAssistantAttributesDto.setUnitOfMeasurement("kW");
        homeAssistantEntityDto.setEntityId("sensor.hvac");
        homeAssistantEntityDto.setState("1");
        homeAssistantEntityDto.setAttributes(homeAssistantAttributesDto);
        HomeAssistantEntityDto climateHomeAssistantEntityDto = new HomeAssistantEntityDto();
        climateHomeAssistantEntityDto.setState("off");

        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("sensor.hvac")).thenReturn(homeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area1")).thenReturn(climateHomeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area2")).thenReturn(climateHomeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area3")).thenReturn(climateHomeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area4")).thenReturn(climateHomeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area5")).thenReturn(climateHomeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area6")).thenReturn(climateHomeAssistantEntityDto);
    }
}
