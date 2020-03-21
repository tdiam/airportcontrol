package gr.ntua.ece.medialab.airportcontrol.view.details;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;
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

public class NextDeparturesDetails implements Initializable {
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

        table.getColumns().setAll(idColumn, flightTypeColumn, planeTypeColumn);

        SimpleObjectProperty<ObservableMap<String, Flight>> flights = data.flightData().flightsProperty();
        data.timeData().minutesSinceStartProperty().addListener((obs, oldValue, newValue) -> {
            Map<String, Flight> nextDepartures = data.flightData().getNextDepartures(flights.get());
            entries = FXCollections.observableArrayList(MapEntry.mapToMapEntryArrayList(nextDepartures));
            table.setItems(entries);
        });

        Map<String, Flight> nextDepartures = data.flightData().getNextDepartures(flights.get());
        entries = FXCollections.observableArrayList(MapEntry.mapToMapEntryArrayList(nextDepartures));
        table.setItems(entries);
    }
}
