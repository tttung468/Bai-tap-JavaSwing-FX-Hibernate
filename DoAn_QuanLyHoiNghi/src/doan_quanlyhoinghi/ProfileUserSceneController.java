/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import DAO.UsersDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Users;

/**
 * FXML Controller class
 *
 * @author ThanhTung
 */
public class ProfileUserSceneController implements Initializable {

    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private Button updateInforButton;
    @FXML
    private Label usernameLabel;
    
    private Users loginUser;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    /**
     * Nhận thông tin LoginUser và load thong tin user
     *
     * @param loginUser
     */
    public void receiveLoginUserInfor(Users loginUser) {
        this.loginUser = UsersDAO.getByID(loginUser.getUserId());
        
        //load thông tin user
        loadUserInfor();
    }
    
    /**
     * Load thông tin của user lên màn hình
     * 
     */
    private void loadUserInfor(){
        fullNameTextField.setText(loginUser.getFullName());
        usernameLabel.setText(loginUser.getUsername());
        emailTextField.setText(loginUser.getEmail());
    }
    
    @FXML
    private void clickOnUpdateInforButton(ActionEvent event){
        //test
        //System.out.println(fullNameTextField.getText() + "---" + passwordTextField.getText() + "---" + emailTextField.getText());
        
        if(fullNameTextField.getText().compareTo("") != 0 && emailTextField.getText().compareTo("") != 0){
            //cập nhật
            loginUser.setFullName(fullNameTextField.getText());
            loginUser.setEmail(emailTextField.getText());
            
            if (passwordTextField.getText().compareTo("") != 0) {  //cập nhật mật khẩu mới
                loginUser.setPass(MD5Library.MD5(passwordTextField.getText()));
            } else {
                passwordTextField.setText(loginUser.getPass());
            } 
            
            boolean check = UsersDAO.update(loginUser);
            
            if (check == true) {
                passwordTextField.setText("***********");
                
                AlertDialog.showAlertDialog("Cập nhật thành công");
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.close();
            } else {
                AlertDialog.showAlertDialog("Cập nhật thất bại");
            }
            
        } else {
            AlertDialog.showAlertDialog("Nhập đầy đủ thông tin để cập nhật");
        }
    }
}
