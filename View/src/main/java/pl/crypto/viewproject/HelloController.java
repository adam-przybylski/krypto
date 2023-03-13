//Autorzy
//Jakub Pazio 242489
//Adam Przybylski 242506


package pl.crypto.viewproject;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button des;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onDesButtonClick() throws IOException {
        Stage stage = (Stage) des.getScene().getWindow();
        HelloApplication.switchScene(stage, "des-window.fxml");
    }
}