package app.homsai.engine.homeassistant.gateways;

import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HAEntity;
import app.homsai.engine.entities.domain.services.EntitiesCommandsService;
import app.homsai.engine.homeassistant.gateways.dto.ws.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static app.homsai.engine.homeassistant.gateways.dto.ws.HomeAssistantWSStatusList.*;

@Service
public class HomeAssistantWSAPIGatewayImpl implements HomeAssistantWSAPIGateway {

    @Autowired
    EntitiesCommandsService entitiesCommandsService;

    @Value("${home_assistant.api_url}")
    private String apiUrl;
    @Value("${home_assistant.token}")
    private String token;

    private WebSocketClient socket;
    private int messages;
    private int status = STATUS_NOT_CONNECTED;

    private List<HAEntity> entityList;

    private final int offset = 100;
    private int entityRegistryRequestId = -1;

    private int requestsSent = 0;
    private int answersReceive = 0;
    private Object lock;

    @Override
    public void syncEntityAreas(List<HAEntity> entityList, Object lock){
        this.entityList = entityList;
        this.lock = lock;
        requestsSent = 0;
        answersReceive = 0;
        String wsUrl = apiUrl.replace("http", "ws")+"/api/websocket";
        connect(URI.create(wsUrl));
    }
    
    public void connect(URI serverUri){
        if(this.socket != null && this.socket.isOpen()) {
            this.socket.close();
            status = STATUS_NOT_CONNECTED;
        }
        this.messages = 1;
        this.socket = new WebSocketClient(serverUri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
            }

            @Override
            public void onMessage(String message) {
                try {
                    switch(status) {
                        case STATUS_NOT_CONNECTED:
                            onAuthRequired();
                            break;
                        case STATUS_NOT_AUTHORIZED:
                            onAuthInvalid();
                            break;
                        case STATUS_CONNECTED:
                            status = STATUS_AUTHORIZED;
                            onAuthOk();
                            break;
                        case STATUS_AUTHORIZED:
                            HomeAssistantWSResponseDto homeAssistantWSResponseDto = getJackson(false).readValue(message, HomeAssistantWSResponseDto.class);
                            if(homeAssistantWSResponseDto.getId() == entityRegistryRequestId)
                                onResult(getJackson(true).readValue(message, HomeAssistantWSResultResponseDto.class));
                            else
                                onSearchRelated(getJackson(true).readValue(message, HomeAssistantWSSearchRelatedResponseDto.class));
                            answersReceive += 1;
                            if(answersReceive >= requestsSent) {
                                synchronized (lock) {
                                    lock.notifyAll();
                                }
                            }
                    }
                } catch(Exception ex) {
                    System.err.println("onMessage:" + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                this.close();
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("Socket_Error: " + ex.getMessage());
                ex.fillInStackTrace();
            }
        };
        this.socket.connect();
    }


    public void onAuthInvalid() {

    }

    public void onAuthRequired() {
        status = STATUS_CONNECTED;
        try {
            this.socket.send(getJackson(true).writeValueAsString(new HomeAssistantWSAuthRequestDto(token)));
        } catch (Exception ex) {
            System.err.println("onAuthRequired:" + ex.getMessage());
        }
    }

    public void onAuthOk() throws JsonProcessingException {
        for(int i = 0; i<entityList.size(); i++){
            sendSearchRelatedRequest(entityList.get(i).getEntityId(), i+offset);
            requestsSent += 1;
        }
        sendResultRequest();
        requestsSent += 1;
    }

    public void onSearchRelated(HomeAssistantWSSearchRelatedResponseDto resultMessage) {
        if(resultMessage != null && resultMessage.getResult() != null){
            if(resultMessage.getResult().getArea() != null){
                HAEntity entityToUpdate = entityList.get(resultMessage.getId() - offset);
                if (entityToUpdate.getAreas() == null)
                    entityToUpdate.setAreas(new ArrayList<>());
                for (String areaName : resultMessage.getResult().getArea()) {
                    Area area = entitiesCommandsService.getAreaByNameOrCreate(areaName);
                    entityToUpdate.getAreas().add(area);
                }
            }
        }
    }


    public void onResult(HomeAssistantWSResultResponseDto resultMessage) {
        if(resultMessage != null && resultMessage.getResult() != null){
            for(HomeAssistantWSResultResultDto h : resultMessage.getResult()){
                if(h.getAreaId() != null) {
                    HAEntity haEntity = entityList.stream()
                            .filter(ha -> ha.getEntityId().equals(h.getEntityId()))
                            .findAny()
                            .orElse(null);
                    if (haEntity != null) {
                        if (haEntity.getAreas() == null)
                            haEntity.setAreas(new ArrayList<>());
                        Area area = entitiesCommandsService.getAreaByNameOrCreate(h.getAreaId());
                        haEntity.getAreas().add(area);
                    }
                }
            }
        }
    }

    public void sendSearchRelatedRequest(String sensorId, int id) throws JsonProcessingException {
        HomeAssistantWSSearchRelatedRequestDto message = new HomeAssistantWSSearchRelatedRequestDto(sensorId, id);
        send(getJackson(true).writeValueAsString(message));
    }

    public void sendResultRequest() throws JsonProcessingException {
        entityRegistryRequestId = entityList.size()+offset;
        HomeAssistantWSGenericRequest message = new HomeAssistantWSGenericRequest(HomeAssistantWSRequestList.entityRegistry, entityRegistryRequestId);
        send(getJackson(true).writeValueAsString(message));
    }


    public static ObjectMapper getJackson(boolean failOnUnknowns) {
        ObjectMapper om = new ObjectMapper();
        om.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknowns);
        return om;
    }

    public void send(String message) {
        try {
            this.socket.send(message);
        } catch (Exception ex) {
            System.err.println("send:" + ex.getMessage());
        }
    }


}
