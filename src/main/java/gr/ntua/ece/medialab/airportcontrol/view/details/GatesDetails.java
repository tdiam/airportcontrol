package gr.ntua.ece.medialab.airportcontrol.view.details;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingStatus;
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

public class GatesDetails implements Initializable {
    private ResourceBundle bundle;
    private Data data;
    private ObservableList<MapEntry<String, ParkingBase>> entries;

    @FXML
    private TableView<MapEntry<String, ParkingBase>> table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        data = Data.getInstance();

        bindParkings();
    }

    private void bindParkings() {
        TableColumn<MapEntry<String, ParkingBase>, String> idColumn = new TableColumn<>(
                bundle.getString("details.id_col.name"));
        idColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> df.getValue().getKey()));

        TableColumn<MapEntry<String, ParkingBase>, String> statusColumn = new TableColumn<>(
                bundle.getString("details.status_col.name"));
        statusColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> {
            ParkingStatus status = df.getValue().getValue().getStatus();
            return bundle.getString("parking_status." + status.name());
        }));

        TableColumn<MapEntry<String, ParkingBase>, String> flightColumn = new TableColumn<>(
                bundle.getString("details.flight_col.name"));
        flightColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> {
            Flight flight = df.getValue().getValue().getParkedFlight();
            if (flight == null) return "";
            return flight.getId();
        }));

        TableColumn<MapEntry<String, ParkingBase>, String> stdColumn = new TableColumn<>(
                bundle.getString("details.std_col.name"));
        stdColumn.setCellValueFactory(df -> Bindings.createStringBinding(() -> {
            Flight flight = df.getValue().getValue().getParkedFlight();
            if (flight == null) return "";
            return data.timeData().minutesToHM(flight.getStd());
        }));

        table.getColumns().setAll(idColumn, statusColumn, flightColumn, stdColumn);

        SimpleObjectProperty<ObservableMap<String, ParkingBase>> prop = data.airportData().parkingsProperty();
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
    private void flightsListener(MapChangeListener.Change<? extends String, ? extends ParkingBase> change) {
        boolean removed = change.wasRemoved();
        if (removed != change.wasAdded()) {
            if (removed) {
                // no put for existing key
                // remove pair completely
                entries.remove(new MapEntry<>(change.getKey(), (ParkingBase) null));
            } else {
                // add new entry
                entries.add(new MapEntry<>(change.getKey(), change.getValueAdded()));
            }
        } else {
            // replace existing entry
            MapEntry<String, ParkingBase> entry = new MapEntry<>(change.getKey(), change.getValueAdded());

            int index = entries.indexOf(entry);
            entries.set(index, entry);
        }
    }
}
