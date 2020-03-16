package gr.ntua.ece.medialab.airportcontrol.view;

import gr.ntua.ece.medialab.airportcontrol.data.Data;
import gr.ntua.ece.medialab.airportcontrol.util.PopupDialog;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Menu implements Initializable {
    private Data data;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        data = Data.getInstance();
    }

    @FXML
    void showLoadDialog(Event evt) {
        String title = bundle.getString("nav.load.title");
        Parent view = loadView("load_dialog.fxml");

        new PopupDialog(title, view).show();
    }

    @FXML
    void startTime() {
        data.timeData().start();
    }

    private Parent loadView(String viewFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewFile), bundle);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
