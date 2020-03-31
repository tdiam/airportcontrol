package gr.ntua.ece.medialab.airportcontrol.data;

import java.io.File;
import java.io.IOException;

/**
 * Data controller.
 */
public class Data {
    private static Data instance;
    private static TimeData timeInstance;
    private static FlightData flightInstance;
    private static AirportData airportInstance;
    private static StatusData statusInstance;

    /**
     * Gets the singleton instance.
     * @return Singleton instance.
     */
    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
            timeInstance = new TimeData(instance);
            flightInstance = new FlightData(instance);
            airportInstance = new AirportData(instance);
            statusInstance = new StatusData(instance);
        }
        return instance;
    }

    /**
     * Gets the absolute path of the directory where the scenario input files are stored.
     * @return Path of the scenario directory as string.
     */
    public String getScenarioDirPath() {
        try {
            return new File("./medialab/").getCanonicalPath();
        } catch (IOException e) {
            statusData().setError("Scenario directory not found");
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the time data controller instance.
     * @return {@link TimeData} instance.
     */
    public TimeData timeData() {
        return timeInstance;
    }

    /**
     * Gets the flight data controller instance.
     * @return {@link FlightData} instance.
     */
    public FlightData flightData() {
        return flightInstance;
    }

    /**
     * Gets the airport data controller instance.
     * @return {@link AirportData} instance.
     */
    public AirportData airportData() {
        return airportInstance;
    }

    /**
     * Gets the status data controller instance.
     * @return {@link StatusData} instance.
     */
    public StatusData statusData() {
        return statusInstance;
    }
}
