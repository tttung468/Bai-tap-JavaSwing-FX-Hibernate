/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import javafx.scene.control.Alert;

/**
 *
 * @author ThanhTung
 */
public class AlertDialog {
    /**
     * hiển thị hộp thoại thông báo với tin nhắn message
     *
     * @param message
     */
    public static void showAlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
