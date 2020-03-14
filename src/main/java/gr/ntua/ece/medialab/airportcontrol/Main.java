package gr.ntua.ece.medialab.airportcontrol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {
    private ResourceBundle bundle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadRb();

        Parent root = loadView();
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.setTitle(bundle.getString("title"));
        primaryStage.show();
    }

    private void loadRb() {
        bundle = ResourceBundle.getBundle("gr.ntua.ece.medialab.airportcontrol.bundle");
    }

    private Parent loadView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(bundle);
            return loader.load(getClass().getResource("main.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
