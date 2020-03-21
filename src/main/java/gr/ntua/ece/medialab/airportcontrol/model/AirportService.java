package gr.ntua.ece.medialab.airportcontrol.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public enum AirportService {
    REFUEL,
    CLEAN,
    PASSENGER_TRANSPORT,
    LOADING;

    private static HashMap<AirportService, Double> coefMap = new HashMap<>() {{
        put(REFUEL, 0.25);
        put(CLEAN, 0.02);
        put(PASSENGER_TRANSPORT, 0.02);
        put(LOADING, 0.05);
    }};

    public static double coefOf(AirportService value) {
        return coefMap.get(value);
    }
    public static final HashSet<AirportService> ALL = new HashSet<>();

    static {
        ALL.addAll(List.of(REFUEL, CLEAN, PASSENGER_TRANSPORT, LOADING));
    }
}
