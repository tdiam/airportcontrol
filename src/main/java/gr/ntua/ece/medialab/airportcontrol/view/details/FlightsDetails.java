package gr.ntua.ece.medialab.airportcontrol.view.details;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightStatus;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;
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

public class FlightsDetails implements Initializable {
    private Data data;

    @FXML
    private TableView<Map.Entry<String, Flight>> table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();

        bindFlights();
    }

    private void bindFlights() {
        TableColumn<Map.Entry<String, Flight>, String> idColumn = new TableColumn<>(
                R.get("details.id_col.name"));
        idColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> df.getValue().getValue().idProperty().get(),
                    df.getValue().getValue().idProperty()
                ));

        TableColumn<Map.Entry<String, Flight>, String> cityColumn = new TableColumn<>(
                R.get("details.city_col.name"));
        cityColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> df.getValue().getValue().cityProperty().get(),
                    df.getValue().getValue().cityProperty()
                ));

        TableColumn<Map.Entry<String, Flight>, String> flightTypeColumn = new TableColumn<>(
                R.get("details.flight_type_col.name"));
        flightTypeColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        FlightType flightType = df.getValue().getValue().flightTypeProperty().get();
                        return R.get("flight_type." + flightType.toString());
                    },
                    df.getValue().getValue().flightTypeProperty()
                ));

        TableColumn<Map.Entry<String, Flight>, String> planeTypeColumn = new TableColumn<>(
                R.get("details.plane_type_col.name"));
        planeTypeColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        PlaneType planeType = df.getValue().getValue().planeTypeProperty().get();
                        return R.get("plane_type." + planeType.toString());
                    },
                    df.getValue().getValue().planeTypeProperty()
                ));

        TableColumn<Map.Entry<String, Flight>, String> statusColumn = new TableColumn<>(
                R.get("details.status_col.name"));
        statusColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        FlightStatus status = df.getValue().getValue().statusProperty().get();
                        return R.get("flight_status." + status.toString());
                    },
                    df.getValue().getValue().statusProperty()
                ));

        TableColumn<Map.Entry<String, Flight>, String> parkingColumn = new TableColumn<>(
                R.get("details.parking_col.name"));
        parkingColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        ParkingBase parking = df.getValue().getValue().parkingProperty().get();
                        if (parking == null) return "";
                        return parking.idProperty().get();
                    },
                    df.getValue().getValue().parkingProperty()
                ));

        TableColumn<Map.Entry<String, Flight>, String> stdColumn = new TableColumn<>(
                R.get("details.std_col.name"));
        stdColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        int std = df.getValue().getValue().stdProperty().get();
                        return TimeUtil.minutesToHM(std);
                    },
                    df.getValue().getValue().stdProperty()
                ));

        table.getColumns().setAll(idColumn, cityColumn, flightTypeColumn, planeTypeColumn, statusColumn,
                parkingColumn, stdColumn);

        table.itemsProperty().bind(data.flightData().getFlights());
    }
}
