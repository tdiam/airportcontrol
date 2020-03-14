package gr.ntua.ece.medialab.airportcontrol.model;

import java.util.HashMap;

public enum PlaneType {
    SINGLE_ENGINE(1),
    TURBOPROP(2),
    JET(3);

    private int value;
    private static HashMap map = new HashMap<>();

    PlaneType(int value) {
        this.value = value;
    }

    static {
        for (PlaneType planeType : PlaneType.values()) {
            map.put(planeType.value, planeType);
        }
    }

    public static PlaneType valueOf(int planeType) {
        return (PlaneType) map.get(planeType);
    }

    public int getValue() {
        return value;
    }
}
