package app.homsai.engine.pvoptimizer.gateways;

import app.homsai.engine.pvoptimizer.gateways.dtos.HvacDevicesOptimizationPVResponseDto;
import app.homsai.engine.pvoptimizer.gateways.dtos.HvacOptimizationPVRequestDto;

public interface HomsaiAIServiceGateway {

    HvacDevicesOptimizationPVResponseDto getHvacDevicesOptimizationPV(HvacOptimizationPVRequestDto hvacOptimizationPVRequestDto);
}
