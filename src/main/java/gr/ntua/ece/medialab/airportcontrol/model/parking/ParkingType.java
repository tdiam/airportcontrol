package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

import java.util.HashMap;

public enum ParkingType {
    GATE(1),
    CARGO(2),
    ZONE_A(3),
    ZONE_B(4),
    ZONE_C(5),
    GENERAL(6),
    LONG_DURATION(7);

    private int value;
    private static HashMap<Integer, ParkingType> map = new HashMap<>();

    ParkingType(int value) {
        this.value = value;
    }

    static {
        for (ParkingType parkingType : ParkingType.values()) {
            map.put(parkingType.value, parkingType);
        }
    }

    public static ParkingType valueOf(int parkingTypeIdx) {
        return map.get(parkingTypeIdx);
    }

    public static ParkingBase createParking(int parkingTypeIdx, String id, double costPerMinute) {
        ParkingType parkingType = map.get(parkingTypeIdx);
        switch (parkingType) {
        case GATE:
            return new ParkingGate(GATE, id, costPerMinute);
        case CARGO:
            return new ParkingCargo(CARGO, id, costPerMinute);
        case ZONE_A:
            return new ParkingZoneA(ZONE_A, id, costPerMinute);
        case ZONE_B:
            return new ParkingZoneB(ZONE_B, id, costPerMinute);
        case ZONE_C:
            return new ParkingZoneC(ZONE_C, id, costPerMinute);
        case GENERAL:
            return new ParkingGeneral(GENERAL, id, costPerMinute);
        case LONG_DURATION:
            return new ParkingLongDuration(LONG_DURATION, id, costPerMinute);
        default:
            throw new RuntimeException("parking type was not handled");
        }
    }
}
