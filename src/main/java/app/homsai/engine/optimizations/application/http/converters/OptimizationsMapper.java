package app.homsai.engine.optimizations.application.http.converters;


import app.homsai.engine.entities.application.http.dtos.*;
import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.optimizations.application.http.dtos.HvacDeviceDto;
import app.homsai.engine.optimizations.domain.models.HvacDevice;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Giacomo Agostini on 04/04/22.
 */

public interface OptimizationsMapper {

    List<HvacDeviceDto> convertToDto(HashMap<String, HvacDevice> hvacDeviceHashMap);
}
