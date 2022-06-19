package app.homsai.engine.entities.domain.services;

import app.homsai.engine.entities.application.http.dtos.AreaDto;
import app.homsai.engine.entities.domain.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntitiesQueriesService {

    Page<HAEntity> findAllEntities(Pageable pageRequest, String search);

    Page<HomsaiEntity> findAllHomsaiEntities(Pageable pageRequest, String search);

    Page<HomsaiEntitiesHistoricalState> findAllHomsaiHistoricalStates(Pageable pageRequest, String search);

    List<Area> findAllAreas();

    Page<HVACDevice> findAllHomsaiHvacDevices(Pageable unpaged, String s);

    HomeInfo findHomeInfo();

    HVACDevice findOneHvacDeviceByEntityId(String entityId);

    Area getHomeArea();
}
