package app.homsai.engine.common.application.http.ui;


import app.homsai.engine.entities.application.http.cache.HomsaiEntityShowCacheRepository;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.w3c.dom.events.UIEvent;

import java.util.List;

@Route(value="", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    private HomsaiEntityShowCacheRepository homsaiEntityShowCacheRepository;

    final Grid<HomsaiEntityShowDto> grid;

    public MainView(HomsaiEntityShowCacheRepository homsaiEntityShowCacheRepository) {
        this.homsaiEntityShowCacheRepository = homsaiEntityShowCacheRepository;
        this.grid = new Grid<>(HomsaiEntityShowDto.class);
        setSizeFull();
        UI.getCurrent().setPollInterval(5000);
        UI.getCurrent().addPollListener(pollEvent -> listHomsaiEntities());
        grid.setSizeFull();
        add(grid);
        listHomsaiEntities();

    }

    private void listHomsaiEntities() {
        List<HomsaiEntityShowDto> homsaiEntityShowDtos = homsaiEntityShowCacheRepository.getHomsaiEntityShowDtoList();
        grid.setItems(homsaiEntityShowDtos);
    }
}
