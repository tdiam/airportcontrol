package gr.ntua.ece.medialab.airportcontrol.model;

import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;

import java.util.Set;

public class Flight {
    private String id;
    private String city;
    private FlightType flightType;
    private FlightStatus status;
    private ParkingBase parking;
    private int landingRequestTime;
    private PlaneType planeType;
    private Set<AirportService> extraServices;
    private int std;

    public Flight() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public FlightType getFlightType() {
        return flightType;
    }

    public void setFlightType(FlightType flightType) {
        this.flightType = flightType;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public ParkingBase getParking() {
        return parking;
    }

    public void setParking(ParkingBase parking) {
        this.parking = parking;
    }

    public int getLandingRequestTime() {
        return landingRequestTime;
    }

    public void setLandingRequestTime(int landingRequestTime) {
        this.landingRequestTime = landingRequestTime;
    }

    public PlaneType getPlaneType() {
        return planeType;
    }

    public void setPlaneType(PlaneType planeType) {
        this.planeType = planeType;
    }

    public Set<AirportService> getExtraServices() {
        return extraServices;
    }

    public void setExtraServices(Set<AirportService> extraServices) {
        this.extraServices = extraServices;
    }

    public int getStd() {
        return std;
    }

    public void setStd(int std) {
        this.std = std;
    }
}
