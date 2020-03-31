package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusBar implements Initializable {
    private Data data;

    @FXML
    private Label statusMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();

        bindMessage();
        bindIsError();
    }

    private void bindMessage() {
        SimpleStringProperty prop = data.statusData().statusProperty();
        statusMessage.textProperty().bind(prop);
    }

    private void bindIsError() {
        SimpleBooleanProperty prop = data.statusData().isErrorProperty();
        onIsErrorChange(prop.get());
        prop.addListener((obs, oldValue, newValue) -> onIsErrorChange(newValue));
    }

    private void onIsErrorChange(boolean isError) {
        if (isError) {
            statusMessage.getStyleClass().add("error");
        } else {
            statusMessage.getStyleClass().remove("error");
        }
    }
}
