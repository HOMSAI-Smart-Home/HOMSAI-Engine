package app.homsai.engine.pvoptimizer.application.http.converters;


import app.homsai.engine.pvoptimizer.application.http.dtos.*;
import app.homsai.engine.pvoptimizer.domain.models.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Giacomo Agostini on 04/04/22.
 */

public interface PVOptimizerMapper {

    List<OptimizerHVACDeviceDto> convertToDto(HashMap<String, OptimizerHVACDevice> hvacDeviceHashMap);

    List<OptimizerHVACInterval> convertToDtoIntervals(List<HvacDeviceInterval> intervals);

    OptimizerHVACDeviceDto convertToDto(OptimizerHVACDevice optimizerHVACDevice);

    List<OptimizerHVACDeviceDto> convertToDto(Collection<OptimizerHVACDevice> values);

    List<HVACDeviceDto> convertToDto(List<HVACDevice> hvacDeviceList);

    HVACDeviceDto convertToDto(HVACDevice syncedDevice);

    List<HVACEquipmentDto> convertToDtoEquipments(List<HVACEquipment> hvacEquipmentList);

    HVACEquipmentDto convertToDto(HVACEquipment hvacEquipment);

    HomeHvacSettingsDto convertToDto(HomeHvacSettingsUpdateDto homeHvacSettingsUpdateDto);


}
