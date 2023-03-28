//Autorzy
//Jakub Pazio 242489
//Adam Przybylski 242506

package pl.crypto.viewproject;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.crypto.model.Schnorr;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cryptography Project");
        stage.setScene(scene);
        stage.show();
    }

    public static void switchScene(Stage stage, String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        stage.getScene().setRoot(fxmlLoader.load());
    }

    public static void main(String[] args) {
        launch();
    }
}