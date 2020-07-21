/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 *
 * @author ThanhTung
 */
public class LoginDialog {
    /**
     * xuất hộp thoại login
     * 
     * @return 
     */
    public static Optional<Pair<String, String>> showLoginDialog() {
        // Tạo loginDialog
        Dialog<Pair<String, String>> loginDialog = new Dialog<>();
        loginDialog.setTitle("Hộp thoại đăng nhập");
        loginDialog.setResizable(true);
        loginDialog.setGraphic(new ImageView(new Image("\\doan_quanlyhoinghi\\conferenceImage\\login.jpg")));
        
        //label, textField
        Label usernameLabel = new Label("Tên tài khoản: ");
        Label passwordLabel = new Label("Mật khẩu: ");
        TextField usernameTextField = new TextField();
        TextField passwordTextField = new TextField();
        
        //tạo gridPane và add labels, textFields
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 35, 20, 35));
        grid.add(usernameLabel, 1, 1); // col=1, row=1
        grid.add(usernameTextField, 2, 1);
        grid.add(passwordLabel, 1, 2); // col=1, row=2
        grid.add(passwordTextField, 2, 2);
        loginDialog.getDialogPane().setContent(grid);
        
        //add button đăng nhập
        ButtonType buttonTypeOk = new ButtonType("Đăng nhập", ButtonBar.ButtonData.OK_DONE);
        loginDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        
        //sau khi nhập sẽ trả giá trị username và password
        loginDialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                return new Pair<>(usernameTextField.getText(), passwordTextField.getText());
            }
            return null;
        });

        // Show loginDialog
        Optional<Pair<String, String>> result = loginDialog.showAndWait();
        return result;
    }
}
