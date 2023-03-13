//Autorzy
//Jakub Pazio 242489
//Adam Przybylski 242506

package pl.crypto.viewproject;

import javafx.scene.control.Alert;

public class CustomAlert {
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert newAlert = new Alert(alertType);
        newAlert.setTitle(title);
        newAlert.setContentText(message);
        newAlert.setHeaderText(null);
        newAlert.showAndWait();
    }
}
