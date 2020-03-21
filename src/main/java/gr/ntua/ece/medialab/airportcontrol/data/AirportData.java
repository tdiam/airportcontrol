package gr.ntua.ece.medialab.airportcontrol.data;

import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightStatus;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingType;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Airport data controller. Data concerns the airport parking spots.
 */
public class AirportData {
    private Data root;

    /**
     * Creates a new instance of the airport data controller.
     * @param root Reference to root controller.
     */
    AirportData(Data root) {
        this.root = root;
    }

    private SimpleObjectProperty<ObservableMap<String, ParkingBase>> parkings = new SimpleObjectProperty<>(
            FXCollections.observableHashMap());

    private SimpleDoubleProperty grossTotal = new SimpleDoubleProperty(0);

    /**
     * Gets parking spots.
     * @return Property that stores an observable map of parking spots with id-{@link ParkingBase} as key-value pairs.
     */
    public SimpleObjectProperty<ObservableMap<String, ParkingBase>> parkingsProperty() {
        return parkings;
    }

    /**
     * Gets gross total.
     * @return Property that stores the gross total since start as a double value.
     */
    public SimpleDoubleProperty grossTotalProperty() {
        return grossTotal;
    }

    /**
     * Gets path of scenario file.
     * @param scenarioId Scenario ID given as a string.
     * @return Absolute path to input file for this scenario's airport data.
     */
    private String airportScenarioToFile(String scenarioId) {
        return new StringBuilder().append(root.getScenarioDirPath()).append("/")
                .append("airport_").append(scenarioId).append(".txt")
                .toString();
    }

    /**
     * Creates {@link ParkingBase} instance from array of values as parsed from the scenario file.
     * @param values Array of strings as parsed from the scenario file.
     * @param idx Index of parking that will be used to create a unique ID.
     * @return An instance of a subclass of {@link ParkingBase}, as determined by the parking type.
     * @throws NumberFormatException If a string of numeric data is malformed.
     */
    private ParkingBase arrayToParking(String[] values, int idx) throws NumberFormatException {
        int parkingTypeIdx = Integer.parseInt(values[0].trim());
        double costPerMinute = Double.parseDouble(values[2].trim());
        // id = parking type letter + increment, e.g. G1, G2, G3...
        String id = new StringBuilder().append(values[3].trim()).append(idx + 1).toString();

        // Use factory
        return ParkingType.createParking(parkingTypeIdx, id, costPerMinute);
    }

    /**
     * Given a scenario ID, it parses the input for the airport and stores the processed data.
     * @param scenarioId String that identifies the input file with the scenario data.
     * @throws IOException If reading the file fails.
     */
    public void importAirport(String scenarioId) throws IOException {
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
                        imported.put(parking.idProperty().get(), parking);
                    }
                } catch (NumberFormatException e) {
                    String msg = new StringBuilder().append(file).append(":").append(lineNum)
                            .append(" could not be parsed").toString();
                    System.err.println(msg);
                }
                ++lineNum;
            }
        }

        parkings.set(FXCollections.observableMap(imported));
    }

    /**
     * Handles landing requests for flights.
     *
     * Searches parking for a requesting flight that meets the requirements.
     * If found, the flight starts landing and the parking is assigned to it.
     * Otherwise, the flight's status is set to HOLDING.
     *
     * @param flight Flight instance.
     */
    public void requestLanding(Flight flight) {
        boolean accepted = false;
        SimpleIntegerProperty timeProp = root.timeData().minutesSinceStartProperty();
        int now = timeProp.get();
        flight.landingRequestTimeProperty().set(now);
        for (ParkingBase parking : parkings.get().values()) {
            if (parking.isGoodForFlight(flight, now)) {
                parking.parkedFlightProperty().set(flight);
                flight.parkingProperty().set(parking);
                flight.statusProperty().set(FlightStatus.LANDING);
                timeProp.addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> obs, Number oldValue, Number newValue) {
                        if ((int)newValue >= now + flight.getLandingTime()) {
                            flight.statusProperty().set(FlightStatus.PARKED);
                            flight.parkedTimeProperty().set(now);
                            timeProp.removeListener(this);
                        }
                    }
                });
                accepted = true;
                break;
            }
        }

        if (!accepted) {
            flight.statusProperty().set(FlightStatus.HOLDING);
        }
    }

    /**
     * Handles takeoffs.
     *
     * Checks if flight can takeoff and calculates the charge.
     *
     * @param flight Flight instance.
     * @return true if takeoff was successful, otherwise false.
     */
    public boolean takeoff(Flight flight) {
        if (flight.statusProperty().get() != FlightStatus.PARKED) {
            // If not parked
            // Data consistency is assumed so we don't check if flight.parking is null
            return false;
        }

        int now = root.timeData().minutesSinceStartProperty().get();
        // Time since parked
        int duration = now - flight.parkedTimeProperty().get();

        double costPerMinute = flight.parkingProperty().get().costPerMinuteProperty().get();
        double totalCost = duration * costPerMinute;
        if (now > flight.stdProperty().get()) { // delayed
            totalCost *= 2;
        }
        // Add services' cost
        totalCost *= 1 + flight.getServicesTotalCoef();

        if (now <= flight.stdProperty().get() - 25) { // 25 minutes before std
            totalCost *= 0.8; // 20% reduction
        } else if (now <= flight.stdProperty().get() - 10) { // 10 minutes before std
            totalCost *= 0.9; // 10% reduction
        }

        // Update gross total
        grossTotal.set(grossTotal.get() + totalCost);
        // Remove flight
        flight.parkingProperty().get().parkedFlightProperty().set(null);
        root.flightData().flightsProperty().get().remove(flight.idProperty().get());
        return true;
    }
}
