package gr.ntua.ece.medialab.airportcontrol.view.details;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class GatesDetails implements Initializable {
    private ResourceBundle bundle;
    private Data data;

    @FXML
    private TableView<Map.Entry<String, ParkingBase>> table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        data = Data.getInstance();

        bindParkings();
    }

    private void bindParkings() {
        TableColumn<Map.Entry<String, ParkingBase>, String> idColumn = new TableColumn<>(
                bundle.getString("details.id_col.name"));
        idColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> df.getValue().getValue().idProperty().get(),
                    df.getValue().getValue().idProperty()
                ));

        TableColumn<Map.Entry<String, ParkingBase>, String> statusColumn = new TableColumn<>(
                bundle.getString("details.status_col.name"));
        statusColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        boolean isAvailable = df.getValue().getValue().isAvailableProperty().get();
                        return bundle.getString("parking_status." + (isAvailable ? "AVAILABLE" : "OCCUPIED"));
                    },
                    df.getValue().getValue().isAvailableProperty()
                ));

        TableColumn<Map.Entry<String, ParkingBase>, String> flightColumn = new TableColumn<>(
                bundle.getString("details.flight_col.name"));
        flightColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        Flight flight = df.getValue().getValue().parkedFlightProperty().get();
                        if (flight == null) return "";
                        return flight.idProperty().get();
                    },
                    df.getValue().getValue().parkedFlightProperty()
                ));

        TableColumn<Map.Entry<String, ParkingBase>, String> stdColumn = new TableColumn<>(
                bundle.getString("details.std_col.name"));
        stdColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        Flight flight = df.getValue().getValue().parkedFlightProperty().get();
                        if (flight == null) return "";
                        return data.timeData().minutesToHM(flight.stdProperty().get());
                    },
                    df.getValue().getValue().parkedFlightProperty()
                ));

        table.getColumns().setAll(idColumn, statusColumn, flightColumn, stdColumn);

        table.itemsProperty().bind(data.airportData().getParkings());
    }
}
