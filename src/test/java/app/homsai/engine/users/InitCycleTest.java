package app.homsai.engine.users;

import app.homsai.engine.common.domain.utils.constants.Consts;
import app.homsai.engine.common.domain.utils.constants.ConstsUtils;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantWSAPIGateway;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantAttributesDto;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HVACDeviceDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HomeHvacSettingsDto;
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

import static app.homsai.engine.common.domain.utils.constants.Consts.HOME_ASSISTANT_WATT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(classes = {EntitiesScheduledApplicationService.class, HomeAssistantRestAPIGateway.class, HomeAssistantWSAPIGateway.class, PVOptimizerHomsaiAIServiceGateway.class, PVOptimizerScheduledApplicationService.class, ConstsUtils.class})
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

    @Autowired
    ConstsUtils constsUtils;

    private final String startInitEndpoint = "/entities/homsai/hvac/init";
    private final String getStatusEndpoint = "/entities/homsai/hvac/init/status";
    private final String getInitEstimatedTime = "/entities/homsai/hvac/init/estimated";
    private final String readHVACDevicesEndpoint = "/entities/homsai/hvac";
    private final String readHomeSettingsEndpoint = "/entities/homsai/home/settings";


    private List<HomeAssistantEntityDto> entityDtos;


/*
    @Test
    public void whenStartHVACInit_thenReadRightStatus() throws InterruptedException {

        configureMockServices(9, true);
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
        configureMockServices(9, true);
        String urlHvacInitEstimatedTimeSummer = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path(env.getProperty("server.contextPath")+getInitEstimatedTime)
                .queryParam("type", Consts.PV_OPTIMIZATION_MODE_SUMMER)
                .encode()
                .toUriString();

        ResponseEntity<HvacOptimizerDeviceInitializationEstimatedDto> initEstimatedTime = restTemplate.getForEntity(urlHvacInitEstimatedTimeSummer, HvacOptimizerDeviceInitializationEstimatedDto.class);
        assertThat(initEstimatedTime.getBody().getTotalTimeSeconds()).isEqualTo(1110);
        String urlHvacInitEstimatedTimeWinter = UriComponentsBuilder.fromHttpUrl("http://localhost:" + this.port)
                .path(env.getProperty("server.contextPath")+getInitEstimatedTime)
                .queryParam("type", Consts.PV_OPTIMIZATION_MODE_WINTER)
                .encode()
                .toUriString();

        ResponseEntity<HvacOptimizerDeviceInitializationEstimatedDto> initEstimatedTimeWinter = restTemplate.getForEntity(urlHvacInitEstimatedTimeWinter, HvacOptimizerDeviceInitializationEstimatedDto.class);
        assertThat(initEstimatedTimeWinter.getBody().getTotalTimeSeconds()).isEqualTo(960);
    }

*/


    @Test
    public void whenStartHVACInit_thenReadRightTwoDevicesValues() throws InterruptedException {
        callInitAPI(3);
    }

    @Test
    public void whenStartHVACInit_thenReadRightSingleDeviceValues() throws InterruptedException {
        callInitAPI(1);
    }

    @Test
    public void whenStartHVACInit_thenReadRightDevicesValues() throws InterruptedException {
        callInitAPI(6);
    }

    private void callInitAPI(int entitiesNumber) throws InterruptedException {
        configureMockServices(entitiesNumber, true);

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

        ResponseEntity<HVACDeviceDto[]> hvacDevices = restTemplate.getForEntity(env.getProperty("server.contextPath") + readHVACDevicesEndpoint, HVACDeviceDto[].class);
        assertThat(hvacDevices.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(hvacDevices.getBody())).hasSize(entitiesNumber);

        List<HVACDeviceDto> hvacDeviceDtos = Arrays.asList(hvacDevices.getBody());
        hvacDeviceDtos.sort(Comparator.comparing(HVACDeviceDto::getEntityId));

        if (hvacDeviceDtos.size() == 1) {
            assertThat(hvacDeviceDtos.get(0).getCoupledPowerConsumption()).isEqualTo(
                    hvacDeviceDtos.get(0).getPowerConsumption()
            );
        }
        if(hvacDeviceDtos.size() == 3){
            assertThat(hvacDeviceDtos.get(0).getPowerConsumption()).isEqualTo(511.08);
            assertThat(hvacDeviceDtos.get(0).getCoupledPowerConsumption()).isEqualTo(Consts.HVAC_INITIALIZATION_MIN_CONSUMPTION);
            assertThat(hvacDeviceDtos.get(1).getPowerConsumption()).isEqualTo(956.66);
            assertThat(hvacDeviceDtos.get(1).getCoupledPowerConsumption()).isEqualTo(321.80);
            assertThat(hvacDeviceDtos.get(2).getPowerConsumption()).isEqualTo(1412.04);
            assertThat(hvacDeviceDtos.get(2).getCoupledPowerConsumption()).isEqualTo(595.20);
        }if(hvacDeviceDtos.size() == 6) {
            assertThat(hvacDeviceDtos.get(1).getPowerConsumption()).isEqualTo(956.66);
            assertThat(hvacDeviceDtos.get(1).getCoupledPowerConsumption()).isEqualTo(321.80);
            assertThat(hvacDeviceDtos.get(2).getPowerConsumption()).isEqualTo(1412.04);
            assertThat(hvacDeviceDtos.get(2).getCoupledPowerConsumption()).isEqualTo(595.20);
            assertThat(hvacDeviceDtos.get(3).getPowerConsumption()).isEqualTo(300);
            assertThat(hvacDeviceDtos.get(3).getCoupledPowerConsumption()).isEqualTo(Consts.HVAC_INITIALIZATION_MIN_CONSUMPTION);
            assertThat(hvacDeviceDtos.get(0).getPowerConsumption()).isEqualTo(hvacDeviceDtos.get(0).getCoupledPowerConsumption());
            assertThat(hvacDeviceDtos.get(4).getPowerConsumption()).isEqualTo(hvacDeviceDtos.get(4).getCoupledPowerConsumption());
            assertThat(hvacDeviceDtos.get(5).getPowerConsumption()).isEqualTo(hvacDeviceDtos.get(5).getCoupledPowerConsumption());
        }

        ResponseEntity<HomeHvacSettingsDto> newHvacSettings = restTemplate.getForEntity(env.getProperty("server.contextPath") + readHomeSettingsEndpoint, HomeHvacSettingsDto.class);
        assertThat(newHvacSettings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(newHvacSettings.getBody()).getOptimizerEnabled()).isEqualTo(true);
    }

    private void configureCoupledInitMockServices(int entitiesNumber) {
        // Invocazioni di homeAssistantRestAPIGateway.syncHomeAssistantEntityValue
        // N volte per il calcolo della baseConsumption (calcInitBaseConsumptionCycles())
        // X volte per calcolare il consumo (calcInitHvacDeviceCycles())
        // di cui X/2 volte con il device i-esimo ON
        // e le restanti con il device i-esimo e il successivo ON

        final int[] baseConsumptionTimes = {0};
        final int[] deviceIndex = {0};
        final int[] initHvacDeviceCycles = {0};

        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue(contains("sensor.hvac"))).then(
                (Answer<HomeAssistantEntityDto>) i -> {
                    HomeAssistantEntityDto homeAssistantConsumption =  new HomeAssistantEntityDto();
                    HomeAssistantAttributesDto homeAssistantAttributesDto = new HomeAssistantAttributesDto();
                    homeAssistantAttributesDto.setUnitOfMeasurement(HOME_ASSISTANT_WATT);
                    homeAssistantAttributesDto.setTemperature("24.0");
                    homeAssistantConsumption.setAttributes(homeAssistantAttributesDto);

                    if (baseConsumptionTimes[0] < constsUtils.calcInitBaseConsumptionCycles()) {
                        // Returned in case of readBaseConsumption()
                        homeAssistantConsumption.setState("52.20");
                        baseConsumptionTimes[0]++;
                    } else {
                        if (initHvacDeviceCycles[0] < constsUtils.calcHvacDeviceCyclesForNextInit()) {
                            homeAssistantConsumption.setState(String.valueOf(getConsumptionForDevice(entitiesNumber, deviceIndex[0])));
                            initHvacDeviceCycles[0]++;
                        } else {
                            Double consumption;
                            if(entitiesNumber == 1)
                                consumption = getConsumptionForDevice(entitiesNumber, deviceIndex[0]);
                            else {
                                consumption = getConsumptionForDevice(entitiesNumber, deviceIndex[0]+1);
                            }
                            homeAssistantConsumption.setState(String.valueOf(consumption));
                            if (initHvacDeviceCycles[0] == constsUtils.calcInitHvacDeviceCycles() - 1) {
                                // In case we're calling getSetTemp for the next device
                                deviceIndex[0]+=2;
                                initHvacDeviceCycles[0] = 0;
                            } else {
                                initHvacDeviceCycles[0]++;
                            }
                        }
                    }

                    return homeAssistantConsumption;
                }
        );

        when(homeAssistantRestAPIGateway.syncHomeAssistantEntityValue(contains("climate."))).then(
                (Answer<HomeAssistantEntityDto>) i -> {
                    HomeAssistantEntityDto homeAssistantConsumption = new HomeAssistantEntityDto();
                    HomeAssistantAttributesDto homeAssistantAttributesDto = new HomeAssistantAttributesDto();
                    homeAssistantAttributesDto.setUnitOfMeasurement(HOME_ASSISTANT_WATT);
                    homeAssistantAttributesDto.setTemperature("24.0");
                    homeAssistantConsumption.setAttributes(homeAssistantAttributesDto);
                    return homeAssistantConsumption;
                }
        );
    }

    private void configureMockConsts(boolean mockTiming) {
        when(constsUtils.calcInitHvacDeviceCycles()).thenReturn(4);
        when(constsUtils.calcHvacDeviceCyclesForNextInit()).thenReturn(2);
        when(constsUtils.calcInitBaseConsumptionCycles()).thenReturn(1);
        if (mockTiming) {
            when(constsUtils.getHvacInitializationSleepTimeMillis()).thenReturn(0);
            when(constsUtils.getHvacInitializationDurationMinutes()).thenReturn(1);
            when(constsUtils.getSyncHomeAssistantEntitiesLockTimeout()).thenReturn(1000);
        } else {
            when(constsUtils.getHvacInitializationSleepTimeMillis()).thenReturn(30*1000);
            when(constsUtils.getHvacInitializationDurationMinutes()).thenReturn(20);
            when(constsUtils.getSyncHomeAssistantEntitiesLockTimeout()).thenReturn(30000);
        }
    }

    private double getConsumptionForDevice(int entitiesNumber, int index) {
        double consumption = 0.00;
        switch (index) {
            case 0:
                consumption = 563.28;
                break;
            case 1:
                consumption = 885.08;
                break;
            case 2:
                consumption = 1008.86;
                break;
            case 3:
                consumption = 1604.06;
                break;
            case 4:
                consumption = 1464.24;
                break;
            case 5:
                consumption = 1470.18;
                break;
            case 6:
                consumption = 352.20;
                break;
            case 7:
                consumption = 752.20;
                break;
            case 8:
                consumption = 452.20;
                break;
            case 9:
                consumption = 952.20;
                break;
            case 10:
                consumption = 552.20;
                break;
            case 11:
                consumption = 1063.28;
                break;
            default:
                consumption = 0.00;
                break;
        }

        return consumption;
    }

    private void configureMockServices(int entitiesNumber, boolean mockTiming){
        configureCoupledInitMockServices(entitiesNumber);
        configureMockConsts(mockTiming);

        when(homeAssistantRestAPIGateway.getAllHomeAssistantEntities())
                .thenReturn(getMockHomeAssistanEntitiesDto(entitiesNumber));
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
        /*when(homeAssistantRestAPIGateway.sendHomeAssistantClimateHVACMode(anyString(), anyString())).then(
                (Answer<HomeAssistantEntityDto>) i -> {
                    String entityId = (String) i.getArguments()[0];
                    String hvacFunction = (String) i.getArguments()[1];
                    for(HomeAssistantEntityDto homeAssistantEntityDto : entityDtos){
                        if(homeAssistantEntityDto.getEntityId().equals(entityId)) {
                            homeAssistantEntityDto.setState(hvacFunction);
                            return homeAssistantEntityDto;
                        }
                    }
                    return null;
                }
        );
        */
        when(homeAssistantRestAPIGateway.sendHomeAssistantClimateHVACMode(anyString(), anyString())).then(
                (Answer<HomeAssistantEntityDto>) i -> {
                    String entityId = (String) i.getArguments()[0];
                    String hvacFunction = (String) i.getArguments()[1];
                    for(HomeAssistantEntityDto homeAssistantEntityDto : entityDtos){
                        if(homeAssistantEntityDto.getEntityId().equals(entityId)) {
                            homeAssistantEntityDto.setState("cool");
                            if(hvacFunction.equals("off"))
                                homeAssistantEntityDto.getAttributes().setHvacAction("idle");
                            else
                                homeAssistantEntityDto.getAttributes().setHvacAction(hvacFunction);
                            return homeAssistantEntityDto;
                        }
                    }
                    return null;
                }
        );
    }

    private List<HomeAssistantEntityDto> getMockHomeAssistanEntitiesDto(int entitiesNumber) {
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
        homeAssistantClimateWinter2.setEntityId("climate.clima2");
        homeAssistantClimateSummer1.setEntityId("climate.clima3");
        homeAssistantClimateSummer2.setEntityId("climate.clima4");
        homeAssistantClimateSummer3.setEntityId("climate.clima5");
        homeAssistantClimateWinterSummer1.setEntityId("climate.clima6");
        homeAssistantClimateWinterSummer2.setEntityId("climate.clima7");
        homeAssistantClimateWinterSummer3.setEntityId("climate.clima8");
        homeAssistantClimateWinterSummer4.setEntityId("climate.clima9");

        entityDtos = Arrays.asList(
                homeAssistantClimateWinter1,
                homeAssistantClimateWinter2,
                homeAssistantClimateSummer1,
                homeAssistantClimateSummer2,
                homeAssistantClimateSummer3,
                homeAssistantClimateWinterSummer1,
                homeAssistantClimateWinterSummer2,
                homeAssistantClimateWinterSummer3,
                homeAssistantClimateWinterSummer4
        );

        switch (entitiesNumber) {
            case 1:
                entityDtos = Collections.singletonList(homeAssistantClimateWinter1);
                break;
            case 3:
                entityDtos = Arrays.asList(
                        homeAssistantClimateWinter1,
                        homeAssistantClimateWinter2,
                        homeAssistantClimateWinterSummer1
                );
                break;
            case 6:
                entityDtos = Arrays.asList(
                        homeAssistantClimateWinter1,
                        homeAssistantClimateWinter2,
                        homeAssistantClimateWinterSummer1,
                        homeAssistantClimateWinterSummer2,
                        homeAssistantClimateWinterSummer3,
                        homeAssistantClimateWinterSummer4
                );
            default:
                break;
        }

        return entityDtos;
    }
}
