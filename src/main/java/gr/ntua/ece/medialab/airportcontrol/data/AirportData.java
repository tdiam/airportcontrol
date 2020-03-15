package gr.ntua.ece.medialab.airportcontrol.data;

import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public interface AirportData {
    String getScenarioDirPath();
    ObservableMap<String, ParkingBase> getParkings();
    void setParkings(ObservableMap<String, ParkingBase> parkings);

    private String airportScenarioToFile(String scenarioId) {
        return new StringBuilder().append(getScenarioDirPath()).append("/")
                .append("airport_").append(scenarioId).append(".txt")
                .toString();
    }

    private ParkingBase arrayToParking(String[] values, int idx) throws NumberFormatException {
        int parkingTypeIdx = Integer.parseInt(values[0].trim());
        double costPerMinute = Double.parseDouble(values[2].trim());
        // id = parking type letter + increment, e.g. G1, G2, G3...
        String id = new StringBuilder().append(values[3].trim()).append(idx + 1).toString();

        // Use factory
        return ParkingType.createParking(parkingTypeIdx, id, costPerMinute);
    }

    default void importAirport(String scenarioId) throws IOException {
        String file = airportScenarioToFile(scenarioId);
        HashMap<String, ParkingBase> imported = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    // Number of spots
                    int n = Integer.parseInt(values[1].trim());
                    // Create n copies
                    for (int idx = 0; idx < n; ++idx) {
                        ParkingBase parking = arrayToParking(values, idx);
                        imported.put(parking.id, parking);
                    }
                } catch (NumberFormatException e) {
                    String msg = new StringBuilder().append(file).append(":").append(lineNum)
                            .append(" could not be parsed").toString();
                    System.err.println(msg);
                }
                ++lineNum;
            }
        }

        setParkings(FXCollections.observableMap(imported));
    }
}
