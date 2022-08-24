package app.homsai.engine.users;

import app.homsai.engine.pvoptimizer.application.http.dtos.HvacOptimizerDeviceInitializationCacheDto;
import app.homsai.engine.entities.application.services.EntitiesScheduledApplicationService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantRestAPIGateway;
import app.homsai.engine.pvoptimizer.application.services.PVOptimizerScheduledApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.ArrayList;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(classes = {EntitiesScheduledApplicationService.class, PVOptimizerScheduledApplicationService.class, HomeAssistantRestAPIGateway.class})
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

    private final String startInitEndpoint = "/entities/homsai/hvac/init";
    private final String getStatusEndpoint = "/entities/homsai/hvac/init/status";


    @Test
    public void whenStartHVACInit_thenReadRightStatus() {

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
                .queryParam("type", "1")
                .encode()
                .toUriString();
        ResponseEntity<String> startHvacInit   =
                restTemplate.postForEntity(urlStartHvacInit,
                        null, String.class);
        assertThat(startHvacInit.getStatusCode()).isEqualTo(HttpStatus.OK);

        // check read settings values
        ResponseEntity<HvacOptimizerDeviceInitializationCacheDto> postInitStatus = restTemplate.getForEntity(env.getProperty("server.contextPath") + getStatusEndpoint, HvacOptimizerDeviceInitializationCacheDto.class);
        assertThat(postInitStatus.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getInProgress()).isTrue();
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getLog()).isNotEqualTo("");
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getElapsedTimeSeconds()).isNotEqualTo(0);
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getTotalTimeSeconds()).isNotEqualTo(0);
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getRemainingTimeSeconds()).isEqualTo(0);
        assertThat(Objects.requireNonNull(postInitStatus.getBody()).getElapsedPercent()).isNotEqualTo(0D);
    }

    private void configureMockServices(){
        when(homeAssistantRestAPIGateway.getAllHomeAssistantEntities()).thenReturn(new ArrayList<>());
    }
}
