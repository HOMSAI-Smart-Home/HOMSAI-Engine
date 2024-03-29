package app.homsai.engine.pvoptimizer.application.http.converters;



import app.homsai.engine.pvoptimizer.application.http.dtos.*;
import app.homsai.engine.pvoptimizer.domain.models.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Giacomo Agostini on 04/04/22.
 */
@Service
@Transactional
public class PVOptimizerMapperImpl implements PVOptimizerMapper {

    @Autowired
    ModelMapper modelMapper;


    @Override
    public List<OptimizerHVACDeviceDto> convertToDto(HashMap<String, OptimizerHVACDevice> hvacDeviceHashMap) {
        return hvacDeviceHashMap.values().stream()
                .map(h -> modelMapper.map(h, OptimizerHVACDeviceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OptimizerHVACInterval> convertToDtoIntervals(List<HvacDeviceInterval> intervals) {
        if(intervals == null)
            return null;
        return intervals.stream()
                .map(h -> modelMapper.map(h, OptimizerHVACInterval.class))
                .collect(Collectors.toList());
    }

    @Override
    public OptimizerHVACDeviceDto convertToDto(OptimizerHVACDevice optimizerHVACDevice) {
        return modelMapper.map(optimizerHVACDevice, OptimizerHVACDeviceDto.class);
    }

    @Override
    public List<OptimizerHVACDeviceDto> convertToDto(Collection<OptimizerHVACDevice> values) {
        return values.stream()
                .map(h -> modelMapper.map(h, OptimizerHVACDeviceDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<HVACDeviceDto> convertToDto(List<HVACDevice> hvacDeviceList) {
        return hvacDeviceList.stream()
                .map(h -> modelMapper.map(h, HVACDeviceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public HVACDeviceDto convertToDto(HVACDevice syncedDevice) {
        return modelMapper.map(syncedDevice, HVACDeviceDto.class);
    }

    @Override
    public List<HVACEquipmentDto> convertToDtoEquipments(List<HVACEquipment> hvacEquipmentList) {
        return hvacEquipmentList.stream()
                .map(h -> modelMapper.map(h, HVACEquipmentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public HVACEquipmentDto convertToDto(HVACEquipment hvacEquipment) {
        return modelMapper.map(hvacEquipment, HVACEquipmentDto.class);
    }

    @Override
    public HomeHvacSettingsDto convertToDto(HomeHvacSettingsUpdateDto homeHvacSettingsUpdateDto) {
        return modelMapper.map(homeHvacSettingsUpdateDto, HomeHvacSettingsDto.class);
    }
}
