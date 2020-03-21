package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

public class ParkingLongDuration extends ParkingBase {
    public ParkingLongDuration(ParkingType type, String id, double costPerMinute) {
        super(type, id, costPerMinute, null, AirportService.ALL);
    }

    public boolean isAllowedFlightType(FlightType flightType) {
        // Allow all
        return flightType == FlightType.CARGO || flightType == FlightType.PRIVATE;
    }

    public boolean isAllowedPlaneType(PlaneType planeType) {
        // Allow all
        return true;
    }

    public int maxStayMinutes() {
        return 10 * 60;
    }
}
