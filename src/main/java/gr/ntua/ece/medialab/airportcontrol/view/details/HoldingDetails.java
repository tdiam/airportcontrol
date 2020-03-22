package gr.ntua.ece.medialab.airportcontrol.view.details;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.model.Flight;
import gr.ntua.ece.medialab.airportcontrol.model.FlightType;
import gr.ntua.ece.medialab.airportcontrol.model.PlaneType;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class HoldingDetails implements Initializable {
    private ResourceBundle bundle;
    private Data data;

    @FXML
    private TableView<Map.Entry<String, Flight>> table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        data = Data.getInstance();

        bindFlights();
    }

    private void bindFlights() {
        TableColumn<Map.Entry<String, Flight>, String> idColumn = new TableColumn<>(
                bundle.getString("details.id_col.name"));
        idColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> df.getValue().getValue().idProperty().get(),
                    df.getValue().getValue().idProperty()
                ));

        TableColumn<Map.Entry<String, Flight>, String> flightTypeColumn = new TableColumn<>(
                bundle.getString("details.flight_type_col.name"));
        flightTypeColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        FlightType flightType = df.getValue().getValue().flightTypeProperty().get();
                        return bundle.getString("flight_type." + flightType.toString());
                    },
                    df.getValue().getValue().flightTypeProperty()
                ));

        TableColumn<Map.Entry<String, Flight>, String> planeTypeColumn = new TableColumn<>(
                bundle.getString("details.plane_type_col.name"));
        planeTypeColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        PlaneType planeType = df.getValue().getValue().planeTypeProperty().get();
                        return bundle.getString("plane_type." + planeType.toString());
                    },
                    df.getValue().getValue().planeTypeProperty()
                ));

        TableColumn<Map.Entry<String, Flight>, String> landingRequestTimeColumn = new TableColumn<>(
                bundle.getString("details.landing_request_col.name"));
        landingRequestTimeColumn.setCellValueFactory(df ->
                Bindings.createStringBinding(
                    () -> {
                        int landingRequestTime = df.getValue().getValue().landingRequestTimeProperty().get();
                        return data.timeData().minutesToHM(landingRequestTime);
                    },
                    df.getValue().getValue().landingRequestTimeProperty()
                ));

        table.getColumns().setAll(idColumn, flightTypeColumn, planeTypeColumn, landingRequestTimeColumn);

        table.itemsProperty().bind(data.flightData().getHoldingFlights());
    }
}
