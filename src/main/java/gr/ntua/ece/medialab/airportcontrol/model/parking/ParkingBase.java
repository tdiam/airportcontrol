package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;

import java.util.Set;

public abstract class ParkingBase {
    private ParkingType type;
    private String id;
    private double costPerMinute;
    private Flight parkedFlight;
    private Set<AirportService> supportedServices;

    public ParkingBase(ParkingType type, String id, double costPerMinute, Flight parkedFlight,
            Set<AirportService> supportedServices) {
        this.type = type;
        this.id = id;
        this.costPerMinute = costPerMinute;
        this.parkedFlight = parkedFlight;
        this.supportedServices = supportedServices;
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

    public boolean isAvailable() {
        return parkedFlight == null;
    }

    public boolean hasServices(Set<AirportService> services) {
        return supportedServices.containsAll(services);
    }

    public boolean isGoodForFlight(Flight flight, int landingRequestTime) {
        return isAvailable()
                && isAllowedFlightType(flight.flightTypeProperty().get())
                && isAllowedPlaneType(flight.planeTypeProperty().get())
                && hasServices(flight.extraServicesProperty().get())
                && maxStayMinutes() >= flight.stdProperty().get() - landingRequestTime;
    }

    public abstract boolean isAllowedFlightType(FlightType flightType);

    public abstract boolean isAllowedPlaneType(PlaneType planeType);

    public abstract int maxStayMinutes();
}
