package app.homsai.engine.forecast.gateways;

import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.forecast.gateways.dtos.SelfConsumptionOptimizationsForecastQueriesDto;
import app.homsai.engine.forecast.gateways.dtos.SelfConsumptionOptimizationsForecastRequestDto;
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

import static app.homsai.engine.forecast.gateways.AIServiceAPIEndpoints.POST_PV_SELF_CONSUMPTION_OPTIMIZATION_FORECAST;


@Service
public class ForecastHomsaiAIServiceGatewayImpl implements ForecastHomsaiAIServiceGateway {

    @Value("${ai_service.api_url}")
    private String apiUrl;

    @Autowired
    private EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Override
    public SelfConsumptionOptimizationsForecastQueriesDto getPhotovoltaicSelfConsumptionOptimizerForecast(SelfConsumptionOptimizationsForecastRequestDto selfConsumptionOptimizationsForecastRequestDto, String currency){
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl+POST_PV_SELF_CONSUMPTION_OPTIMIZATION_FORECAST)
                .queryParam("unit", currency)
                .encode()
                .toUriString();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+homeInfo.getAiserviceToken());
        ResponseEntity<SelfConsumptionOptimizationsForecastQueriesDto> response =
                restTemplate
                        .exchange(url, HttpMethod.POST,  new HttpEntity<>(selfConsumptionOptimizationsForecastRequestDto, headers), SelfConsumptionOptimizationsForecastQueriesDto.class);
        return response.getBody();
    }


}
