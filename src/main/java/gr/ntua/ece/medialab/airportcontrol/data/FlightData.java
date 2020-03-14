package gr.ntua.ece.medialab.airportcontrol.data;

import gr.ntua.ece.medialab.airportcontrol.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public interface FlightData {
    String getScenarioDirPath();
    ObservableMap<String, Flight> getFlights();
    void setFlights(ObservableMap<String, Flight> flights);

    private String flightScenarioToFile(String scenarioId) {
        return new StringBuilder().append(getScenarioDirPath()).append("/")
                .append("setup_").append(scenarioId).append(".txt")
                .toString();
    }

    private Flight arrayToFlight(String[] values) throws NumberFormatException {
        String id = values[0].trim();
        String city = values[1].trim();
        FlightType flightType = FlightType.valueOf(Integer.parseInt(values[2].trim()));
        PlaneType planeType = PlaneType.valueOf(Integer.parseInt(values[3].trim()));
        int std = Integer.parseInt(values[4].trim());
        return new Flight(id, city, flightType, FlightStatus.HOLDING, planeType, new HashSet<>(), std);
    }

    default void importSetup(String scenarioId) throws IOException {
        String file = flightScenarioToFile(scenarioId);
        HashMap<String, Flight> imported = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    imported.put(values[0].trim(), arrayToFlight(values));
                } catch (NumberFormatException e) {
                    String msg = new StringBuilder().append(file).append(":").append(lineNum)
                            .append(" could not be parsed").toString();
                    System.err.println(msg);
                }
                ++lineNum;
            }
        }

        setFlights(FXCollections.observableMap(imported));
    }
}
