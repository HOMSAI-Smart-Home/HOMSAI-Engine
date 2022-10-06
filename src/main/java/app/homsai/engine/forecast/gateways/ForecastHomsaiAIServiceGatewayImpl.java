package app.homsai.engine.forecast.gateways;

import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import app.homsai.engine.forecast.gateways.dtos.*;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantHistoryDto;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static app.homsai.engine.forecast.gateways.AIServiceAPIEndpoints.*;


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

    @Override
    public List<PVForecastQueriesDto> getPhotovoltaicProductionForecast(PVForecastRequestDto pvForecastRequestDto){
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl+GET_PV_PRODUCTION_FORECAST)
                .queryParam("days", pvForecastRequestDto.getDays())
                .queryParam("plant_life_days", pvForecastRequestDto.getPlantLifeDays())
                .queryParam("kwp", pvForecastRequestDto.getkWp())
                .queryParam("lat", pvForecastRequestDto.getLat())
                .queryParam("lng", pvForecastRequestDto.getLng())
                .encode()
                .toUriString();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+homeInfo.getAiserviceToken());
        ResponseEntity<PVForecastQueriesDto[]> response =
                restTemplate
                        .exchange(url, HttpMethod.GET,  new HttpEntity<>(headers), PVForecastQueriesDto[].class);
        if(response.getBody() == null)
            return new ArrayList<>();
        return Arrays.asList(response.getBody());
    }

    @Override
    public List<HomeAssistantHistoryDto> getConsumptionForecast(ConsumptionForecastRequestDto consumptionForecastRequestDto, Integer days){
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl+GET_CONSUMPTION_FORECAST)
                .queryParam("days", days)
                .encode()
                .toUriString();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+homeInfo.getAiserviceToken());
        ResponseEntity<HomeAssistantHistoryDto[]> response =
                restTemplate
                        .exchange(url, HttpMethod.POST,  new HttpEntity<>(consumptionForecastRequestDto, headers), HomeAssistantHistoryDto[].class);
        if(response.getBody() == null)
            return new ArrayList<>();
        return Arrays.asList(response.getBody());
    }




}
