package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

import java.util.Set;

public class ParkingLongDuration extends ParkingBase {
    public static Set<AirportService> supportedServices = AirportService.ALL;

    public ParkingLongDuration(ParkingType type, String id, double costPerMinute) {
        super(type, id, costPerMinute);
    }

    public static boolean isAllowedFlightType(FlightType flightType) {
        // Allow all
        return flightType == FlightType.CARGO || flightType == FlightType.PRIVATE;
    }

    public static boolean isAllowedPlaneType(PlaneType planeType) {
        // Allow all
        return true;
    }

    public static int maxStayMinutes() {
        return 10 * 60;
    }
}
