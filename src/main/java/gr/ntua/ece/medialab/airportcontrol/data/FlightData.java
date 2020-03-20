package gr.ntua.ece.medialab.airportcontrol.data;

import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightStatus;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Flight data controller.
 */
public class FlightData {
    private Data root;

    /**
     * Creates a new instance of the flight data controller.
     * @param root Reference to root controller.
     */
    FlightData(Data root) {
        this.root = root;
    }

    private SimpleObjectProperty<ObservableMap<String, Flight>> flights = new SimpleObjectProperty<>(
            FXCollections.observableHashMap());

    /**
     * Gets active flights.
     * @return Property that stores an observable map of active flights with id-{@link Flight} as key-value pairs.
     */
    public SimpleObjectProperty<ObservableMap<String, Flight>> flightsProperty() {
        return flights;
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
        flight.setId(values[0].trim());
        flight.setCity(values[1].trim());
        flight.setFlightType(FlightType.valueOf(Integer.parseInt(values[2].trim())));
        flight.setStatus(FlightStatus.EN_ROUTE);
        flight.setParking(null);
        flight.setPlaneType(PlaneType.valueOf(Integer.parseInt(values[3].trim())));
        flight.setStd(Integer.parseInt(values[4].trim()));
        flight.setExtraServices(new HashSet<>());
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
                    imported.put(flight.getId(), flight);
                } catch (NumberFormatException e) {
                    String msg = new StringBuilder().append(file).append(":").append(lineNum)
                            .append(" could not be parsed").toString();
                    System.err.println(msg);
                }
                ++lineNum;
            }
        }

        flights.set(FXCollections.observableMap(imported));
    }

    /**
     * Gets parked flights whose scheduled departure time has passed.
     * @param flights An observable map of all active flights with id-{@link Flight} as key-value pairs.
     * @return A plain Map of the delayed flights.
     */
    public Map<String, Flight> getDelayedFlights(ObservableMap<String, Flight> flights) {
        return flights.entrySet().stream().filter(entry -> {
            Flight flight = entry.getValue();
            // Not delayed if not parked
            if (flight.getStatus() != FlightStatus.PARKED) return false;

            // current time > scheduled time
            return root.timeData().minutesSinceStartProperty().get() > flight.getStd();
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Gets flights with HOLDING status.
     * @param flights An observable map of all active flights with id-{@link Flight} as key-value pairs.
     * @return A plain Map of the holding flights.
     */
    public Map<String, Flight> getHoldingFlights(ObservableMap<String, Flight> flights) {
        return flights.entrySet().stream()
                .filter(entry -> entry.getValue().getStatus() == FlightStatus.HOLDING)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Gets the next departures, ie. flights with scheduled departure within 10 minutes from now.
     * @param flights An observable map of all active flights with id-{@link Flight} as key-value pairs.
     * @return A plain Map of the flights that are departing next.
     */
    public Map<String, Flight> getNextDepartures(ObservableMap<String, Flight> flights) {
        // TODO: Make this a live list that updates with each clock tick.
        return flights.entrySet().stream().filter(entry -> {
            Flight flight = entry.getValue();
            // Not next departure if not parked
            if (flight.getStatus() != FlightStatus.PARKED) return false;

            // scheduled time <= 10 minutes from now
            return flight.getStd() <= root.timeData().minutesSinceStartProperty().get() + 10;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
