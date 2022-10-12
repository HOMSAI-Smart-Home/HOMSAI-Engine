package app.homsai.engine.users;

import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantWSAPIGateway;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantAttributesDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HvacOptimizerDeviceInitializationCacheDto;
import app.homsai.engine.entities.application.services.EntitiesScheduledApplicationService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantRestAPIGateway;
import app.homsai.engine.pvoptimizer.application.http.dtos.HvacOptimizerDeviceInitializationEstimatedDto;
import app.homsai.engine.pvoptimizer.application.services.PVOptimizerScheduledApplicationService;
import app.homsai.engine.pvoptimizer.gateways.PVOptimizerHomsaiAIServiceGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static app.homsai.engine.common.domain.utils.Consts.HOME_ASSISTANT_WATT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(classes = {EntitiesScheduledApplicationService.class, HomeAssistantRestAPIGateway.class, HomeAssistantWSAPIGateway.class, PVOptimizerHomsaiAIServiceGateway.class, PVOptimizerScheduledApplicationService.class})
public class InitCycleTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String token;

    @Autowired
    private Environment env;

    @Autowired
    HomeAssistantRestAPIGateway homeAssistantRestAPIGateway;

    @Autowired
    HomeAssistantWSAPIGateway homeAssistantWSAPIGateway;

    @Autowired
    EntitiesCommandsService entitiesCommandsService;

    private final String startInitEndpoint = "/entities/homsai/hvac/init";
    private final String getStatusEndpoint = "/entities/homsai/hvac/init/status";
    private final String getInitEstimatedTime = "/entities/homsai/hvac/init/estimated";


    @Test
    public void whenStartHVACInit_thenReadRightStatus() throws InterruptedException {

        configureMockServices();
        // Check right init values
        ResponseEntity<HvacOptimizerDeviceInitializationCacheDto> preInitStatus = restTemplate.getForEntity(env.getProperty("server.contextPath") + getStatusEndpoint, HvacOptimizerDeviceInitializationCacheDto.class);
        assertThat(preInitStatus.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(preInitStatus.getBody()).getInProgress()).isFalse();
        assertThat(Objects.requireNonNull(preInitStatus.getBody()).getLog()).isEqualTo("");
        assertThat(Objects.requireNonNull(preInitStatus.getBody()).getElapsedTimeSeconds()).isEqualTo(0);
        assertThat(Objects.requireNonNull(preInitStatus.getBody()).getTotalTimeSeconds()).isEqualTo(0);
        assertThat(Objects.requireNonNull(preInitStatus.getBody()).getRemainingTimeSeconds()).isEqualTo(0);
        assertThat(Objects.requireNonNull(preInitStatus.getBody()).getElapsedPercent()).isEqualTo(0D);

        String urlStartHvacInit = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path(env.getProperty("server.contextPath")+startInitEndpoint)
                .queryParam("type", "0")
                .encode()
                .toUriString();
        ResponseEntity<String> startHvacInit   =
                restTemplate.postForEntity(urlStartHvacInit,
                        null, String.class);
        assertThat(startHvacInit.getStatusCode()).isEqualTo(HttpStatus.OK);

        Thread.sleep(1000);

        // check read settings values
        ResponseEntity<HvacOptimizerDeviceInitializationCacheDto> postInitStatus = restTemplate.getForEntity(env.getProperty("server.contextPath") + getStatusEndpoint, HvacOptimizerDeviceInitializationCacheDto.class);
        assertThat(postInitStatus.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getInProgress()).isTrue();
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getLog()).isNotEqualTo("");
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getElapsedTimeSeconds()).isNotEqualTo(0);
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getTotalTimeSeconds()).isNotEqualTo(0);
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getElapsedPercent()).isNotEqualTo(0D);
    }

    @Test
    public void whenAskForEstimatedTime_thenReadRightTime() {
        configureMockServices();
        String urlHvacInitEstimatedTimeSummer = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path(env.getProperty("server.contextPath")+getInitEstimatedTime)
                .queryParam("type", Consts.PV_OPTIMIZATION_MODE_SUMMER)
                .encode()
                .toUriString();

        ResponseEntity<HvacOptimizerDeviceInitializationEstimatedDto> initEstimatedTime = restTemplate.getForEntity(urlHvacInitEstimatedTimeSummer, HvacOptimizerDeviceInitializationEstimatedDto.class);
        assertThat(initEstimatedTime.getBody().getTotalTimeSeconds()).isEqualTo(10740);
        String urlHvacInitEstimatedTimeWinter = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path(env.getProperty("server.contextPath")+getInitEstimatedTime)
                .queryParam("type", Consts.PV_OPTIMIZATION_MODE_WINTER)
                .encode()
                .toUriString();

        ResponseEntity<HvacOptimizerDeviceInitializationEstimatedDto> initEstimatedTimeWinter = restTemplate.getForEntity(urlHvacInitEstimatedTimeWinter, HvacOptimizerDeviceInitializationEstimatedDto.class);
        assertThat(initEstimatedTimeWinter.getBody().getTotalTimeSeconds()).isEqualTo(9210);
    }

    @Test
    public void whenStartHVACInit_thenReadRightDeviceValues() throws InterruptedException {
        configureMockServices();
        configureCoupledInitMockServices();

        String urlStartHvacInit = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path(env.getProperty("server.contextPath")+startInitEndpoint)
                .queryParam("type", "0")
                .encode()
                .toUriString();
        ResponseEntity<String> startHvacInit   =
                restTemplate.postForEntity(urlStartHvacInit,
                        null, String.class);
        assertThat(startHvacInit.getStatusCode()).isEqualTo(HttpStatus.OK);

        Thread.sleep(120000);
    }

    private void configureCoupledInitMockServices() {
        // Invocazioni di homeAssistantRestAPIGateway.syncHomeAssistantEntityValue
        // N volte per il calcolo della baseConsumption (calcInitBaseConsumptionCycles())
        // 1 volta per calcolare il setTemp del primo device
            // X volte per calcolare il consumo (calcInitHvacDeviceCycles())
            //

        final int[] baseConsumptionTimes = {0};
        final boolean[] calculatedFirstDeviceSetTemp = {false};
        final int[] consumptionCalculatedForDevices = {0};
        final int[] initHvacDeviceCycles = {0};

        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue(any())).then(
                (Answer<HomeAssistantEntityDto>) i -> {
                    HomeAssistantEntityDto homeAssistantConsumption =  new HomeAssistantEntityDto();
                    HomeAssistantAttributesDto homeAssistantAttributesDto = new HomeAssistantAttributesDto();
                    homeAssistantAttributesDto.setUnitOfMeasurement(HOME_ASSISTANT_WATT);
                    homeAssistantAttributesDto.setTemperature("24.0");
                    homeAssistantConsumption.setAttributes(homeAssistantAttributesDto);

                    if (baseConsumptionTimes[0] < calcInitBaseConsumptionCycles()) {
                        // Returned in case of readBaseConsumption()
                        homeAssistantConsumption.setState("0.00");
                        baseConsumptionTimes[0]++;
                    } else {
                        if (!calculatedFirstDeviceSetTemp[0]){
                            homeAssistantConsumption.setState("10.00");
                            calculatedFirstDeviceSetTemp[0] = true;
                        }
                        if (initHvacDeviceCycles[0] <= calcHvacDeviceCyclesForNextInit()) {
                            homeAssistantConsumption.setState(String.valueOf(getConsumptionForDevice(consumptionCalculatedForDevices[0])));
                        } else if (initHvacDeviceCycles[0] == calcHvacDeviceCyclesForNextInit() + 1) {
                            // In case we're calling getSetTemp for the next device
                            homeAssistantConsumption.setState("10.00");
                            initHvacDeviceCycles[0]++;
                        } else {
                            homeAssistantConsumption.setState(
                                    String.valueOf(
                                            getConsumptionForDevice(consumptionCalculatedForDevices[0]) +
                                                    getConsumptionForDevice(consumptionCalculatedForDevices[0] + 1))
                            );
                            if (initHvacDeviceCycles[0] == calcInitHvacDeviceCycles() + 1) {
                                // In case we're calling getSetTemp for the next device
                                consumptionCalculatedForDevices[0]++;
                                initHvacDeviceCycles[0] = 0;
                            }
                        }
                    }

                    return homeAssistantConsumption;
                }
        );
    }

    private int calcInitBaseConsumptionCycles(){
        return 1;// HVAC_BC_INITIALIZATION_DURATION_MINUTES * 60 / (HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000);
    }

    private int calcInitHvacDeviceCycles(){
        return 4;// HVAC_INITIALIZATION_DURATION_MINUTES * 60 / (HVAC_INITIALIZATION_SLEEP_TIME_MILLIS / 1000);
    }

    private int calcHvacDeviceCyclesForNextInit(){
        return calcInitHvacDeviceCycles() / 2;
    }

    private double getConsumptionForDevice(int index) {
        double consumption = 0.00;

        switch (index) {
            case 0:
                consumption = 1090.63;
                break;
            case 1:
                consumption = 755.21;
                break;
            case 2:
                consumption = 1093.92;
                break;
            case 3:
                consumption = 1097.7199999999998;
                break;
            case 4:
                consumption = 1095.625;
                break;
            case 5:
                consumption = 1136.9;
                break;
            default:
                consumption = 0.00;
                break;
        }

        return consumption;
    }

    private void configureMockServices(){
        HomeAssistantAttributesDto attributesCoolingHeating = new HomeAssistantAttributesDto();
        HomeAssistantAttributesDto attributesCooling = new HomeAssistantAttributesDto();
        HomeAssistantAttributesDto attributesHeating = new HomeAssistantAttributesDto();
        attributesCoolingHeating.setHvacModes(Arrays.asList(Consts.HOME_ASSISTANT_HVAC_DEVICE_HEATING_FUNCTION, Consts.HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION));
        attributesCooling.setHvacModes(Collections.singletonList(Consts.HOME_ASSISTANT_HVAC_DEVICE_CONDITIONING_FUNCTION));
        attributesHeating.setHvacModes(Collections.singletonList(Consts.HOME_ASSISTANT_HVAC_DEVICE_HEATING_FUNCTION));
        HomeAssistantEntityDto homeAssistantClimateWinter1 =  new HomeAssistantEntityDto();
        HomeAssistantEntityDto homeAssistantClimateWinter2 =  new HomeAssistantEntityDto();
        HomeAssistantEntityDto homeAssistantClimateSummer1 =  new HomeAssistantEntityDto();
        HomeAssistantEntityDto homeAssistantClimateSummer2 =  new HomeAssistantEntityDto();
        HomeAssistantEntityDto homeAssistantClimateSummer3 =  new HomeAssistantEntityDto();
        HomeAssistantEntityDto homeAssistantClimateWinterSummer1 =  new HomeAssistantEntityDto();
        HomeAssistantEntityDto homeAssistantClimateWinterSummer2 =  new HomeAssistantEntityDto();
        HomeAssistantEntityDto homeAssistantClimateWinterSummer3 =  new HomeAssistantEntityDto();
        HomeAssistantEntityDto homeAssistantClimateWinterSummer4 =  new HomeAssistantEntityDto();
        homeAssistantClimateWinter1.setAttributes(attributesHeating);
        homeAssistantClimateWinter2.setAttributes(attributesHeating);
        homeAssistantClimateSummer1.setAttributes(attributesCooling);
        homeAssistantClimateSummer2.setAttributes(attributesCooling);
        homeAssistantClimateSummer3.setAttributes(attributesCooling);
        homeAssistantClimateWinterSummer1.setAttributes(attributesCoolingHeating);
        homeAssistantClimateWinterSummer2.setAttributes(attributesCoolingHeating);
        homeAssistantClimateWinterSummer3.setAttributes(attributesCoolingHeating);
        homeAssistantClimateWinterSummer4.setAttributes(attributesCoolingHeating);
        homeAssistantClimateWinter1.setEntityId("climate.clima1");
        homeAssistantClimateWinter2.setEntityId("climate.clima1");
        homeAssistantClimateSummer1.setEntityId("climate.clima1");
        homeAssistantClimateSummer2.setEntityId("climate.clima1");
        homeAssistantClimateSummer3.setEntityId("climate.clima1");
        homeAssistantClimateWinterSummer1.setEntityId("climate.clima1");
        homeAssistantClimateWinterSummer2.setEntityId("climate.clima1");
        homeAssistantClimateWinterSummer3.setEntityId("climate.clima1");
        homeAssistantClimateWinterSummer4.setEntityId("climate.clima1");
        when(homeAssistantRestAPIGateway.getAllHomeAssistantEntities()).thenReturn(Arrays.asList(
                homeAssistantClimateWinter1,
                homeAssistantClimateWinter2,
                homeAssistantClimateSummer1,
                homeAssistantClimateSummer2,
                homeAssistantClimateSummer3,
                homeAssistantClimateWinterSummer1,
                homeAssistantClimateWinterSummer2,
                homeAssistantClimateWinterSummer3,
                homeAssistantClimateWinterSummer4
        ));
        when(homeAssistantWSAPIGateway.syncEntityAreas(anyList(), any())).then(
                (Answer<Integer>) i -> {
                    List<HAEntity> entities = (List<HAEntity>) i.getArguments()[0];
                    Object lock = i.getArguments()[1];
                    for(HAEntity haEntity : entities){
                        Area area = entitiesCommandsService.getAreaByNameOrCreate("area1");
                        if (haEntity.getAreas() == null)
                            haEntity.setAreas(new ArrayList<>());
                        haEntity.getAreas().add(area);
                    }
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                    return 0;
                }
        );
    }
}
