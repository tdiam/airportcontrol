package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Statbar implements Initializable {
    @FXML
    private Label totalTime;

    private Data data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();

        bindTotalTime();
    }

    private void bindTotalTime() {
        SimpleIntegerProperty prop = data.timeData().minutesSinceStartProperty();

        totalTime.setText(data.timeData().minutesToHM(prop.get()));
        prop.addListener((obs, oldValue, newValue) -> {
            totalTime.setText(data.timeData().minutesToHM((int)newValue));
        });
    }
}
