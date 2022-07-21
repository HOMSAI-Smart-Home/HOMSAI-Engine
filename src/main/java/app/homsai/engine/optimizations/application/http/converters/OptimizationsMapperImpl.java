package app.homsai.engine.optimizations.application.http.converters;



import app.homsai.engine.entities.application.http.converters.EntitiesMapper;
import app.homsai.engine.entities.application.http.dtos.*;
import app.homsai.engine.entities.domain.models.*;
import app.homsai.engine.homeassistant.gateways.dto.rest.HomeAssistantEntityDto;
import app.homsai.engine.optimizations.application.http.dtos.HvacDeviceDto;
import app.homsai.engine.optimizations.domain.models.HvacDevice;
import app.homsai.engine.optimizations.domain.models.HvacInterval;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Giacomo Agostini on 04/04/22.
 */
@Service
@Transactional
public class OptimizationsMapperImpl implements OptimizationsMapper {

    @Autowired
    ModelMapper modelMapper;


    @Override
    public List<HvacDeviceDto> convertToDto(HashMap<String, HvacDevice> hvacDeviceHashMap) {
        return hvacDeviceHashMap.values().stream()
                .map(h -> modelMapper.map(h, HvacDeviceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HvacInterval> convertToDtoIntervals(List<HvacDeviceInterval> intervals) {
        if(intervals == null)
            return null;
        return intervals.stream()
                .map(h -> modelMapper.map(h, HvacInterval.class))
                .collect(Collectors.toList());
    }
}
