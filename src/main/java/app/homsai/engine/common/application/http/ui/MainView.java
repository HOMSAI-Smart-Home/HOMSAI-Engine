package app.homsai.engine.common.application.http.ui;


import app.homsai.engine.common.application.http.ui.components.MainLayout;
import app.homsai.engine.common.domain.utils.EnText;
import app.homsai.engine.entities.application.http.cache.HomsaiEntityShowCacheRepository;
import app.homsai.engine.entities.application.http.dtos.HomsaiEntityShowDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Homsai Engine")
@Route(value="dashboard", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    private HomsaiEntityShowCacheRepository homsaiEntityShowCacheRepository;

    final Grid<HomsaiEntityShowDto> grid;
    Label label;

    public MainView(HomsaiEntityShowCacheRepository homsaiEntityShowCacheRepository) {
        this.homsaiEntityShowCacheRepository = homsaiEntityShowCacheRepository;
        this.grid = new Grid<>(HomsaiEntityShowDto.class);
        setSizeFull();
        UI.getCurrent().setPollInterval(5000);
        UI.getCurrent().addPollListener(pollEvent -> listHomsaiEntities());
        label = new Label(EnText.WAITING_FIRST_SYNC);
        label.setVisible(false);
        grid.setSizeFull();
        grid.setColumns("area", "temperature", "humidity");
        add(grid, label);
        listHomsaiEntities();

    }

    private void listHomsaiEntities() {
        List<HomsaiEntityShowDto> homsaiEntityShowDtos = homsaiEntityShowCacheRepository.getHomsaiEntityShowDtoList();
        if(homsaiEntityShowDtos == null || homsaiEntityShowDtos.size() == 0){
            label.getStyle().set("color", "#00FF00");
            label.setVisible(true);
            grid.setVisible(false);
        } else {
            label.setVisible(false);
            grid.setVisible(true);
            grid.setItems(homsaiEntityShowDtos);
        }
    }
}
