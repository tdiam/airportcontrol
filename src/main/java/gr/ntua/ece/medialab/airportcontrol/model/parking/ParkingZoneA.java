package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

public class ParkingZoneA extends ParkingBase {
    public ParkingZoneA(ParkingType type, String id, double costPerMinute) {
        super(type, id, costPerMinute, null, AirportService.ALL);
    }

    public boolean isAllowedFlightType(FlightType flightType) {
        return flightType == FlightType.PASSENGER;
    }

    public boolean isAllowedPlaneType(PlaneType planeType) {
        return planeType == PlaneType.TURBOPROP || planeType == PlaneType.JET;
    }

    public int maxStayMinutes() {
        return 90;
    }
}
