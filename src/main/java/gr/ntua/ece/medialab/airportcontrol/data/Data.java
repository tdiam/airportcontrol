package gr.ntua.ece.medialab.airportcontrol.data;

import java.io.File;
import java.io.IOException;

public class Data {
    private static Data instance;
    private static FlightData flightInstance;
    private static AirportData airportInstance;

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
            flightInstance = new FlightData(instance);
            airportInstance = new AirportData(instance);
        }
        return instance;
    }

    public String getScenarioDirPath() {
        try {
            return new File("./medialab/").getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public FlightData flightData() {
        return flightInstance;
    }

    public AirportData airportData() {
        return airportInstance;
    }
}
