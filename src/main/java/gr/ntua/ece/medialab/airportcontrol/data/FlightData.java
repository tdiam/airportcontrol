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

public class FlightData {
    private Data root;

    FlightData(Data root) {
        this.root = root;
    }

    private SimpleObjectProperty<ObservableMap<String, Flight>> flights = new SimpleObjectProperty<>(
            FXCollections.observableHashMap());

    public SimpleObjectProperty<ObservableMap<String, Flight>> flightsProperty() {
        return flights;
    }

    private String flightScenarioToFile(String scenarioId) {
        return new StringBuilder().append(root.getScenarioDirPath()).append("/")
                .append("setup_").append(scenarioId).append(".txt")
                .toString();
    }

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
     * Returns parked flights whose scheduled departure time has passed.
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
     * Returns flights with HOLDING status.
     */
    public Map<String, Flight> getHoldingFlights(ObservableMap<String, Flight> flights) {
        return flights.entrySet().stream()
                .filter(entry -> entry.getValue().getStatus() == FlightStatus.HOLDING)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Returns the next departures, ie. flights with scheduled departure within 10 minutes from now.
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
