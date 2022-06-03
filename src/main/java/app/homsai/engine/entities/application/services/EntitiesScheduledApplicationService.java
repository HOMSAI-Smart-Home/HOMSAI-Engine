package app.homsai.engine.entities.application.services;

import app.homsai.engine.entities.application.http.dtos.HomsaiEntitiesHistoricalStateDto;
import app.homsai.engine.entities.domain.exceptions.AreaNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface EntitiesScheduledApplicationService {

    void getAllHomeAssistantEntities();

    void getAllHomsaiEntityValues();
}
