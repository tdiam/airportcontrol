package gr.ntua.ece.medialab.airportcontrol.data;

import gr.ntua.ece.medialab.airportcontrol.model.*;
import gr.ntua.ece.medialab.airportcontrol.util.MapEntry;
import gr.ntua.ece.medialab.airportcontrol.util.ObservableUtil;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Flight data controller.
 */
public class FlightData {
    private Data root;

    private SimpleObjectProperty<ObservableMap<String, Flight>> flightMap = new SimpleObjectProperty<>(
            FXCollections.observableHashMap());

    private SimpleListProperty<Map.Entry<String, Flight>> flightList = new SimpleListProperty<>();

    /**
     * Creates a new instance of the flight data controller.
     * @param root Reference to root controller.
     */
    FlightData(Data root) {
        this.root = root;
        bindFlightsList();
    }

    private void bindFlightsList() {
        flightList.bind(Bindings.createObjectBinding(
            () -> ObservableUtil.observableMapToList(flightMap.get()).sorted(),
            flightMap
        ));
    }

    /**
     * Gets active flights as a map.
     * @return Property that stores an observable map of active flights with id-{@link Flight} as key-value pairs.
     */
    public SimpleObjectProperty<ObservableMap<String, Flight>> flightMapProperty() {
        return flightMap;
    }

    /**
     * Gets active flights as a list.
     * @return Property that stores an observable list of key-value pairs of active flights.
     */
    public SimpleListProperty<Map.Entry<String, Flight>> getFlights() {
        return flightList;
    }

    /**
     * Gets path of scenario file.
     * @param scenarioId Scenario ID given as a string.
     * @return Absolute path to input file for this scenario's flight data.
     */
    private String flightScenarioToFile(String scenarioId) {
        return new StringBuilder().append(root.getScenarioDirPath()).append("/")
                .append("setup_").append(scenarioId).append(".txt")
                .toString();
    }

    /**
     * Creates {@link Flight} instance from array of values as parsed from the scenario file.
     * @param values Array of strings as parsed from the scenario file.
     * @return A {@link Flight} instance.
     * @throws NumberFormatException If a string of numeric data is malformed.
     */
    private Flight arrayToFlight(String[] values) throws NumberFormatException {
        Flight flight = new Flight();
        flight.idProperty().set(values[0].trim());
        flight.cityProperty().set(values[1].trim());
        flight.flightTypeProperty().set(FlightType.valueOf(Integer.parseInt(values[2].trim())));
        flight.statusProperty().set(FlightStatus.EN_ROUTE);
        flight.parkedStatusProperty().set(FlightParkedStatus.NORMAL);
        flight.parkingProperty().set(null);
        flight.planeTypeProperty().set(PlaneType.valueOf(Integer.parseInt(values[3].trim())));
        flight.stdProperty().set(Integer.parseInt(values[4].trim()));
        flight.extraServicesProperty().set(new HashSet<>());
        return flight;
    }

    /**
     * Given a scenario ID, it parses the input for flights and stores the processed data.
     * @param scenarioId String that identifies the input file with the scenario data.
     * @throws IOException If reading the file fails.
     */
    public void importSetup(String scenarioId) throws IOException {
        String file = flightScenarioToFile(scenarioId);
        HashMap<String, Flight> imported = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    Flight flight = arrayToFlight(values);
                    imported.put(flight.idProperty().get(), flight);
                } catch (NumberFormatException e) {
                    String msg = new StringBuilder().append(file).append(":").append(lineNum)
                            .append(" could not be parsed").toString();
                    System.err.println(msg);
                }
                ++lineNum;
            }
        }

        flightMap.set(FXCollections.observableMap(imported));
    }

    /**
     * Gets landing flights.
     * @return A Property that contains a sorted list of the key-value pairs of landing flights.
     */
    public SimpleListProperty<Map.Entry<String, Flight>> getLandingFlights() {
        SimpleListProperty<Map.Entry<String, Flight>> prop = new SimpleListProperty<>();

        prop.bind(Bindings.createObjectBinding(() -> {
            ObservableList<Map.Entry<String, Flight>> base = FXCollections.observableArrayList(
                    (Map.Entry<String, Flight> entry) -> new Observable[] {entry.getValue().statusProperty()});
            FilteredList<Map.Entry<String, Flight>> filtered = new FilteredList<>(base,
                    f -> f.getValue().statusProperty().get() == FlightStatus.LANDING);
            base.addAll(flightList.get());
            return new SortedList<>(filtered);
        }, flightList));

        return prop;
    }

    /**
     * Gets parked flights whose scheduled departure time has passed.
     * @return A Property that contains a sorted list of the key-value pairs of delayed flights.
     */
    public SimpleListProperty<Map.Entry<String, Flight>> getDelayedFlights() {
        SimpleListProperty<Map.Entry<String, Flight>> prop = new SimpleListProperty<>();

        prop.bind(Bindings.createObjectBinding(() -> {
            ObservableList<Map.Entry<String, Flight>> base = FXCollections.observableArrayList(
                    (Map.Entry<String, Flight> entry) -> new Observable[] {entry.getValue().parkedStatusProperty()});
            FilteredList<Map.Entry<String, Flight>> filtered = new FilteredList<>(base,
                    f -> f.getValue().parkedStatusProperty().get() == FlightParkedStatus.DELAYED);
            base.addAll(flightList.get());
            return new SortedList<>(filtered, MapEntry.ValueComparator(Flight.getStdComparator()));
        }, flightList));

        return prop;
    }

    /**
     * Gets flights with HOLDING status.
     * @return A Property that contains a sorted list of the key-value pairs of holding flights.
     */
    public SimpleListProperty<Map.Entry<String, Flight>> getHoldingFlights() {
        SimpleListProperty<Map.Entry<String, Flight>> prop = new SimpleListProperty<>();

        prop.bind(Bindings.createObjectBinding(() -> {
            ObservableList<Map.Entry<String, Flight>> base = FXCollections.observableArrayList(
                    (Map.Entry<String, Flight> entry) -> new Observable[] {entry.getValue().statusProperty()});
            FilteredList<Map.Entry<String, Flight>> filtered = new FilteredList<>(base,
                    f -> f.getValue().statusProperty().get() == FlightStatus.HOLDING);
            base.addAll(flightList.get());
            return new SortedList<>(filtered, MapEntry.ValueComparator(Flight.getLandingRequestTimeComparator()));
        }, flightList));

        return prop;
    }

    /**
     * Gets the next departures, ie. flights with scheduled departure within 10 minutes from now.
     * @return A Property that contains a sorted list of the key-value pairs of flights that are departing next.
     */
    public SimpleListProperty<Map.Entry<String, Flight>> getNextDepartures() {
        SimpleListProperty<Map.Entry<String, Flight>> prop = new SimpleListProperty<>();

        prop.bind(Bindings.createObjectBinding(() -> {
            ObservableList<Map.Entry<String, Flight>> base = FXCollections.observableArrayList(
                    (Map.Entry<String, Flight> entry) -> new Observable[] {entry.getValue().parkedStatusProperty()});
            FilteredList<Map.Entry<String, Flight>> filtered = new FilteredList<>(base,
                    f -> f.getValue().parkedStatusProperty().get() == FlightParkedStatus.NEXT_DEPARTURE);
            base.addAll(flightList.get());
            return new SortedList<>(filtered, MapEntry.ValueComparator(Flight.getStdComparator()));
        }, flightList));

        return prop;
    }
}
