package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

public class ParkingZoneC extends ParkingBase {
    public ParkingZoneC(ParkingType type, String id, double costPerMinute) {
        super(type, id, costPerMinute, null, AirportService.ALL);
    }

    public boolean isAllowedFlightType(FlightType flightType) {
        // Allow all
        return true;
    }

    public boolean isAllowedPlaneType(PlaneType planeType) {
        return planeType == PlaneType.SINGLE_ENGINE;
    }

    public int maxStayMinutes() {
        return 3 * 60;
    }
}
