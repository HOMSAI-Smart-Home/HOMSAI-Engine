package app.homsai.engine.users;

import app.homsai.engine.common.application.http.dtos.LoggedDto;
import app.homsai.engine.common.application.http.dtos.TokenDto;
import app.homsai.engine.entities.application.services.EntitiesScheduledApplicationService;
import app.homsai.engine.homeassistant.gateways.HomeAssistantRestAPIGateway;
import app.homsai.engine.optimizations.application.services.OptimizationsScheduledApplicationService;
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

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(classes = {EntitiesScheduledApplicationService.class, OptimizationsScheduledApplicationService.class, HomeAssistantRestAPIGateway.class})
public class TokenCRUDTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static String token;

    @Autowired
    private Environment env;

    private final String injectTokenEndpoint = "/auth/token/inject";
    private final String removeTokenEndpoint = "/auth/token/remove";
    private final String isLoggedEndpoint = "/auth/islogged";



    @Test
    public void whenInjectLogin_thenIsLogged() {

        // IsLogged must be false
        ResponseEntity<LoggedDto> isLogged = restTemplate.getForEntity(env.getProperty("server.contextPath") + isLoggedEndpoint, LoggedDto.class);
        assertThat(isLogged.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(isLogged.getBody()).getLogged()).isFalse();

        // Injecting token
        String token = "tokenTemplate";
        String refreshToken = "refreshTokenTemplate";
        String email = "emailTemplate";
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(token);
        tokenDto.setRefreshToken(refreshToken);
        tokenDto.setEmail(email);

        HttpEntity<TokenDto> request = new HttpEntity<>(tokenDto);
        ResponseEntity<String> loginResponse =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + injectTokenEndpoint,
                        request, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Now isLogged must be true
        isLogged = restTemplate.getForEntity(env.getProperty("server.contextPath") + isLoggedEndpoint, LoggedDto.class);
        assertThat(isLogged.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(isLogged.getBody()).getLogged()).isTrue();

        // Removing token
        ResponseEntity<String> logoutResponse =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + removeTokenEndpoint,
                        request, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Now isLogged must be false
        isLogged = restTemplate.getForEntity(env.getProperty("server.contextPath") + isLoggedEndpoint, LoggedDto.class);
        assertThat(isLogged.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(isLogged.getBody()).getLogged()).isFalse();
    }
}
