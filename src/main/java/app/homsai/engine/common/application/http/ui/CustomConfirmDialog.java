package app.homsai.engine.common.application.http.ui;

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

public class CustomConfirmDialog extends Dialog {

    ComponentEventListener<ClickEvent<Button>> listener;

    public CustomConfirmDialog(String title, String description, List<String> args) {
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
        for (String arg : args) {
            description = description.replace("ARG" + counter, arg);
            counter++;
        }
        Paragraph paragraph = new Paragraph();
        paragraph.getElement().setProperty("innerHTML", description);

        Button closeButton = new Button(EnText.CANCEL);
        closeButton.addClickListener(e -> dialog.close());

        Button continueButton = new Button(EnText.CONTINUE);
        continueButton.addClickListener(listener);

        VerticalLayout dialogLayout = new VerticalLayout(headline, paragraph, continueButton, closeButton);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        //dialogLayout.setAlignSelf(FlexComponent.Alignment.END, closeButton);

        return dialogLayout;
    }

    public void setOnConfirmListener(ComponentEventListener<ClickEvent<Button>> listener){
        this.listener = listener;
    }
}
