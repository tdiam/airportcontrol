package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

public class ParkingZoneB extends ParkingBase {
    public ParkingZoneB(ParkingType type, String id, double costPerMinute) {
        super(type, id, costPerMinute, null, AirportService.ALL);
    }

    public boolean isAllowedFlightType(FlightType flightType) {
        // Allow all
        return true;
    }

    public boolean isAllowedPlaneType(PlaneType planeType) {
        return planeType == PlaneType.TURBOPROP || planeType == PlaneType.JET;
    }

    public int maxStayMinutes() {
        return 2 * 60;
    }
}
