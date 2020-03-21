package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightStatus;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Statbar implements Initializable {
    private Data data;
    SimpleObjectProperty<ObservableMap<String, Flight>> flightsProperty;
    SimpleObjectProperty<ObservableMap<String, ParkingBase>> parkingsProperty;

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
        flightsProperty = data.flightData().flightsProperty();
        parkingsProperty = data.airportData().parkingsProperty();

        bindLandingFlights();
        bindAvailableParkings();
        bindNextDepartures();
        bindTotalTime();
    }

    private void bindLandingFlights() {
        updateLandingFlights();
        bindLandingFlights(flightsProperty.get());
        flightsProperty.addListener((obs, oldValue, newValue) -> {
            updateLandingFlights();
            bindLandingFlights(newValue);
        });
    }

    private void bindLandingFlights(ObservableMap<String, Flight> flights) {
        for (Flight flight : flights.values()) {
            bindLandingFlights(flight);
        }

        flights.addListener((MapChangeListener.Change<? extends String, ? extends Flight> change) -> {
            // if removed, ignore
            if (change.wasRemoved() && !change.wasAdded()) return;

            // new or updated entry
            bindLandingFlights(change.getValueAdded());
        });
    }

    private void bindLandingFlights(Flight flight) {
        flight.statusProperty().addListener((obs, oldValue, newValue) -> updateLandingFlights());
    }

    private void updateLandingFlights() {
        long num = flightsProperty.get().values().stream()
                        .filter(f -> f.statusProperty().get() == FlightStatus.LANDING)
                        .count();
        landingFlights.setText(Long.toString(num));
    }

    private void bindAvailableParkings() {
        updateAvailableParkings();
        bindAvailableParkings(parkingsProperty.get());
        parkingsProperty.addListener((obs, oldValue, newValue) -> {
            updateAvailableParkings();
            bindAvailableParkings(newValue);
        });
    }

    private void bindAvailableParkings(ObservableMap<String, ParkingBase> parkings) {
        for (ParkingBase parking : parkings.values()) {
            bindAvailableParkings(parking);
        }

        parkings.addListener((MapChangeListener.Change<? extends String, ? extends ParkingBase> change) -> {
            // if removed, ignore
            if (change.wasRemoved() && !change.wasAdded()) return;

            // new or updated entry
            bindAvailableParkings(change.getValueAdded());
        });
    }

    private void bindAvailableParkings(ParkingBase parking) {
        parking.isAvailableProperty().addListener((obs, oldValue, newValue) -> updateAvailableParkings());
    }

    private void updateAvailableParkings() {
        long num = parkingsProperty.get().values().stream()
                        .filter(p -> p.isAvailableProperty().get())
                        .count();
        availableParkings.setText(Long.toString(num));
    }

    private void bindNextDepartures() {
        updateNextDepartures();
        data.timeData().minutesSinceStartProperty().addListener((obs, oldValue, newValue) -> updateNextDepartures());
    }

    private void updateNextDepartures() {
        int num = data.flightData().getNextDepartures(flightsProperty.get()).size();
        departingSoon.setText(Integer.toString(num));
    }

    private void bindTotalTime() {
        SimpleIntegerProperty prop = data.timeData().minutesSinceStartProperty();

        totalTime.setText(data.timeData().minutesToHM(prop.get()));
        prop.addListener((obs, oldValue, newValue) -> {
            totalTime.setText(data.timeData().minutesToHM((int)newValue));
        });
    }
}
