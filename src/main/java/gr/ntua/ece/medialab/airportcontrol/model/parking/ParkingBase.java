package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

import java.util.Set;

public class ParkingBase {
    public static Set<AirportService> supportedServices;

    private ParkingType type;
    public String id;
    public ParkingStatus status;
    public double costPerMinute;
    public Flight parkedFlight;

    public ParkingBase(ParkingType type, String id, double costPerMinute) {
        this.type = type;
        this.id = id;
        this.status = ParkingStatus.AVAILABLE;
        this.costPerMinute = costPerMinute;
    }

    public ParkingType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ParkingStatus getStatus() {
        return status;
    }

    public void setStatus(ParkingStatus status) {
        this.status = status;
    }

    public double getCostPerMinute() {
        return costPerMinute;
    }

    public void setCostPerMinute(double costPerMinute) {
        this.costPerMinute = costPerMinute;
    }

    public Flight getParkedFlight() {
        return parkedFlight;
    }

    public void setParkedFlight(Flight parkedFlight) {
        this.parkedFlight = parkedFlight;
    }

    public static boolean isAllowedFlightType(FlightType flightType) {
        throw new UnsupportedOperationException();
    }

    public static boolean isAllowedPlaneType(PlaneType planeType) {
        throw new UnsupportedOperationException();
    }

    public static boolean hasServices(Set<AirportService> services) {
        return supportedServices.containsAll(services);
    }

    public static int maxStayMinutes() {
        throw new UnsupportedOperationException();
    }
}
