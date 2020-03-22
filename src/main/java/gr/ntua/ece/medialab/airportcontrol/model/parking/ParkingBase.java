package gr.ntua.ece.medialab.airportcontrol.model.parking;

import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Set;

public abstract class ParkingBase implements Comparable<ParkingBase> {
    private SimpleObjectProperty<ParkingType> type = new SimpleObjectProperty<>();
    private SimpleStringProperty id = new SimpleStringProperty();
    private SimpleDoubleProperty costPerMinute = new SimpleDoubleProperty();
    private SimpleObjectProperty<Flight> parkedFlight = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Set<AirportService>> supportedServices = new SimpleObjectProperty<>();
    private SimpleBooleanProperty isAvailable = new SimpleBooleanProperty();

    public ParkingBase(ParkingType type, String id, double costPerMinute, Flight parkedFlight,
            Set<AirportService> supportedServices) {
        this.type.set(type);
        this.id.set(id);
        this.costPerMinute.set(costPerMinute);
        this.parkedFlight.set(parkedFlight);
        this.supportedServices.set(supportedServices);
        this.isAvailable.bind(
                Bindings.createBooleanBinding(
                    () -> this.parkedFlight.get() == null,
                    this.parkedFlight
                ));
    }

    public SimpleObjectProperty<ParkingType> typeProperty() {
        return type;
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public SimpleDoubleProperty costPerMinuteProperty() {
        return costPerMinute;
    }

    public SimpleObjectProperty<Flight> parkedFlightProperty() {
        return parkedFlight;
    }

    public SimpleObjectProperty<Set<AirportService>> supportedServicesProperty() {
        return supportedServices;
    }

    public SimpleBooleanProperty isAvailableProperty() {
        return isAvailable;
    }

    public boolean hasServices(Set<AirportService> services) {
        return supportedServices.get().containsAll(services);
    }

    public boolean isGoodForFlight(Flight flight, int landingRequestTime) {
        return isAvailable.get()
                && isAllowedFlightType(flight.flightTypeProperty().get())
                && isAllowedPlaneType(flight.planeTypeProperty().get())
                && hasServices(flight.extraServicesProperty().get())
                && maxStayMinutes() >= flight.stdProperty().get() - landingRequestTime;
    }

    public abstract boolean isAllowedFlightType(FlightType flightType);

    public abstract boolean isAllowedPlaneType(PlaneType planeType);

    public abstract int maxStayMinutes();

    @Override
    public int compareTo(ParkingBase other) {
        return other.idProperty().get().compareTo(idProperty().get());
    }
}
