package app.homsai.engine.common.gateways;

import app.homsai.engine.common.gateways.dtos.LoginRequestDto;
import app.homsai.engine.common.gateways.dtos.LoginResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static app.homsai.engine.optimizations.gateways.AIServiceAPIEndpoints.LOGIN;

@Service
public class AIServiceAuthenticationGatewayImpl implements AIServiceAuthenticationGateway{

    @Value("${ai_service.api_url}")
    private String apiUrl;

    @Override
    public LoginResponseDto login(String username, String password) throws AuthenticationException{
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl+LOGIN)
                .encode()
                .toUriString();
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail(username);
        loginRequestDto.setPassword(password);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-Requested-With", "XMLHttpRequest");
        try {
            ResponseEntity<LoginResponseDto> aiServiceResponse =
                    restTemplate
                            .exchange(url, HttpMethod.POST, new HttpEntity<>(loginRequestDto, headers), LoginResponseDto.class);

            if(aiServiceResponse.getStatusCode() != HttpStatus.OK)
                throw new BadCredentialsException("Authentication failed");
            return aiServiceResponse.getBody();
        }catch (Exception e){
            throw new BadCredentialsException("Authentication failed");
        }
    }

}
