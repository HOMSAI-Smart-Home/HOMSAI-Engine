package app.homsai.engine.common.infrastructure.security;

import app.homsai.engine.common.application.http.ui.LoginView;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    @Autowired
    EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    @Autowired
    EntitiesCommandsApplicationService entitiesCommandsApplicationService;

    private static final String LOGOUT_SUCCESS_URL = "/";

    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) context.getAuthentication().getPrincipal();
        }
        // Anonymous or no authentication.
        return null;
    }

    public void logout() {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        homeInfo.setAiserviceEmail(null);
        homeInfo.setAiserviceToken(null);
        homeInfo.setAiserviceRefreshToken(null);
        entitiesCommandsApplicationService.saveHomeInfo(homeInfo);
        UI.getCurrent().navigate(LoginView.class);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }
}