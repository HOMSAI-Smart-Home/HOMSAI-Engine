package app.homsai.engine.users;

import app.homsai.engine.entities.application.services.EntitiesScheduledApplicationService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantRestAPIGateway;
import app.homsai.engine.homeassistant.gateways.HomeAssistantWSAPIGateway;
import app.homsai.engine.pvoptimizer.application.http.dtos.HVACDeviceDto;
import app.homsai.engine.pvoptimizer.application.http.dtos.HVACEquipmentDto;
import app.homsai.engine.pvoptimizer.application.services.PVOptimizerScheduledApplicationService;
import app.homsai.engine.pvoptimizer.gateways.HomsaiAIServiceGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(classes = {EntitiesScheduledApplicationService.class, HomeAssistantRestAPIGateway.class, HomeAssistantWSAPIGateway.class, HomsaiAIServiceGateway.class, PVOptimizerScheduledApplicationService.class})
public class HvacDevicesTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Environment env;

    private final String readHVACDevicesDBEndpoint = "/entities/homsai/db/hvac";
    private final String readHVACDevicesEndpoint = "/entities/homsai/hvac";

    @Test
    public void whenGetHVACDevices_thenReadRightOnes() {

        ResponseEntity<HVACDeviceDto[]> hvacDevicesDB = restTemplate.getForEntity(env.getProperty("server.contextPath") + readHVACDevicesDBEndpoint, HVACDeviceDto[].class);
        assertThat(hvacDevicesDB.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(hvacDevicesDB.getBody())).hasSize(9);

        ResponseEntity<HVACDeviceDto[]> hvacDevices = restTemplate.getForEntity(env.getProperty("server.contextPath") + readHVACDevicesEndpoint, HVACDeviceDto[].class);
        assertThat(hvacDevices.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(hvacDevices.getBody())).hasSize(3);
        assertThat(Objects.requireNonNull(hvacDevices.getBody()[0].getType())).isEqualTo(0);

    }
}
