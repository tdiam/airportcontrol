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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
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

        SimpleObjectProperty<ObservableMap<String, Flight>> flights = data.flightData().flightMapProperty();

        setDisabledForm(flights.getValue().isEmpty());
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

    public void clearAll() {
        idField.clear();
        clearServicesField();
    }

    private Flight validate() throws Errors.FlightFormValidationError {
        Flight flight = data.flightData().flightMapProperty().get().get(idField.getText());

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

            FlightStatus newStatus = flight.statusProperty().get();
            if (newStatus == FlightStatus.LANDING) {
                showLandingDialog(flight);
            } else if (newStatus == FlightStatus.HOLDING) {
                showHoldingDialog(flight);
            }
            clearAll();
        } catch (Errors.FlightFormValidationError e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void showLandingDialog(Flight flight) {
        String id = flight.idProperty().get();
        String title = bundle.getString("flight_form.dialog.title");
        String message = String.format(bundle.getString("flight_form.dialog.landing_message"), id,
                flight.parkingProperty().get().idProperty().get());
        showDialog(title, message);
    }

    private void showHoldingDialog(Flight flight) {
        String id = flight.idProperty().get();
        String title = bundle.getString("flight_form.dialog.title");
        String message = String.format(bundle.getString("flight_form.dialog.holding_message"), id);
        showDialog(title, message);
    }

    private void showErrorDialog(String message) {
        String title = bundle.getString("flight_form.dialog.error.title");
        showDialog(title, message);
    }

    private void showDialog(String title, String message) {
        int DIALOG_WIDTH = 320;
        Label label = new Label(message);
        label.setPadding(new Insets(15));
        label.setMaxWidth(DIALOG_WIDTH);
        label.setWrapText(true);
        PopupDialog dialog = new PopupDialog(title, label);
        Stage stage = dialog.getStage();
        stage.setWidth(DIALOG_WIDTH);
        stage.setMinHeight(60);
        dialog.show();
        stage.centerOnScreen();
    }
}
