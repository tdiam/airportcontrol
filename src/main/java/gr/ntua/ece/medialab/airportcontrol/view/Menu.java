package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.util.PopupDialog;
import gr.ntua.ece.medialab.airportcontrol.util.R;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Menu implements Initializable {
    private Data data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = Data.getInstance();
    }

    @FXML
    void startTime() {
        data.timeData().start();
    }

    @FXML
    void showLoadDialog(Event evt) {
        String title = R.get("nav.load.title");
        Parent view = loadView("load_dialog.fxml");

        PopupDialog dialog = new PopupDialog(title, view);
        dialog.getStage().setWidth(320);
        dialog.show();
        dialog.getStage().centerOnScreen();
    }

    @FXML
    void showGatesDetails(Event evt) {
        showDetailsView(R.get("details.gates.title"), loadView("details/gates.fxml"));
    }

    @FXML
    void showFlightsDetails(Event evt) {
        showDetailsView(R.get("details.flights.title"), loadView("details/flights.fxml"));
    }

    @FXML
    void showDelayedDetails(Event evt) {
        showDetailsView(R.get("details.delayed.title"), loadView("details/delayed.fxml"));
    }

    @FXML
    void showHoldingDetails(Event evt) {
        showDetailsView(R.get("details.holding.title"), loadView("details/holding.fxml"));
    }

    @FXML
    void showNextDeparturesDetails(Event evt) {
        showDetailsView(R.get("details.next_departures.title"), loadView("details/next_departures.fxml"));
    }

    void showDetailsView(String title, Parent view) {
        PopupDialog dialog = new PopupDialog(title, view, false);
        Stage stage = dialog.getStage();
        stage.setWidth(720);
        stage.setMinHeight(480);
        dialog.show();
        stage.centerOnScreen();
    }

    @FXML
    void exit() {
        Platform.exit();
    }

    private Parent loadView(String viewFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewFile), R.getBundle());
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
