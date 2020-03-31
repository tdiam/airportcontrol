package gr.ntua.ece.medialab.airportcontrol.view.details;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import gr.ntua.ece.medialab.airportcontrol.util.R;
import gr.ntua.ece.medialab.airportcontrol.util.TimeUtil;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class GatesDetails implements Initializable {
    private Data data;

    @FXML
    private TableView<Map.Entry<String, ParkingBase>> table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();

        bindParkings();
    }

    private void bindParkings() {
        TableColumn<Map.Entry<String, ParkingBase>, String> idColumn = new TableColumn<>(
                R.get("details.id_col.name"));
        idColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> df.getValue().getValue().idProperty().get(),
                    df.getValue().getValue().idProperty()
                ));

        TableColumn<Map.Entry<String, ParkingBase>, String> statusColumn = new TableColumn<>(
                R.get("details.status_col.name"));
        statusColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        boolean isAvailable = df.getValue().getValue().isAvailableProperty().get();
                        return R.get("parking_status." + (isAvailable ? "AVAILABLE" : "OCCUPIED"));
                    },
                    df.getValue().getValue().isAvailableProperty()
                ));

        TableColumn<Map.Entry<String, ParkingBase>, String> flightColumn = new TableColumn<>(
                R.get("details.flight_col.name"));
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
                R.get("details.std_col.name"));
        stdColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        Flight flight = df.getValue().getValue().parkedFlightProperty().get();
                        if (flight == null) return "";
                        return TimeUtil.minutesToHM(flight.stdProperty().get());
                    },
                    df.getValue().getValue().parkedFlightProperty()
                ));

        table.getColumns().setAll(idColumn, statusColumn, flightColumn, stdColumn);

        table.itemsProperty().bind(data.airportData().getParkings());
    }
}
