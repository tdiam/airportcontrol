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
import java.util.ResourceBundle;

public class FlightsDetails implements Initializable {
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
        TableColumn<MapEntry<String, Flight>, String> idColumn = new TableColumn<>(
                bundle.getString("details.id_col.name"));
        idColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> df.getValue().getKey()));

        TableColumn<MapEntry<String, Flight>, String> cityColumn = new TableColumn<>(
                bundle.getString("details.city_col.name"));
        cityColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> {
            return df.getValue().getValue().getCity();
        }));

        TableColumn<MapEntry<String, Flight>, String> flightTypeColumn = new TableColumn<>(
                bundle.getString("details.flight_type_col.name"));
        flightTypeColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> {
            FlightType flightType = df.getValue().getValue().getFlightType();
            return bundle.getString("flight_type." + flightType.toString());
        }));

        TableColumn<MapEntry<String, Flight>, String> planeTypeColumn = new TableColumn<>(
                bundle.getString("details.plane_type_col.name"));
        planeTypeColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> {
            PlaneType planeType = df.getValue().getValue().getPlaneType();
            return bundle.getString("plane_type." + planeType.toString());
        }));

        TableColumn<MapEntry<String, Flight>, String> statusColumn = new TableColumn<>(
                bundle.getString("details.status_col.name"));
        statusColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> {
            FlightStatus status = df.getValue().getValue().getStatus();
            return bundle.getString("flight_status." + status.toString());
        }));

        TableColumn<MapEntry<String, Flight>, String> parkingColumn = new TableColumn<>(
                bundle.getString("details.parking_col.name"));
        parkingColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> {
            ParkingBase parking = df.getValue().getValue().getParking();
            if (parking == null) return "";
            return parking.getId();
        }));

        TableColumn<MapEntry<String, Flight>, String> stdColumn = new TableColumn<>(
                bundle.getString("details.std_col.name"));
        stdColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> {
            int std = df.getValue().getValue().getStd();
            return data.timeData().minutesToHM(std);
        }));

        table.getColumns().setAll(idColumn, cityColumn, flightTypeColumn, planeTypeColumn, statusColumn,
                parkingColumn, stdColumn);

        SimpleObjectProperty<ObservableMap<String, Flight>> prop = data.flightData().flightsProperty();
        prop.addListener((obs, oldValue, newValue) -> {
            entries = FXCollections.observableArrayList(MapEntry.mapToMapEntryArrayList(newValue));
            newValue.addListener(this::flightsListener);
            table.setItems(entries);
        });

        entries = FXCollections.observableArrayList(MapEntry.mapToMapEntryArrayList(prop.get()));
        prop.get().addListener(this::flightsListener);
        table.setItems(entries);
    }

    /**
     * Adapted from:
     * https://stackoverflow.com/a/38490212
     */
    private void flightsListener(MapChangeListener.Change<? extends String, ? extends Flight> change) {
        boolean removed = change.wasRemoved();
        if (removed != change.wasAdded()) {
            if (removed) {
                // no put for existing key
                // remove pair completely
                entries.remove(new MapEntry<>(change.getKey(), (Flight) null));
            } else {
                // add new entry
                entries.add(new MapEntry<>(change.getKey(), change.getValueAdded()));
            }
        } else {
            // replace existing entry
            MapEntry<String, Flight> entry = new MapEntry<>(change.getKey(), change.getValueAdded());

            int index = entries.indexOf(entry);
            entries.set(index, entry);
        }
    }
}
