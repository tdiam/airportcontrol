package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import gr.ntua.ece.medialab.airportcontrol.util.R;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ParkingDashboard implements Initializable {
    private Data data;
    SimpleListProperty<Map.Entry<String, ParkingBase>> parkings;

    @FXML
    GridPane parkingDashboard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();
        parkings = data.airportData().getParkings();

        populateDashboard();
        parkings.addListener((obs, oldValue, newValue) -> populateDashboard());
    }

    private void populateDashboard() {
        int N_COLS = 5;

        parkingDashboard.getChildren().clear();

        List<Map.Entry<String, ParkingBase>> parkingList = parkings.get();

        for (int idx = 0; idx < parkingList.size(); ++idx) {
            int row = idx / N_COLS;
            int col = idx % N_COLS;
            Parent itemView = loadParkingItem(parkingList.get(idx).getValue());
            parkingDashboard.add(itemView, col, row);
        }
    }

    private Parent loadParkingItem(ParkingBase parking) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingItem.fxml"), R.getBundle());
            Parent view = loader.load();
            loader.<ParkingItem>getController().initItemData(parking);
            return view;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
