package app.homsai.engine.optimizations.gateways;

import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.optimizations.gateways.dtos.HvacDevicesOptimizationPVResponseDto;
import app.homsai.engine.optimizations.gateways.dtos.HvacOptimizationPVRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static app.homsai.engine.optimizations.gateways.AIServiceAPIEndpoints.POST_HVAC_DEVICES_TURN_ON_OFF;

@Service
public class HomsaiAIServiceGatewayImpl implements HomsaiAIServiceGateway {

    @Value("${ai_service.api_url}")
    private String apiUrl;

    @Autowired
    private EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Override
    public HvacDevicesOptimizationPVResponseDto getHvacDevicesOptimizationPV(HvacOptimizationPVRequestDto hvacOptimizationPVRequestDto){
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl+POST_HVAC_DEVICES_TURN_ON_OFF)
                .encode()
                .toUriString();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+homeInfo.getAiserviceToken());
        ResponseEntity<HvacDevicesOptimizationPVResponseDto> response =
                restTemplate
                        .exchange(url, HttpMethod.POST,  new HttpEntity<>(hvacOptimizationPVRequestDto, headers), HvacDevicesOptimizationPVResponseDto.class);
        return response.getBody();
    }


}
