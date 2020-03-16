package gr.ntua.ece.medialab.airportcontrol.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupDialog {
    private Stage stage;

    public PopupDialog(String title, Parent view) {
        stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(250);

        Scene scene = new Scene(view);
        // Close when user presses ESC
        scene.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public Stage getStage() {
        return stage;
    }

    public void show() {
        stage.showAndWait();
    }
}
