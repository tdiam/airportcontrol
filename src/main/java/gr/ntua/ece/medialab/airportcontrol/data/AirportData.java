package gr.ntua.ece.medialab.airportcontrol.data;

import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightParkedStatus;
import gr.ntua.ece.medialab.airportcontrol.model.FlightStatus;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingType;
import gr.ntua.ece.medialab.airportcontrol.util.ObservableUtil;
import gr.ntua.ece.medialab.airportcontrol.util.R;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.util.List;
import java.util.Map;

/**
 * Airport data controller. Data concerns the airport parking spots.
 */
public class AirportData {
    private Data root;

    private SimpleObjectProperty<ObservableMap<String, ParkingBase>> parkingMap = new SimpleObjectProperty<>(
            FXCollections.observableHashMap());

    private SimpleListProperty<Map.Entry<String, ParkingBase>> parkingList = new SimpleListProperty<>();

    private SimpleDoubleProperty grossTotal = new SimpleDoubleProperty(0);

    /**
     * Creates a new instance of the airport data controller.
     * @param root Reference to root controller.
     */
    AirportData(Data root) {
        this.root = root;
        bindParkingList();
    }

    private void bindParkingList() {
        parkingList.bind(Bindings.createObjectBinding(
            () -> ObservableUtil.observableMapToList(parkingMap.get()).sorted(),
            parkingMap
        ));
    }

    /**
     * Gets parking spots as map.
     * @return Property that stores an observable map of parking spots with id-{@link ParkingBase} as key-value pairs.
     */
    public SimpleObjectProperty<ObservableMap<String, ParkingBase>> parkingMapProperty() {
        return parkingMap;
    }

    /**
     * Gets parking spots as sorted list.
     * @return Property that stores an observable list of key-value pairs of parking spots.
     */
    public SimpleListProperty<Map.Entry<String, ParkingBase>> getParkings() {
        return parkingList;
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
        String id = String.format("%s%03d", values[3].trim(), idx + 1);

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
                    root.statusData().setError(R.get("errors.parse_imported", file, lineNum));
                }
                ++lineNum;
            }
        }

        parkingMap.set(FXCollections.observableMap(imported));
    }

    /**
     * Gets available parking spots.
     * @return A Property that contains a sorted list of the key-value pairs of available parking spots.
     */
    public SimpleListProperty<Map.Entry<String, ParkingBase>> getAvailableParkings() {
        SimpleListProperty<Map.Entry<String, ParkingBase>> prop = new SimpleListProperty<>();

        prop.bind(Bindings.createObjectBinding(() -> {
            ObservableList<Map.Entry<String, ParkingBase>> base = FXCollections.observableArrayList(
                    (Map.Entry<String, ParkingBase> entry) -> new Observable[]{entry.getValue().isAvailableProperty()});
            FilteredList<Map.Entry<String, ParkingBase>> filtered = new FilteredList<>(base,
                    f -> f.getValue().isAvailableProperty().get());
            base.addAll(parkingList.get());
            return new SortedList<>(filtered);
        }, parkingList));

        return prop;
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
        SimpleIntegerProperty timeProp = root.timeData().timeProperty();
        int now = timeProp.get();
        flight.landingRequestTimeProperty().set(now);
        for (ParkingBase parking : parkingMap.get().values()) {
            if (parking.isGoodForFlight(flight, now)) {
                park(flight, parking);
                accepted = true;
                break;
            }
        }

        if (!accepted) {
            root.statusData().setStatus(R.get(
                "status.flight_status",
                flight.idProperty().get(),
                R.get("flight_status." + flight.statusProperty().get().toString()),
                R.get("flight_status." + FlightStatus.HOLDING.toString())
            ));
            flight.statusProperty().set(FlightStatus.HOLDING);
        }
    }

    /**
     * Park flight.
     *
     * @param flight Flight instance.
     * @param parking ParkingBase instance.
     */
    public void park(Flight flight, ParkingBase parking) {
        int now = root.timeData().timeProperty().get();

        parking.parkedFlightProperty().set(flight);
        flight.parkingProperty().set(parking);
        root.statusData().setStatus(R.get(
            "status.flight_status",
            flight.idProperty().get(),
            R.get("flight_status." + flight.statusProperty().get().toString()),
            R.get("flight_status." + FlightStatus.LANDING.toString())
        ));
        flight.statusProperty().set(FlightStatus.LANDING);

        int landWhen = now + flight.getLandingTime();
        int std = flight.stdProperty().get();

        // Schedule landing
        root.timeData().schedule(() -> {
            root.statusData().setStatus(R.get(
                "status.flight_status",
                flight.idProperty().get(),
                R.get("flight_status." + flight.statusProperty().get().toString()),
                R.get("flight_status." + FlightStatus.PARKED.toString())
            ));
            flight.statusProperty().set(FlightStatus.PARKED);
            flight.parkedTimeProperty().set(landWhen);
        }, landWhen);

        // If flight will land before std, schedule marking as next departure
        if (landWhen <= std) {
            root.timeData().schedule(() -> {
                flight.parkedStatusProperty().set(FlightParkedStatus.NEXT_DEPARTURE);
            }, std - 10, true);
        }

        // Schedule marking as delayed
        root.timeData().schedule(() -> {
            flight.parkedStatusProperty().set(FlightParkedStatus.DELAYED);
        }, Math.max(std + 1, landWhen));
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

        int now = root.timeData().timeProperty().get();
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
        root.flightData().flightMapProperty().get().remove(flight.idProperty().get());

        root.statusData().setStatus(R.get("status.takeoff", flight.idProperty().get(), totalCost));

        checkHolding(flight.parkingProperty().get());
        return true;
    }

    /**
     * Handles reallocation of parking to holding flights after a takeoff.
     */
    public void checkHolding(ParkingBase parking) {
        int now = root.timeData().timeProperty().get();

        List<Map.Entry<String, Flight>> holdingFlights = root.flightData().getHoldingFlights().get();
        for (Map.Entry<String, Flight> entry : holdingFlights) {
            Flight flight = entry.getValue();
            if (parking.isGoodForFlight(flight, now)) {
                park(flight, parking);
                break;
            }
        }
    }
}
