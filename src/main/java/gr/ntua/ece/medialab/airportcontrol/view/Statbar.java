package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import gr.ntua.ece.medialab.airportcontrol.util.TimeUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Statbar implements Initializable {
    private Data data;

    @FXML
    private Label landingFlights;

    @FXML
    private Label availableParkings;

    @FXML
    private Label departingSoon;

    @FXML
    private Label grossTotal;

    @FXML
    private Label totalTime;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();

        bindLandingFlights();
        bindAvailableParkings();
        bindNextDepartures();
        bindGrossTotal();
        bindTotalTime();
    }

    private void bindLandingFlights() {
        ReadOnlyIntegerProperty prop = data.flightData().getLandingFlights().sizeProperty();
        landingFlights.textProperty().bind(Bindings.createStringBinding(() -> Integer.toString(prop.get()), prop));
    }

    private void bindAvailableParkings() {
        ReadOnlyIntegerProperty prop = data.airportData().getAvailableParkings().sizeProperty();
        availableParkings.textProperty().bind(Bindings.createStringBinding(() -> Integer.toString(prop.get()), prop));
    }

    private void bindNextDepartures() {
        ReadOnlyIntegerProperty prop = data.flightData().getNextDepartures().sizeProperty();
        departingSoon.textProperty().bind(Bindings.createStringBinding(() -> Integer.toString(prop.get()), prop));
    }

    private void bindGrossTotal() {
        SimpleDoubleProperty prop = data.airportData().grossTotalProperty();
        grossTotal.textProperty().bind(Bindings.createStringBinding(
            () -> String.format("%.2f", prop.get()),
            prop
        ));
    }

    private void bindTotalTime() {
        SimpleIntegerProperty prop = data.timeData().minutesSinceStartProperty();

        totalTime.textProperty().bind(
                Bindings.createStringBinding(() -> TimeUtil.minutesToHM(prop.get()), prop));
    }
}
