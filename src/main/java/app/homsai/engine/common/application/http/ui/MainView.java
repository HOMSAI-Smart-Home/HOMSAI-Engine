package app.homsai.engine.common.application.http.ui;


import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;
import app.homsai.engine.entities.application.services.EntitiesQueriesApplicationService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route
public class MainView extends VerticalLayout {

    private EntitiesQueriesApplicationService entitiesQueriesApplicationService;

    final Grid<HomsaiEntityShowDto> grid;

    public MainView(EntitiesQueriesApplicationService entitiesQueriesApplicationService) {
        this.entitiesQueriesApplicationService = entitiesQueriesApplicationService;
        this.grid = new Grid<>(HomsaiEntityShowDto.class);
        add(grid);
        listCustomers();
    }

    private void listCustomers() {
        List<HomsaiEntityShowDto> homsaiEntityShowDtos = entitiesQueriesApplicationService.getAllLastHomsaiEntityToShow();
        grid.setItems(homsaiEntityShowDtos);
    }
}
