package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

import java.util.Set;

public class ParkingZoneC extends ParkingBase {
    public static Set<AirportService> supportedServices = AirportService.ALL;

    public ParkingZoneC(ParkingType type, String id, double costPerMinute) {
        super(type, id, costPerMinute);
    }

    public static boolean isAllowedFlightType(FlightType flightType) {
        // Allow all
        return true;
    }

    public static boolean isAllowedPlaneType(PlaneType planeType) {
        return planeType == PlaneType.SINGLE_ENGINE;
    }

    public static int maxStayMinutes() {
        return 3 * 60;
    }
}
