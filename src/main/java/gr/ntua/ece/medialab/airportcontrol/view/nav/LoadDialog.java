package gr.ntua.ece.medialab.airportcontrol.view.nav;

import static gr.ntua.ece.medialab.airportcontrol.util.EventUtil.getWindowFromEvent;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadDialog {
    @FXML
    TextField scenarioField;

    @FXML
    Label error;

    @FXML
    void handleSubmit(Event evt) {
        error.setVisible(false);
        Stage stage = getWindowFromEvent(evt);
        Data data = Data.getInstance();
        String scenarioId = scenarioField.getText();
        try {
            data.importSetup(scenarioId);
            data.importAirport(scenarioId);
            stage.close();
        } catch (IOException e) {
            // Scenario files do not exist
            error.setVisible(true);
            scenarioField.clear();
        }
    }
}
