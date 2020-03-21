package gr.ntua.ece.medialab.airportcontrol.model;

import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Set;

public class Flight {
    private SimpleStringProperty id = new SimpleStringProperty();
    private SimpleStringProperty city = new SimpleStringProperty();
    private SimpleObjectProperty<FlightType> flightType = new SimpleObjectProperty<>();
    private SimpleObjectProperty<FlightStatus> status = new SimpleObjectProperty<>();
    private SimpleObjectProperty<ParkingBase> parking = new SimpleObjectProperty<>();
    private SimpleObjectProperty<PlaneType> planeType = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Set<AirportService>> extraServices = new SimpleObjectProperty<>();
    private SimpleIntegerProperty landingRequestTime = new SimpleIntegerProperty();
    private SimpleIntegerProperty parkedTime = new SimpleIntegerProperty();
    private SimpleIntegerProperty std = new SimpleIntegerProperty();

    public Flight() {
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public SimpleObjectProperty<FlightType> flightTypeProperty() {
        return flightType;
    }

    public SimpleObjectProperty<FlightStatus> statusProperty() {
        return status;
    }

    public SimpleObjectProperty<ParkingBase> parkingProperty() {
        return parking;
    }

    public SimpleObjectProperty<PlaneType> planeTypeProperty() {
        return planeType;
    }

    public SimpleObjectProperty<Set<AirportService>> extraServicesProperty() {
        return extraServices;
    }

    public SimpleIntegerProperty landingRequestTimeProperty() {
        return landingRequestTime;
    }

    public SimpleIntegerProperty parkedTimeProperty() {
        return parkedTime;
    }

    public SimpleIntegerProperty stdProperty() {
        return std;
    }

    public int getLandingTime() {
        return PlaneType.landingTimeOf(planeType.get());
    }

    public double getServicesTotalCoef() {
        return extraServices.get().stream().mapToDouble(AirportService::coefOf).sum();
    }
}
