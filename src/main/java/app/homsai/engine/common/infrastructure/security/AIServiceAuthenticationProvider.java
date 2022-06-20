package app.homsai.engine.common.infrastructure.security;

import app.homsai.engine.common.gateways.AIServiceAuthenticationGateway;
import app.homsai.engine.common.gateways.dtos.LoginResponseDto;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AIServiceAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    AIServiceAuthenticationGateway aiServiceAuthenticationGateway;

    @Autowired
    EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Autowired
    EntitiesCommandsApplicationService entitiesCommandsApplicationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        LoginResponseDto loginResponseDto = aiServiceAuthenticationGateway.login(username, password);
        if (loginResponseDto.getToken() != null) {
            homeInfo.setAiserviceToken(loginResponseDto.getToken());
            homeInfo.setAiserviceRefreshToken(loginResponseDto.getRefreshToken());
            homeInfo.setAiserviceEmail(username);
            entitiesCommandsApplicationService.saveHomeInfo(homeInfo);
            return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
        } else {
            throw new BadCredentialsException("Authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?>aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}
