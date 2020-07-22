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
import pojos.Users;

/**
 *
 * @author ThanhTung
 */
public class RegisterDialog {

    /**
     * xuất hộp thoại login
     *
     * @return
     */
    public static Optional<Users> showLoginDialog() {
        // Tạo loginDialog
        Dialog<Users> loginDialog = new Dialog<>();
        loginDialog.setTitle("Hộp thoại đăng ký tài khoản");
        loginDialog.setResizable(true);
        loginDialog.setHeaderText("Nhập các thông tin để đăng ký tài khoản");

        //label, textField
        Label fullNameLabel = new Label("Tên đầy đủ: ");
        Label usernameLabel = new Label("Tên tài khoản: ");
        Label passwordLabel = new Label("Mật khẩu: ");
        Label emailLabel = new Label("Email: ");
        TextField fullNameTextField = new TextField();
        TextField usernameTextField = new TextField();
        TextField passwordTextField = new TextField();
        TextField emailTextField = new TextField();

        //tạo gridPane và add labels, textFields
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 35, 20, 35));
        grid.add(fullNameLabel, 1, 1); // col=1, row=1
        grid.add(fullNameTextField, 2, 1);
        grid.add(usernameLabel, 1, 2); // col=1, row=2
        grid.add(usernameTextField, 2, 2);
        grid.add(passwordLabel, 1, 3); // col=1, row=3
        grid.add(passwordTextField, 2, 3);
        grid.add(emailLabel, 1, 4); // col=1, row=4
        grid.add(emailTextField, 2, 4);
        
        loginDialog.getDialogPane().setContent(grid);

        //add button đăng nhập
        ButtonType buttonTypeOk = new ButtonType("Đăng ký", ButtonBar.ButtonData.OK_DONE);
        loginDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        //sau khi nhập sẽ trả giá trị username và password
        loginDialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                Users user = new Users();
                user.setFullName(fullNameTextField.getText());
                user.setUsername(usernameTextField.getText());
                user.setPass(passwordTextField.getText());
                user.setEmail(emailTextField.getText());

                return user;
            }
            return null;
        });

        // Show loginDialog
        Optional<Users> result = loginDialog.showAndWait();
        return result;
    }
}
