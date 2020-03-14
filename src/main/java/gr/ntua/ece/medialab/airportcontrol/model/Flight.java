package gr.ntua.ece.medialab.airportcontrol.model;

import java.util.Set;

public class Flight {
    public String id;
    public String city;
    public FlightType flightType;
    public FlightStatus status;
    public PlaneType planeType;
    public Set<AirportService> extraServices;
    public int std;

    public Flight(String id, String city, FlightType flightType, FlightStatus status, PlaneType planeType,
            Set<AirportService> extraServices, int std) {
        this.id = id;
        this.city = city;
        this.flightType = flightType;
        this.status = status;
        this.planeType = planeType;
        this.extraServices = extraServices;
        this.std = std;
    }
}
