package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightParkedStatus;
import gr.ntua.ece.medialab.airportcontrol.model.FlightStatus;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingType;
import gr.ntua.ece.medialab.airportcontrol.util.TimeUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ParkingItem implements Initializable {
    private Data data;
    private ResourceBundle bundle;
    private ParkingBase parking;

    @FXML
    private GridPane parkingItem;

    @FXML
    private Label flightId, flightCity, available, landing, flightParkedAtLabel, flightParkedAt, flightStdLabel,
            flightStd, parkingId, parkingType;

    @FXML
    private Button takeoffBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();
        bundle = resources;

        List.of(flightId, flightCity, available, landing, flightParkedAtLabel, flightParkedAt, flightStdLabel,
                flightStd, parkingId, parkingType).forEach(item -> item.managedProperty().bind(item.visibleProperty()));
    }

    void initItemData(ParkingBase parking) {
        this.parking = parking;
        bindData();
    }

    private void bindData() {
        handleBindFlightProperties(null, parking.parkedFlightProperty().getValue());
        parking.parkedFlightProperty().addListener((obs, oldValue, newValue) ->
                handleBindFlightProperties(oldValue, newValue));

        parkingId.textProperty().bind(parking.idProperty());
        parkingType.textProperty().bind(Bindings.createStringBinding(() -> {
            ParkingType type = parking.typeProperty().getValue();
            return bundle.getString("parking_type." + type.toString());
        }, parking.typeProperty()));

        onAvailableChange(parking.isAvailableProperty().get());
        parking.isAvailableProperty().addListener((obs, oldValue, newValue) -> onAvailableChange(newValue));
    }

    private void handleBindFlightProperties(Flight oldFlight, Flight newFlight) {
        if (oldFlight != null) {
            oldFlight.statusProperty().unbind();
            oldFlight.parkedStatusProperty().unbind();
        }
        if (newFlight == null) {
            flightId.textProperty().unbind();
            flightCity.textProperty().unbind();
            flightParkedAt.textProperty().unbind();
            flightStd.textProperty().unbind();
        } else {
            flightId.textProperty().bind(newFlight.idProperty());
            flightCity.textProperty().bind(newFlight.cityProperty());
            flightParkedAt.textProperty().bind(Bindings.createStringBinding(() -> {
                return TimeUtil.minutesToHM(newFlight.parkedTimeProperty().getValue());
            }, newFlight.parkedTimeProperty()));
            flightStd.textProperty().bind(Bindings.createStringBinding(() -> {
                return TimeUtil.minutesToHM(newFlight.stdProperty().getValue());
            }, newFlight.stdProperty()));

            onFlightStatusChange(newFlight.statusProperty().get());
            newFlight.statusProperty().addListener((obs, oldValue, newValue) -> onFlightStatusChange(newValue));

            onFlightParkedStatusChange(newFlight.parkedStatusProperty().get());
            newFlight.parkedStatusProperty().addListener((obs, oldValue, newValue) ->
                    onFlightParkedStatusChange(newValue));
        }
    }

    private void onAvailableChange(boolean isAvailable) {
        if (isAvailable) {
            parkingItem.getStyleClass().remove("occupied");
            parkingItem.getStyleClass().remove("landing");
            parkingItem.getStyleClass().add("available");
            available.setVisible(true);
            List.of(flightId, flightCity, landing, flightParkedAtLabel, flightParkedAt, flightStdLabel, flightStd,
                    takeoffBtn).forEach(item -> item.setVisible(false));
        }
    }

    private void onFlightStatusChange(FlightStatus status) {
        if (status == FlightStatus.LANDING) {
            parkingItem.getStyleClass().remove("available");
            parkingItem.getStyleClass().remove("occupied");
            parkingItem.getStyleClass().add("landing");
            List.of(flightId, flightCity, landing, flightStdLabel, flightStd).forEach(item -> item.setVisible(true));
            List.of(available, flightParkedAtLabel, flightParkedAt, takeoffBtn).forEach(item -> item.setVisible(false));
        } else if (status == FlightStatus.PARKED) {
            parkingItem.getStyleClass().remove("available");
            parkingItem.getStyleClass().remove("landing");
            parkingItem.getStyleClass().add("occupied");
            List.of(available, landing).forEach(item -> item.setVisible(false));
            List.of(flightId, flightCity, flightParkedAtLabel, flightParkedAt, flightStdLabel, flightStd, takeoffBtn)
                    .forEach(item -> item.setVisible(true));
        }
    }

    private void onFlightParkedStatusChange(FlightParkedStatus status) {
        if (status == FlightParkedStatus.NORMAL) {
            parkingItem.getStyleClass().remove("flight-next");
            parkingItem.getStyleClass().remove("flight-delayed");
            parkingItem.getStyleClass().add("flight-normal");
        } else if (status == FlightParkedStatus.NEXT_DEPARTURE) {
            parkingItem.getStyleClass().remove("flight-normal");
            parkingItem.getStyleClass().remove("flight-delayed");
            parkingItem.getStyleClass().add("flight-next");
        } else if (status == FlightParkedStatus.DELAYED) {
            parkingItem.getStyleClass().remove("flight-normal");
            parkingItem.getStyleClass().remove("flight-next");
            parkingItem.getStyleClass().add("flight-delayed");
        }
    }

    @FXML
    public void takeoff() {
        // We assume UI consistency, ie. that parking.parkedFlight is not null
        data.airportData().takeoff(parking.parkedFlightProperty().get());
    }
}
