package gr.ntua.ece.medialab.airportcontrol.model;

import java.util.HashMap;

public enum AirportService {
    REFUEL,
    CLEAN,
    PASSENGER_TRANSPORT,
    LOADING;

    private static HashMap coefMap = new HashMap<AirportService, Double>() {{
        put(REFUEL, 0.25);
        put(CLEAN, 0.02);
        put(PASSENGER_TRANSPORT, 0.02);
        put(LOADING, 0.05);
    }};

    public static double coefOf(AirportService value) {
        return (double) coefMap.get(value);
    }
}
