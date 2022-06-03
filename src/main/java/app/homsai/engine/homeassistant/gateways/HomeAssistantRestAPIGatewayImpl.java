package app.homsai.engine.homeassistant.gateways;

import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static app.homsai.engine.homeassistant.gateways.HomeAssistantAPIEndpoints.GET_ENTITIES_LIST;
import static app.homsai.engine.homeassistant.gateways.HomeAssistantAPIEndpoints.GET_ENTITY_STATE;

@Service
public class HomeAssistantRestAPIGatewayImpl implements HomeAssistantRestAPIGateway {

    @Value("${home_assistant.api_url}")
    private String apiUrl;
    @Value("${home_assistant.token}")
    private String token;


    @Override
    public List<HomeAssistantEntityDto> getAllHomeAssistantEntities(){
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl+GET_ENTITIES_LIST)
                .encode()
                .toUriString();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+token);
        ResponseEntity<HomeAssistantEntityDto[]> homeAssistantResponse =
                restTemplate
                        .exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers), HomeAssistantEntityDto[].class);
        return Arrays.asList(homeAssistantResponse.getBody());
    }

    @Override
    public HomeAssistantEntityDto syncHomeAssistantEntityValue(String entityId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl+GET_ENTITY_STATE.replace("{entity_id}", entityId))
                .encode()
                .toUriString();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+token);
        ResponseEntity<HomeAssistantEntityDto> homeAssistantResponse =
                restTemplate
                        .exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers), HomeAssistantEntityDto.class);
        return homeAssistantResponse.getBody();
    }


}
