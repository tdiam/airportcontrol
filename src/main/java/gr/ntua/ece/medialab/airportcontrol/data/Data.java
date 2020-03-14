package gr.ntua.ece.medialab.airportcontrol.data;

import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.File;
import java.io.IOException;

public class Data implements FlightData {
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

    ObservableMap<String, Flight> flights = FXCollections.observableHashMap();

    @Override
    public ObservableMap<String, Flight> getFlights() {
        return flights;
    }

    @Override
    public void setFlights(ObservableMap<String, Flight> flights) {
        this.flights = flights;
    }
}
