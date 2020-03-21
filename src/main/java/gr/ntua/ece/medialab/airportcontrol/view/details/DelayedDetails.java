package gr.ntua.ece.medialab.airportcontrol.view.details;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightStatus;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import gr.ntua.ece.medialab.airportcontrol.util.MapEntry;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DelayedDetails implements Initializable {
    private ResourceBundle bundle;
    private Data data;
    private ObservableList<MapEntry<String, Flight>> entries;

    @FXML
    private TableView<MapEntry<String, Flight>> table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        data = Data.getInstance();

        bindFlights();
    }

    private void bindFlights() {
        TableColumn<MapEntry<String, Flight>, String> parkingColumn = new TableColumn<>(
                bundle.getString("details.parking_col.name"));
        parkingColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        ParkingBase parking = df.getValue().getValue().parkingProperty().get();
                        // NOTE: Data consistency is assumed (status=holding therefore parking is not null)
                        return parking.idProperty().get();
                    },
                    df.getValue().getValue().parkingProperty()
                ));

        TableColumn<MapEntry<String, Flight>, String> idColumn = new TableColumn<>(
                bundle.getString("details.id_col.name"));
        idColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> df.getValue().getValue().idProperty().get(),
                    df.getValue().getValue().idProperty()
                ));

        TableColumn<MapEntry<String, Flight>, String> flightTypeColumn = new TableColumn<>(
                bundle.getString("details.flight_type_col.name"));
        flightTypeColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        FlightType flightType = df.getValue().getValue().flightTypeProperty().get();
                        return bundle.getString("flight_type." + flightType.toString());
                    },
                    df.getValue().getValue().flightTypeProperty()
                ));

        TableColumn<MapEntry<String, Flight>, String> planeTypeColumn = new TableColumn<>(
                bundle.getString("details.plane_type_col.name"));
        planeTypeColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        PlaneType planeType = df.getValue().getValue().planeTypeProperty().get();
                        return bundle.getString("plane_type." + planeType.toString());
                    },
                    df.getValue().getValue().planeTypeProperty()
                ));

        TableColumn<MapEntry<String, Flight>, String> stdColumn = new TableColumn<>(
                bundle.getString("details.std_col.name"));
        stdColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        int std = df.getValue().getValue().stdProperty().get();
                        return data.timeData().minutesToHM(std);
                    },
                    df.getValue().getValue().stdProperty()
                ));

        table.getColumns().setAll(parkingColumn, idColumn, flightTypeColumn, planeTypeColumn, stdColumn);

        SimpleObjectProperty<ObservableMap<String, Flight>> flights = data.flightData().flightsProperty();
        data.timeData().minutesSinceStartProperty().addListener((obs, oldValue, newValue) -> {
            Map<String, Flight> delayed = data.flightData().getDelayedFlights(flights.get());
            entries = FXCollections.observableArrayList(MapEntry.mapToMapEntryArrayList(delayed));
            table.setItems(entries);
        });

        Map<String, Flight> delayed = data.flightData().getDelayedFlights(flights.get());
        entries = FXCollections.observableArrayList(MapEntry.mapToMapEntryArrayList(delayed));
        table.setItems(entries);
    }
}
