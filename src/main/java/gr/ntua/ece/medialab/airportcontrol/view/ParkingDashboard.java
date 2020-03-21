package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
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
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ParkingDashboard implements Initializable {
    private Data data;
    SimpleObjectProperty<ObservableMap<String, ParkingBase>> parkingsProperty;
    private ResourceBundle bundle;

    @FXML
    GridPane parkingDashboard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();
        parkingsProperty = data.airportData().parkingsProperty();
        bundle = resources;

        populateDashboard(parkingsProperty.get());
//        parkingsProperty.addListener()
    }

    private void populateDashboard(ObservableMap<String, ParkingBase> parkings) {
        int N_COLS = 5;

        List<ParkingBase> parkingList = parkings.values().stream().sorted().collect(Collectors.toList());
        int N_ROWS = (int)Math.ceil((float)parkingList.size() / N_COLS);
        for (int row = 0; row < N_ROWS; ++row) {
            for (int col = 0; col < N_COLS; ++col) {
                int idx = row * N_COLS + col;
                if (idx >= parkingList.size()) break;

                Parent itemView = loadParkingItem(parkingList.get(idx));
                parkingDashboard.add(itemView, col, row);
            }
        }
    }

    private Parent loadParkingItem(ParkingBase parking) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingItem.fxml"), bundle);
            Scene view = loader.load();
            loader.<ParkingItem>getController().initItemData(parking);
            return view.getRoot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
