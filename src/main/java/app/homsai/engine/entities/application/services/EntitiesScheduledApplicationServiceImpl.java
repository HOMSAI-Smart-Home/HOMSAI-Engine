package app.homsai.engine.entities.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;

@Service
public class EntitiesScheduledApplicationServiceImpl implements EntitiesScheduledApplicationService {

    @Autowired
    private ServletContext context;

    @Value("${server.port:}")
    private String port;


    private String rootUrl = "http://localhost:";

    @Override
    @EventListener(ApplicationStartedEvent.class)
    @Scheduled(cron = "0 0 0 * * ?")
    public void getAllHomeAssistantEntities(){
        String endPoint = "/entities/hass";
        String contextPath = context.getContextPath();
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(rootUrl+port+contextPath+endPoint)
                .encode()
                .toUriString();
        restTemplate.postForEntity(url, HttpMethod.POST, String.class);
    }

    @Override
    @Scheduled(fixedRate = 5*60*1000)
    public void getAllHomsaiEntityValues(){
        String endPoint = "/entities/homsai";
        String contextPath = context.getContextPath();
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(rootUrl+port+contextPath+endPoint)
                .encode()
                .toUriString();
        restTemplate.postForEntity(url, HttpMethod.POST, String.class);
    }
}
