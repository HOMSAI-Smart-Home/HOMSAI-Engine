package app.homsai.engine.common.application.http.ui.components;

import app.homsai.engine.common.domain.utils.EnText;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class CustomErrorDialog extends Dialog {


    public CustomErrorDialog(String title, String description, List<String> args) {
        super();
        configureDialog(title, description, args);
    }

    private void configureDialog(String title, String description, List<String> args) {
        VerticalLayout dialogLayout = createDialogLayout(this, title, description, args);
        add(dialogLayout);
    }

    private VerticalLayout createDialogLayout(Dialog dialog, String title, String description, List<String> args) {
        H2 headline = new H2(title);
        headline.getStyle().set("margin", "var(--lumo-space-m) 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        int counter = 0;
        if(args != null)
            for (String arg : args) {
                description = description.replace("ARG" + counter, arg);
                counter++;
            }
        Paragraph paragraph = new Paragraph();
        paragraph.getElement().setProperty("innerHTML", description);

        Button closeButton = new Button(EnText.CLOSE);
        closeButton.addClickListener(e -> dialog.close());


        VerticalLayout dialogLayout = new VerticalLayout(headline, paragraph, closeButton);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        return dialogLayout;
    }

}
