package app.homsai.engine.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserCRUDTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static String token;

    @Autowired
    private Environment env;

    @Test
    public void whenLogin_thenReturnToken() {
     /*   MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Content-Type", "application/json");

        LoginRequest loginBody = new LoginRequest("a.anzilotti@hastega.it", "password");
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginBody, headers);

        ResponseEntity<LoginResponse> loginResponse =
                restTemplate.postForEntity(env.getProperty("server.contextPath") + "/auth/login",
                        request, LoginResponse.class);
        token = loginResponse.getBody().getToken();

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(token);*/
    }
}
