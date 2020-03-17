package gr.ntua.ece.medialab.airportcontrol.model;

import java.util.HashMap;

public enum PlaneType {
    SINGLE_ENGINE(1),
    TURBOPROP(2),
    JET(3);

    private int value;
    private static HashMap<Integer, PlaneType> map = new HashMap<>();

    PlaneType(int value) {
        this.value = value;
    }

    static {
        for (PlaneType planeType : PlaneType.values()) {
            map.put(planeType.value, planeType);
        }
    }

    public static PlaneType valueOf(int planeType) {
        return map.get(planeType);
    }

    private static HashMap<PlaneType, Integer> landingTimeMap = new HashMap<>() {{
        put(SINGLE_ENGINE, 6);
        put(TURBOPROP, 4);
        put(JET, 2);
    }};

    public static int landingTimeOf(PlaneType value) {
        return (int) landingTimeMap.get(value);
    }
}
