package app.homsai.engine.common.application.http.ui.components;


import app.homsai.engine.common.application.http.ui.MainView;
import app.homsai.engine.common.domain.utils.EnText;
import app.homsai.engine.common.infrastructure.security.SecurityService;
import app.homsai.engine.pvoptimizer.application.http.ui.HvacDevicesView;
import app.homsai.engine.entities.application.http.ui.SettingsView;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;

@Theme(themeFolder = "homsai")
public class MainLayout extends AppLayout {

    private final SecurityService securityService;
    private final EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    public MainLayout(SecurityService securityService, EntitiesQueriesApplicationService entitiesQueriesApplicationService) {
        this.securityService = securityService;
        this.entitiesQueriesApplicationService = entitiesQueriesApplicationService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Homsai Engine");
        logo.addClassNames("text-l", "m-m");
        Label currentUser = new Label(entitiesQueriesApplicationService.getHomeInfo().getAiserviceEmail());
        Button logout = new Button("Log out", e -> confirmLogout());

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                currentUser,
                logout
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    public void confirmLogout(){
        CustomConfirmDialog d1 = new CustomConfirmDialog(EnText.LOGOUT_TITLE, EnText.LOGOUT_TEXT, null);
        d1.setOnConfirmListener(securityService::logout);
        d1.open();
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Home", MainView.class);
        RouterLink hvacLink = new RouterLink("HVAC Devices", HvacDevicesView.class);
        RouterLink settingsLink = new RouterLink("Settings", SettingsView.class);
        listLink.setHighlightCondition(HighlightConditions.locationPrefix());
        hvacLink.setHighlightCondition(HighlightConditions.locationPrefix());
        settingsLink.setHighlightCondition(HighlightConditions.locationPrefix());

        addToDrawer(new VerticalLayout(
                listLink,
                hvacLink,
                settingsLink
        ));
    }
}
