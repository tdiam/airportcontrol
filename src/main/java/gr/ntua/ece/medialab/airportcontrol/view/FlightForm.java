package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.AirportService;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightStatus;
import gr.ntua.ece.medialab.airportcontrol.util.Errors;
import gr.ntua.ece.medialab.airportcontrol.util.PopupDialog;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class FlightForm implements Initializable {
    private Data data;
    private ResourceBundle bundle;

    @FXML
    TextField idField;

    @FXML
    ListView<AirportService> servicesField;

    @FXML
    Button clearServicesBtn;

    @FXML
    Button submitBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();
        bundle = resources;
        populateServicesField();

        SimpleObjectProperty<ObservableMap<String, Flight>> flights = data.flightData().flightsProperty();

        setDisabledForm(true);
        flights.addListener((obs, oldValue, newValue) -> {
            setDisabledForm(newValue.isEmpty());
        });
    }

    private void setDisabledForm(boolean value) {
        List<Node> els = List.of(idField, servicesField, clearServicesBtn, submitBtn);
        for (Node el : els) {
            el.setDisable(value);
        }
    }

    private void populateServicesField() {
        servicesField.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AirportService> call(ListView<AirportService> lv) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(AirportService item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                        } else {
                            setText(bundle.getString("airport_service." + item.name()));
                        }
                    }
                };
            }
        });
        servicesField.getItems().addAll(AirportService.ALL);
        servicesField.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void clearServicesField() {
        servicesField.getSelectionModel().clearSelection();
    }

    private Flight validate() throws Errors.FlightFormValidationError {
        Flight flight = data.flightData().flightsProperty().get().get(idField.getText());

        if (flight == null) {
            throw new Errors.FlightFormValidationError("Flight not found");
        }

        if (flight.statusProperty().get() != FlightStatus.EN_ROUTE) {
            throw new Errors.FlightFormValidationError("Flight must be en route to request landing");
        }

        return flight;
    }

    @FXML
    public void submit() {
        try {
            Flight flight = validate();
            flight.extraServicesProperty().set(Set.copyOf(servicesField.getSelectionModel().getSelectedItems()));
            data.airportData().requestLanding(flight);
            //if (flight.getStatus()) Stage dialog = new PopupDialog("", null);
        } catch (Errors.FlightFormValidationError e) {
            System.err.println(e.getMessage());
        }
    }
}
