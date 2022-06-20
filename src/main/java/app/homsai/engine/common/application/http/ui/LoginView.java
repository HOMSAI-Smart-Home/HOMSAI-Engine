package app.homsai.engine.common.application.http.ui;

import app.homsai.engine.common.gateways.AIServiceAuthenticationGateway;
import app.homsai.engine.common.gateways.dtos.LoginResponseDto;
import app.homsai.engine.entities.application.http.ui.HvacDevicesInitView;
import app.homsai.engine.entities.application.services.EntitiesCommandsApplicationService;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import app.homsai.engine.entities.domain.models.HomeInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

@Route("")
@PageTitle("Homsai Engine | Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private final EntitiesQueriesApplicationService entitiesQueriesApplicationService;
    private final EntitiesCommandsApplicationService entitiesCommandsApplicationService;
    private final AIServiceAuthenticationGateway aiServiceAuthenticationGateway;
    private final AuthenticationProvider authenticationManager;

    public LoginView(AuthenticationProvider authenticationManager, EntitiesQueriesApplicationService entitiesQueriesApplicationService, AIServiceAuthenticationGateway aiServiceAuthenticationGateway, EntitiesCommandsApplicationService entitiesCommandsApplicationService){
        this.entitiesQueriesApplicationService = entitiesQueriesApplicationService;
        this.aiServiceAuthenticationGateway = aiServiceAuthenticationGateway;
        this.entitiesCommandsApplicationService = entitiesCommandsApplicationService;
        this.authenticationManager = authenticationManager;
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.addLoginListener(this::loginListener);
        add(new H1("HOMSAI Engine"), login);
    }

    private void loginListener(AbstractLogin.LoginEvent loginEvent){
        try {
            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(loginEvent.getUsername(), loginEvent.getPassword());
            Authentication auth = authenticationManager.authenticate(authReq);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            UI.getCurrent().navigate(MainView.class);
        }catch (Exception e){
            login.setError(true);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        HomeInfo homeInfo = entitiesQueriesApplicationService.getHomeInfo();
        if(homeInfo.getAiserviceRefreshToken() != null){
            try {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(homeInfo.getAiserviceEmail(), homeInfo.getAiserviceRefreshToken(), Collections.emptyList());
                String token = aiServiceAuthenticationGateway.refreshToken(homeInfo.getAiserviceRefreshToken()).getToken();
                homeInfo.setAiserviceToken(token);
                entitiesCommandsApplicationService.saveHomeInfo(homeInfo);
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(auth);
                beforeEnterEvent.forwardTo(MainView.class);
            } catch (Exception e){}
        }
    }
}