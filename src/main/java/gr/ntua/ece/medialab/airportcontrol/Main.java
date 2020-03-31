package gr.ntua.ece.medialab.airportcontrol;

import gr.ntua.ece.medialab.airportcontrol.util.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = loadView();
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.setTitle(R.get("title"));
        primaryStage.show();
    }

    private Parent loadView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main.fxml"), R.getBundle());
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
