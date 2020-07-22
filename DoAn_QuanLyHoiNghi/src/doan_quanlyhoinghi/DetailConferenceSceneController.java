/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import DAO.RegisteredUsersDAO;
import DAO.UsersDAO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import pojos.Conferences;
import pojos.RegisteredUsers;
import pojos.RegisteredUsersId;
import pojos.Users;

/**
 * FXML Controller class
 *
 * @author ThanhTung
 */
public class DetailConferenceSceneController implements Initializable {

    @FXML
    private Button backGuestSceneButton;
    @FXML
    private Button registerUserButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button joinConferenceButton;
    @FXML
    private Label nameConferenceLabel;
    @FXML
    private Label briefDescriptionLabel;
    @FXML
    private Label detailedDescriptionLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label placeLabel;
    @FXML
    private Label attendeesLabel;
    @FXML
    private ImageView imageView;

    private Conferences selectedConference;
    private Users loginUser;
       

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Nhận thông tin hội nghị đã chọn từ màn hình GuestView. Khi chuyển sang
     * màn hình xem chi tiết hội nghị sẽ tự động load thông tin hội nghị đã chọn
     * lên màn hình
     *
     * @param conference
     */
    public void receiveConferenceInfor(Conferences conference) {
        String tempStr = "\\doan_quanlyhoinghi\\conferenceImage\\";
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy-MM-dd");

        //lấy đường dẫn đang làm việc
        File file = new File("");
        String currentDirectory = file.getAbsolutePath() + "\\src\\doan_quanlyhoinghi\\conferenceImage\\"
                + conference.getImageLink();
        //System.out.println(currentDirectory);

        //kiểm tra ảnh có tồn tại
        //nếu tồn tại thì lấy ảnh lên, không tồn tại thì lấy ảnh mặc định
        File f = new File(currentDirectory);
        if (f.exists()) {
            //System.out.println("File existed");
            tempStr += conference.getImageLink();   //ảnh tồn tại
        } else {
            //System.out.println("File not found!");
            tempStr += "image0.jpg";      //ảnh không tồn tại, lấy ảnh mặc định
        }
        Image image = new Image(tempStr);   //lấy ảnh

        this.selectedConference = conference;
        this.nameConferenceLabel.setText(selectedConference.getConferenceName());
        this.briefDescriptionLabel.setText(selectedConference.getBriefDescription());
        this.detailedDescriptionLabel.setText(selectedConference.getDetailedDescription());
        this.placeLabel.setText(selectedConference.getPlaces().getPlaceName());
        tempStr = Integer.toString(selectedConference.getRegisteredAttendees());
        tempStr += " / " + Integer.toString(selectedConference.getPlaces().getCapacity());
        this.attendeesLabel.setText(tempStr);
        this.timeLabel.setText(ft.format(conference.getOrganizedTime()));
        this.imageView.setImage(image);
        adjustImageCenter();
    }

    /**
     * Nhận thông tin LoginUser
     *
     * @param loginUser
     */
    public void receiveLoginUserInfor(Users loginUser) {
        this.loginUser = loginUser;
    }

    /**
     * điều chỉnh ảnh vào chính giữa của imageView
     * nguồn: https://stackoverflow.com/questions/32781362/centering-an-image-in-an-imageview/32866286#32866286
     *
     */
    private void adjustImageCenter() {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }

    /**
     * Nhấp vào backGuestSceneButton thì chuyển về Guest scene
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void clickOnbackGuestSceneButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GuestView.fxml"));   //lấy location màn hình DetailConferenceScene

        //tạo scene
        Parent guestViewParent = loader.load();
        Scene guestViewScene = new Scene(guestViewParent);

        //gửi thông tin loginUser sang GuestView
        GuestViewController controller = loader.getController();
        controller.receiveLoginUserInfor(this.loginUser);        //gửi thông tin

        //tạo stage, chuyển DetailConferenceScene
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(guestViewScene);
        window.setResizable(false);
        window.show();
    }

    /**
     * Nhấp vào loginButton thì cho phép đăng ký tham dự hội nghị
     * 
     * @param event 
     */
    @FXML
    private void clickOnLoginButton(ActionEvent event) {
        if(this.loginUser != null){
            AlertDialog.showAlertDialog("Bạn đã đăng nhập");
            return;
        }
        
        //hiển thị dialog đăng nhập và lấy username, password mà người dùng
        //đã nhập trong dialog
        Optional<Pair<String, String>> result = LoginDialog.showLoginDialog();   
        
        //người dùng thoát dialog và ko lấy được chuỗi
        if(result.isPresent() == false){
            return;
        }
        
        //người dùng đã yêu cầu login
        String username = result.get().getKey();
        String password = result.get().getValue();

        //kiểm tra tài khoản và mật khẩu đăng nhập có hợp lệ
        boolean check = checkUserValid(username, password);
        if (check == true) {  //hợp lệ
            //kiểm tra tài khoản có bị khóa truy cập
            if(loginUser.getIsBlocked() == true){   //blocked
                AlertDialog.showAlertDialog("Tài khoản đã bị chặn truy cập");
                loginUser = null;
            } else {
                AlertDialog.showAlertDialog("Đăng nhập thành công");
            }
        } else {
            AlertDialog.showAlertDialog("Tài khoản và mật khẩu không hợp lệ");
        }
    }

    @FXML
    private void clickOnRegisterUserButton(ActionEvent event) {
        showRegisterDialog();
    }
    
    /**
     * Nhấp vào JoinConferenceButton cho phép người dùng tham gia hôi nghị nêu
     * đã đăng nhập, ngược lại thì yêu cầu đăng nhập
     * 
     * @param event 
     */
    @FXML
    private void clickOnJoinConferenceButton(ActionEvent event) {
        //this.loginUser = UsersDAO.getByID(1);     //test
        if (this.loginUser == null) {   //user chưa đăng nhập
            AlertDialog.showAlertDialog("Bạn cần đăng nhập tài khoản để đăng ký tham gia");
        }  
        else if (checkUserRegisteredSelectedConference() == true) { //kiểm tra người dùng đã tham gia hội nghị này chưa
            AlertDialog.showAlertDialog("Bạn đã đăng ký tham gia hội nghị này");
        }     
        else if (checkConferenceTakesPlace() != -1) { //kiểm tra hội nghị đã diễn ra hay chưa
            AlertDialog.showAlertDialog("Hội nghị đã diễn ra, không thể đăng ký");
        }     
        else if (checkConferenceHasEmptySeats() <= 0) {   //kiểm tra hội nghị còn chỗ trống
            AlertDialog.showAlertDialog("Hội nghị không còn chỗ để đăng ký");
        }  
        else {
            //xử lý đăng ký cho user
            RegisteredUsers registeredUser = new RegisteredUsers();
            registeredUser.setId(new RegisteredUsersId(loginUser.getUserId(), selectedConference.getConferenceId()));
            registeredUser.setConferences(selectedConference);
            registeredUser.setUsers(loginUser);
            
            boolean check = RegisteredUsersDAO.insert(registeredUser);
            
            //kiểm tra user đã được hệ thống đăng ký hay chưa
            if(check == true){  //đăng ký thành công
                AlertDialog.showAlertDialog("Đăng ký thành công. Vui lòng chờ admin duyệt");
                this.loginUser = UsersDAO.getByID(loginUser.getUserId());   //cập nhật lại user
            } else {    //đăng ký thất bại
                AlertDialog.showAlertDialog("Đăng ký thất bại");
            }
        }
    }
   
    private void showRegisterDialog() {
        AlertDialog.showAlertDialog("Xử lý Tạo tài khoản mới");
    }
    

    /**
     * kiểm tra loginUser đã tham gia selectedConference hay chưa, trả về true
     * nếu đã tham gia và ngược lại
     *
     * @return
     */
    private boolean checkUserRegisteredSelectedConference() {
        if (this.loginUser != null) {
            HashSet<RegisteredUsers> set = new HashSet<>(loginUser.getRegisteredUserses());
            for (RegisteredUsers registeredUsers : set) {
                //System.out.println(registeredUsers.getId().getUserId() + "---" + registeredUsers.getId().getConferenceId());
                
                if (registeredUsers.getId().getConferenceId() == selectedConference.getConferenceId()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * kiểm trta selectedConference đã diễn ra hay chưa, trả về -1 nếu chưa diễn
     * ra, 0 và 1 nếu đã diễn ra
     *
     * @return
     */
    private int checkConferenceTakesPlace() {
        Date date = new java.util.Date();
        return date.compareTo(selectedConference.getOrganizedTime());
    }

    /**
     * kiểm tra hội nghị còn chỗ trống, trả về giá trị <= 0 là hết chỗ, > 0 là
     * còn chỗ
     *
     * @return
     */
    private int checkConferenceHasEmptySeats() {
        return selectedConference.getPlaces().getCapacity() - selectedConference.getRegisteredAttendees();
    }

    /**
     * kiểm tra username và password người dùng nhập có hợp lệ
     * trả true nếu hợp lệ và ngược lại
     * 
     * @param username
     * @param password
     * @return 
     */
    private boolean checkUserValid(String username, String password) {
        password = MD5Library.MD5(password);
        List<Users> list = UsersDAO.getAll();
        boolean flag = false;
        
        for (Users users : list) {
            if (users.getUsername().compareTo(username) == 0 && users.getPass().compareTo(password) == 0) {   //hợp lệ
                flag = true;
                this.loginUser = users;     //lấy thông tin user
                break;
            }
        }
        return flag;
    }

    /**
     * disable các button, ko cần thao tác, chỉ cần xem chi tiết hội nghị
     * 
     */
    public void disableButton(){
        this.registerUserButton.setVisible(false);
        this.joinConferenceButton.setVisible(false);
        this.loginButton.setVisible(false);
        this.backGuestSceneButton.setVisible(false);
    }
}
