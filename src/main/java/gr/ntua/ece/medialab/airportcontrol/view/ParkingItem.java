package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ParkingItem implements Initializable {
    private Data data;
    private ResourceBundle bundle;
    private ParkingBase parking;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();
        bundle = resources;
    }

    void initItemData(ParkingBase parking) {
        this.parking = parking;
    }
}
