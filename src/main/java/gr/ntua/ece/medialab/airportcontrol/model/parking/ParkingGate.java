package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

import java.util.Set;

public class ParkingGate extends ParkingBase {
    public static Set<AirportService> supportedServices = AirportService.ALL;

    public ParkingGate(ParkingType type, String id, double costPerMinute) {
        super(type, id, costPerMinute);
    }

    public static boolean isAllowedFlightType(FlightType flightType) {
        return flightType == FlightType.PASSENGER;
    }

    public static boolean isAllowedPlaneType(PlaneType planeType) {
        return planeType == PlaneType.TURBOPROP || planeType == PlaneType.JET;
    }

    public static int maxStayMinutes() {
        return 45;
    }
}
