package gr.ntua.ece.medialab.airportcontrol.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupDialog {
    private Stage stage;
    private boolean blocking;

    public PopupDialog(Parent view) {
        this("", view, true);
    }

    public PopupDialog(String title, Parent view) {
        this(title, view, true);
    }

    public PopupDialog(String title, Parent view, boolean blocking) {
        this.blocking = blocking;

        stage = new Stage();

        if (blocking) {
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        stage.setTitle(title);

        Scene scene = new Scene(view);
        // Close when user presses ESC
        scene.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        stage.setScene(scene);
    }

    public Stage getStage() {
        return stage;
    }

    public void show() {
        if (blocking) {
            stage.showAndWait();
        } else {
            stage.show();
        }
    }
}
