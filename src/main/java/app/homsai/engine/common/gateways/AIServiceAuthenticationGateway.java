package app.homsai.engine.common.gateways;

import app.homsai.engine.common.gateways.dtos.LoginResponseDto;
import org.springframework.security.core.AuthenticationException;

public interface AIServiceAuthenticationGateway {
    LoginResponseDto login(String username, String password);

    LoginResponseDto refreshToken(String refreshToken) throws AuthenticationException;
}
