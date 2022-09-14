package app.homsai.engine.users;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.application.services.EntitiesScheduledApplicationService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantRestAPIGateway;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantAttributesDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HomeHvacSettingsDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HomeHvacSettingsUpdateDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.OptimizerHVACDeviceDto;
import app.homsai.engine.pvoptimizer.application.services.PVOptimizerScheduledApplicationService;
import app.homsai.engine.pvoptimizer.domain.services.PVOptimizerEngineService;
import app.homsai.engine.pvoptimizer.domain.services.cache.PVOptimizerCacheService;
import app.homsai.engine.pvoptimizer.gateways.HomsaiAIServiceGateway;
import app.homsai.engine.pvoptimizer.gateways.dtos.HvacDevicesOptimizationPVResponseDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(classes = {EntitiesScheduledApplicationService.class, HomeAssistantRestAPIGateway.class, HomsaiAIServiceGateway.class, PVOptimizerScheduledApplicationService.class})
public class HvacOptimizerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Environment env;

    private final String readSettingsEndpoint = "/entities/homsai/hvac";
    private final String readHomeSettingsEndpoint = "/entities/homsai/home/settings";
    private final String updateHomeSettingsEndpoint = "/entities/homsai/home/settings";

    @Autowired
    HomeAssistantRestAPIGateway homeAssistantRestAPIGateway;

    @Autowired
    HomsaiAIServiceGateway homsaiAIServiceGateway;

    @Autowired
    PVOptimizerEngineService pvOptimizerEngineService;


    @Test
    public void whenAIServiceTurnsOnDevices_thenReadRightStates() {
        List<String> devicesToTurnOn = new ArrayList<>();
        devicesToTurnOn.add("climate.area1");
        devicesToTurnOn.add("climate.area3");
        devicesToTurnOn.add("climate.area4");
        configureMockHomeAssistantGateway();
        configureMockAIServiceGateway(devicesToTurnOn);
        pvOptimizerEngineService.updateHvacDeviceOptimizationCache();
        enableOptimizer();
        pvOptimizerEngineService.updateHvacDeviceOptimizationStatus();

        ResponseEntity<OptimizerHVACDeviceDto[]> optimizedDevicesResponse  =
                restTemplate.getForEntity(env.getProperty("server.contextPath") + readSettingsEndpoint,
                        OptimizerHVACDeviceDto[].class);
        assertThat(optimizedDevicesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        OptimizerHVACDeviceDto[] optimizerHVACDeviceDtos = optimizedDevicesResponse.getBody();
        for(OptimizerHVACDeviceDto optimizerHVACDeviceDto : optimizerHVACDeviceDtos){
            if(devicesToTurnOn.contains(optimizerHVACDeviceDto.getEntityId()))
                assertThat(optimizerHVACDeviceDto.getHvacMode()).isEqualTo(Consts.HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION);
            else
                assertThat(optimizerHVACDeviceDto.getHvacMode()).isEqualTo(Consts.HOME_ASSISTANT_HVAC_DEVICE_OFF_FUNCTION);
        }

        disableOptimizer();

    }

    @Test
    public void whenSwitchOptimizerMode_thenReadRightStates() {
        final String winterEquipmentUuid = "1142fa99-9b4d-4ce5-ab1f-5694664fd881";
        final String summerEquipmentUuid = "fa3a5e07-df74-4c45-83cd-693d002548a7";
        HomeHvacSettingsUpdateDto homeHvacSettingsUpdateDto = new HomeHvacSettingsUpdateDto();
        homeHvacSettingsUpdateDto.setOptimizerEnabled(true);
        homeHvacSettingsUpdateDto.setSetTemperature(30.0);
        homeHvacSettingsUpdateDto.setOptimizerMode(Consts.HVAC_MODE_SUMMER_ID);
        homeHvacSettingsUpdateDto.setCurrentWinterHVACEquipmentUuid(winterEquipmentUuid);
        homeHvacSettingsUpdateDto.setCurrentSummerHVACEquipmentUuid(summerEquipmentUuid);
        HttpEntity<HomeHvacSettingsUpdateDto> request = new HttpEntity<>(homeHvacSettingsUpdateDto);
        ResponseEntity<HomeHvacSettingsDto> updateSettingsResponse  =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + updateHomeSettingsEndpoint,
                        request, HomeHvacSettingsDto.class);
        assertThat(updateSettingsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(updateSettingsResponse.getBody()).getCurrentWinterHVACEquipment().getUuid()).isEqualTo(winterEquipmentUuid);
        assertThat(Objects.requireNonNull(updateSettingsResponse.getBody()).getCurrentSummerHVACEquipment().getUuid()).isEqualTo(summerEquipmentUuid);

        ResponseEntity<HomeHvacSettingsDto> newHvacSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readHomeSettingsEndpoint, HomeHvacSettingsDto.class);
        assertThat(newHvacSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(newHvacSettings.getBody()).getOptimizerMode()).isEqualTo(Consts.HVAC_MODE_SUMMER_ID);
        assertThat(Objects.requireNonNull(newHvacSettings.getBody()).getCurrentWinterHVACEquipment().getUuid()).isEqualTo(winterEquipmentUuid);
        assertThat(Objects.requireNonNull(newHvacSettings.getBody()).getCurrentSummerHVACEquipment().getUuid()).isEqualTo(summerEquipmentUuid);
    }


    private void enableOptimizer() {
        HomeHvacSettingsUpdateDto homeHvacSettingsUpdateDto = new HomeHvacSettingsUpdateDto();
        homeHvacSettingsUpdateDto.setOptimizerEnabled(true);
        homeHvacSettingsUpdateDto.setSetTemperature(30.0);
        HttpEntity<HomeHvacSettingsUpdateDto> request = new HttpEntity<>(homeHvacSettingsUpdateDto);
        ResponseEntity<HomeHvacSettingsDto> updateSettingsResponse  =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + updateHomeSettingsEndpoint,
                        request, HomeHvacSettingsDto.class);
        assertThat(updateSettingsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<HomeHvacSettingsDto> newHvacSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readHomeSettingsEndpoint, HomeHvacSettingsDto.class);
        assertThat(newHvacSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(newHvacSettings.getBody()).getSetTemperature()).isEqualTo(30);
        assertThat(Objects.requireNonNull(newHvacSettings.getBody()).getOptimizerEnabled()).isEqualTo(true);
    }


    private void disableOptimizer(){
        HomeHvacSettingsUpdateDto homeHvacSettingsUpdateDto = new HomeHvacSettingsUpdateDto();
        homeHvacSettingsUpdateDto.setOptimizerEnabled(false);
        homeHvacSettingsUpdateDto.setSetTemperature(26.0);
        HttpEntity<HomeHvacSettingsUpdateDto> request = new HttpEntity<>(homeHvacSettingsUpdateDto);
        ResponseEntity<HomeHvacSettingsDto> updateSettingsResponse  =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + updateHomeSettingsEndpoint,
                        request, HomeHvacSettingsDto.class);
        assertThat(updateSettingsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void configureMockHomeAssistantGateway(){
        HomeAssistantEntityDto hvacSensor = new HomeAssistantEntityDto();
        HomeAssistantAttributesDto homeAssistantAttributesDto1 = new HomeAssistantAttributesDto();
        homeAssistantAttributesDto1.setUnitOfMeasurement("kW");
        hvacSensor.setEntityId("sensor.hvac");
        hvacSensor.setState("1");
        hvacSensor.setAttributes(homeAssistantAttributesDto1);

        HomeAssistantEntityDto generalSensor = new HomeAssistantEntityDto();
        HomeAssistantAttributesDto homeAssistantAttributesDto4 = new HomeAssistantAttributesDto();
        homeAssistantAttributesDto4.setUnitOfMeasurement("kW");
        generalSensor.setEntityId("sensor.general");
        generalSensor.setState("1");
        generalSensor.setAttributes(homeAssistantAttributesDto4);

        HomeAssistantEntityDto productionSensor = new HomeAssistantEntityDto();
        HomeAssistantAttributesDto homeAssistantAttributesDto2 = new HomeAssistantAttributesDto();
        homeAssistantAttributesDto2.setUnitOfMeasurement("kW");
        productionSensor.setEntityId("sensor.production");
        productionSensor.setState("3");
        productionSensor.setAttributes(homeAssistantAttributesDto2);

        HomeAssistantEntityDto storageSensor = new HomeAssistantEntityDto();
        HomeAssistantAttributesDto homeAssistantAttributesDto3 = new HomeAssistantAttributesDto();
        homeAssistantAttributesDto3.setUnitOfMeasurement("kW");
        storageSensor.setEntityId("sensor.storage");
        storageSensor.setState("0");
        storageSensor.setAttributes(homeAssistantAttributesDto3);

        HomeAssistantEntityDto climateHomeAssistantEntityDto = new HomeAssistantEntityDto();
        climateHomeAssistantEntityDto.setState("off");

        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("sensor.hvac")).thenReturn(hvacSensor);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("sensor.general")).thenReturn(generalSensor);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("sensor.production")).thenReturn(productionSensor);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("sensor.storage")).thenReturn(storageSensor);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area1")).thenReturn(climateHomeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area2")).thenReturn(climateHomeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area3")).thenReturn(climateHomeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area4")).thenReturn(climateHomeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area5")).thenReturn(climateHomeAssistantEntityDto);
        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue("climate.area6")).thenReturn(climateHomeAssistantEntityDto);
    }

    private void configureMockAIServiceGateway(List<String> devicesToTurnOn){
        HvacDevicesOptimizationPVResponseDto hvacDevicesOptimizationPVResponseDto = new HvacDevicesOptimizationPVResponseDto();
        hvacDevicesOptimizationPVResponseDto.setDevicesToTurnOn(devicesToTurnOn);

        when(homsaiAIServiceGateway.getHvacDevicesOptimizationPV(any())).thenReturn(hvacDevicesOptimizationPVResponseDto);
    }
}
