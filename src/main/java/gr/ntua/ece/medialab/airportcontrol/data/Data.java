package gr.ntua.ece.medialab.airportcontrol.data;

import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.parking.ParkingBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.File;
import java.io.IOException;

public class Data implements FlightData, AirportData {
    private static Data instance;

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public String getScenarioDirPath() {
        try {
            return new File("./medialab/").getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ObservableMap<String, Flight> flights = FXCollections.observableHashMap();
    private ObservableMap<String, ParkingBase> parkings = FXCollections.observableHashMap();

    @Override
    public ObservableMap<String, Flight> getFlights() {
        return flights;
    }

    @Override
    public void setFlights(ObservableMap<String, Flight> flights) {
        this.flights = flights;
    }

    @Override
    public ObservableMap<String, ParkingBase> getParkings() {
        return parkings;
    }

    @Override
    public void setParkings(ObservableMap<String, ParkingBase> parkings) {
        this.parkings = parkings;
    }
}
