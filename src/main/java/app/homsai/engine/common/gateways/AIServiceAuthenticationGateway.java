package app.homsai.engine.common.gateways;

import app.homsai.engine.common.gateways.dtos.LoginResponseDto;

public interface AIServiceAuthenticationGateway {
    LoginResponseDto login(String username, String password);
}
