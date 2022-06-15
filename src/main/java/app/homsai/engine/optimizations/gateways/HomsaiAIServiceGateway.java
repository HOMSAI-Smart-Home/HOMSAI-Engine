package app.homsai.engine.optimizations.gateways;

import app.homsai.engine.optimizations.gateways.dtos.HvacDevicesOptimizationPVResponseDto;
import app.homsai.engine.optimizations.gateways.dtos.HvacOptimizationPVRequestDto;

public interface HomsaiAIServiceGateway {

    HvacDevicesOptimizationPVResponseDto getHvacDevicesOptimizationPV(HvacOptimizationPVRequestDto hvacOptimizationPVRequestDto);
}
