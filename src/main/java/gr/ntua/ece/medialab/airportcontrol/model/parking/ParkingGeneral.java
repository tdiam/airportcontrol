package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParkingGeneral extends ParkingBase {
    public static Set<AirportService> supportedServices = new HashSet<>(
            List.of(AirportService.REFUEL, AirportService.CLEAN));

    public ParkingGeneral(ParkingType type, String id, double costPerMinute) {
        super(type, id, costPerMinute);
    }

    public static boolean isAllowedFlightType(FlightType flightType) {
        // Allow all
        return true;
    }

    public static boolean isAllowedPlaneType(PlaneType planeType) {
        // Allow all
        return true;
    }

    public static int maxStayMinutes() {
        return 4 * 60;
    }
}
