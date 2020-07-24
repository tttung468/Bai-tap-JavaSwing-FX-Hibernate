/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import DAO.AdminsDAO;
import DAO.UsersDAO;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import pojos.Admins;
import pojos.Users;

/**
 * FXML Controller class
 *
 * @author ThanhTung
 */
public class AdminSceneController implements Initializable {

    @FXML
    private Button conferenceManageButton;
    @FXML
    private Button userManageButton;
    @FXML
    private Button logoutButton;
    
    Admins loginAdmin;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickOnConferenceManageButton(ActionEvent event) throws IOException {
        //chuyển sang màn hình quản lý hội nghị của admin
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ConferencesManageScene.fxml"));   //lấy location màn hình ConferencesManageScene

        //tạo scene
        Parent detailConferenceParent = loader.load();
        Scene detailConferenceScene = new Scene(detailConferenceParent);
        
        //gửi thông tin loginAdmin nếu đã đăng nhập sang ConferencesManageScene
        ConferencesManageSceneController controller = loader.getController();
        controller.receiveLoginAdminInfor(this.loginAdmin);

        //tạo stage, chuyển ConferencesManageScene
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(detailConferenceScene);
        window.setTitle("Màn hình quản lý hội nghị");
        window.show();
    }

    @FXML
    private void clickOnUserManageButton(ActionEvent event) {
        
    }

    @FXML
    private void clickOnLogoutButton(ActionEvent event) throws IOException {
        String headerText = "Bạn có chắc chắn muốn đăng xuất";
        String message = "username: " + loginAdmin.getUsername();
        //mở hộp thoại xác nhận có muốn đăng xuất
        Optional<ButtonType> option = ConfirmationDialog.showConfirmationDialog(headerText, message);

        //user đồng ý đăng xuất
        if (option.get() == ButtonType.OK) {
            loginAdmin = null;
            AlertDialog.showAlertDialog("Đã đăng xuất tài khoản");

            //chuyển sang màn hình khách
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("GuestView.fxml"));   //lấy location màn hình GuestView

            //tạo scene
            Parent detailConferenceParent = loader.load();
            Scene detailConferenceScene = new Scene(detailConferenceParent);

            //tạo stage, chuyển GuestView
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(detailConferenceScene);
            window.setResizable(false);
            window.setTitle("Màn hình chính");
            window.show();
        }
    }
    
    /**
     * Nhận thông tin LoginUser
     *
     * @param loginAdmin
     */
    public void receiveLoginAdminInfor(Admins loginAdmin) {
        if(loginAdmin != null){
            this.loginAdmin = loginAdmin;
        }
    }
    
}
