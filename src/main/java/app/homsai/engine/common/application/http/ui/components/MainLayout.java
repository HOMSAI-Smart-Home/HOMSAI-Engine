package app.homsai.engine.common.application.http.ui.components;


import app.homsai.engine.common.application.http.ui.MainView;
import app.homsai.engine.common.domain.utils.Consts;
import app.homsai.engine.common.domain.utils.EnText;
import app.homsai.engine.common.infrastructure.security.SecurityService;
import app.homsai.engine.entities.application.http.ui.HvacDevicesView;
import app.homsai.engine.entities.application.http.ui.SettingsView;
import app.homsai.engine.entities.domain.exceptions.HvacPowerMeterIdNotSet;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

import java.util.Collections;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Homsai Engine");
        logo.addClassNames("text-l", "m-m");
        Button logout = new Button("Log out", e -> confirmLogout());

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
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
        listLink.setHighlightCondition(HighlightConditions.sameLocation());
        hvacLink.setHighlightCondition(HighlightConditions.sameLocation());
        settingsLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listLink,
                hvacLink,
                settingsLink
        ));
    }
}
