package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

import java.util.HashSet;
import java.util.List;

public class ParkingGeneral extends ParkingBase {
    public ParkingGeneral(ParkingType type, String id, double costPerMinute) {
        super(type, id, costPerMinute, null, new HashSet<>(
                List.of(AirportService.REFUEL, AirportService.CLEAN)));
    }

    public boolean isAllowedFlightType(FlightType flightType) {
        // Allow all
        return true;
    }

    public boolean isAllowedPlaneType(PlaneType planeType) {
        // Allow all
        return true;
    }

    public int maxStayMinutes() {
        return 4 * 60;
    }
}
