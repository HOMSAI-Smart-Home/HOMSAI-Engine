package app.homsai.engine.users;

import app.homsai.engine.pvoptimizer.application.http.dtos.HVACEquipmentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HvacEquipmentsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Environment env;

    private final String readEquipmentsEndpoint = "/entities/homsai/equipment";

    @Test
    public void whenGetHVACEquipments_thenReadRightOnes() {
        // Check right init values
        ResponseEntity<HVACEquipmentDto[]> hvacEquipments = restTemplate.getForEntity(env.getProperty("server.contextPath") + readEquipmentsEndpoint, HVACEquipmentDto[].class);
        assertThat(hvacEquipments.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(hvacEquipments.getBody())).hasSize(4);
    }
}
