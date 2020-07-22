/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 *
 * @author ThanhTung
 */
public class ConfirmationDialog {
    
    public static Optional<ButtonType> showConfirmationDialog(String headerText, String message){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}
