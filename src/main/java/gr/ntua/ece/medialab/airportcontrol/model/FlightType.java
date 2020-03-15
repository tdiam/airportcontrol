package gr.ntua.ece.medialab.airportcontrol.model;

import java.util.HashMap;

public enum FlightType {
    PASSENGER(1),
    CARGO(2),
    PRIVATE(3);

    private int value;
    private static HashMap map = new HashMap<>();

    FlightType(int value) {
        this.value = value;
    }

    static {
        for (FlightType flightType : FlightType.values()) {
            map.put(flightType.value, flightType);
        }
    }

    public static FlightType valueOf(int flightType) {
        return (FlightType) map.get(flightType);
    }
}
