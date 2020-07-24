/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import DAO.AdminsDAO;
import DAO.ConferencesDAO;
import DAO.UsersDAO;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import pojos.Admins;
import pojos.Conferences;
import pojos.Users;

/**
 *
 * @author ThanhTung
 */
public class GuestViewController implements Initializable {

    @FXML
    private TableView<Conferences> conferenceTable;
    @FXML
    private TableColumn<Conferences, Integer> IDCol;
    @FXML
    private TableColumn<Conferences, String> nameCol;
    @FXML
    private TableColumn<Conferences, String> descriptionCol;
    @FXML
    private Button watchingDetailButton;
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private Menu userMenu;
    @FXML
    private MenuItem viewProfileUserMenuItem;
    @FXML
    private MenuItem conferencesStatisticMenuItem;
    @FXML
    private MenuItem logoutUserMenuItem;
    @FXML
    private Button adminLoginButton;
    
    private ObservableList<Conferences> observableList;
    private Users loginUser;
    private Admins loginAdmin;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadConferencesIntoTableView();     //tải thông tin hội nghị vào table view
        this.watchingDetailButton.setDisable(true);     //không cho người dùng xem chi tiết cho đến khi chọn 1 hội nghị
        
//        //test user login
//        loginUser = UsersDAO.getByID(1);
//        viewProfileUserMenuItem.setDisable(false);
//        conferencesStatisticMenuItem.setDisable(false);
    }

    /**
     * Nhận thông tin LoginUser
     *
     * @param loginUser
     */
    public void receiveLoginUserInfor(Users loginUser) {
        if(loginUser != null){
            this.loginUser = UsersDAO.getByID(loginUser.getUserId());
            viewProfileUserMenuItem.setDisable(false);
            conferencesStatisticMenuItem.setDisable(false);
            logoutUserMenuItem.setDisable(false);
        }
    }

    /**
     * Khi khởi mở chương tình sẽ lấy thông tin (ID, tên, mô tả ngắn gọn) của
     * tất cả hội nghị trong DB và tải vào table view
     */
    private void loadConferencesIntoTableView() {
        this.observableList = FXCollections.observableArrayList();
        this.observableList.addAll(ConferencesDAO.getAll());    //lấy danh sách hội nghị

        //set up column
        this.IDCol.setCellValueFactory(new PropertyValueFactory<>("conferenceId"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("conferenceName"));
        this.descriptionCol.setCellValueFactory(new PropertyValueFactory<>("briefDescription"));

        //set up table view
        this.conferenceTable.setItems(this.observableList);
    }

    /**
     * chọn 1 hội nghị và chuyển sang màn hình xem chi tiết hội nghị đồng thời
     * truyền thông tin hội nghị vừa chọn và thông tin user nếu đã đăng nhập
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void clickOnWatchingDetailButton(ActionEvent event) throws IOException {
        Conferences conference = conferenceTable.getSelectionModel().getSelectedItem();

        if (conference != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DetailConferenceScene.fxml"));   //lấy location màn hình DetailConferenceScene

            //tạo scene
            Parent detailConferenceParent = loader.load();
            Scene detailConferenceScene = new Scene(detailConferenceParent);

            //gửi thông tin hội nghị sang DetailConferenceScene
            DetailConferenceSceneController controller = loader.getController();
            controller.receiveConferenceInfor(conference);        //gửi thông tin

            //gửi thông tin loginUser nếu đã đăng nhập sang DetailConferenceScene
            controller.receiveLoginUserInfor(this.loginUser);

            //tạo stage, chuyển DetailConferenceScene
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(detailConferenceScene);
            window.setResizable(false);
            window.setTitle("Màn hình xem chi tiết hội nghị");
            window.show();
        }
    }

    /**
     * cho người dùng xem chi tiết hội nghị khi nhấp vào tableView
     */
    @FXML
    private void clickOnTableView() {
        this.watchingDetailButton.setDisable(false);
    }

    /**
     * Hiển thị hộp thoại giới thiệu chương trình
     */
    @FXML
    private void clickOnHelpDialog() {
        String imageLink = "\\doan_quanlyhoinghi\\conferenceImage\\fit_hcmus.jpg";
        Image image = new Image(imageLink);
        ImageView imageView = new ImageView(image);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText(".");
        alert.setGraphic(imageView);
        alert.setContentText(" - Sinh viên thực hiện: Thái Thanh Tùng - 1712885\n"
                + " - Bài tập môn Lập trình ứng dụng Java - 17_32 - HK2_2019_2020:"
                + " Chương trình quản lý tổ chức Hội nghị"
                + ", sử dụng JavaFX kết hợp Hibernate với database MySQL.");
        alert.setTitle("Giới thiệu chương trình");
        alert.showAndWait();
    }

    /**
     * logout user đang đăng nhập khi click vào LogoutUserMenuItem
     * 
     */
    @FXML
    private void clickOnLogoutUserMenuItem() {
        String headerText = "Bạn có chắc chắn muốn đăng xuất";
        String message = "username: " + loginUser.getUsername();
        
        //mở hộp thoại xác nhận có muốn đăng xuất
        Optional<ButtonType> option = ConfirmationDialog.showConfirmationDialog(headerText, message);

        //user đồng ý đăng xuất
        if (option.get() == ButtonType.OK) {
            loginUser = null;
            viewProfileUserMenuItem.setDisable(true);
            conferencesStatisticMenuItem.setDisable(true);
            logoutUserMenuItem.setDisable(true);
            AlertDialog.showAlertDialog("Đã đăng xuất tài khoản");
        }
    }
    
    /**
     * mở màn hình thông tin user và cho phép chỉnh sửa khi click vào 
     * ViewProfileUserMenuItem
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void clickOnViewProfileUserMenuItem(ActionEvent event) throws IOException{
        if(this.loginUser != null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ProfileUserScene.fxml"));   //ProfileUserScene

            //tạo scene
            Parent profileUserParent = loader.load();
            Scene profileUserScene = new Scene(profileUserParent);

            //gửi thông tin loginUser nếu đã đăng nhập sang ProfileUserScene
            ProfileUserSceneController controller = loader.getController();
            controller.receiveLoginUserInfor(this.loginUser);

            //tạo stage, chuyển ProfileUserScene
            //Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage window = new Stage();
            window.setScene(profileUserScene);
            window.setResizable(false);
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Màn hình xem thông tin user");
            window.showAndWait();
        }
    }
    
    /**
     * mở màn hình thống kê hội nghị khi click vào ConferencesStatisticMenuItem
     * @param event
     * @throws IOException 
     */
    @FXML
    private void clickOnConferencesStatisticMenuItem(ActionEvent event) throws IOException{
        if(this.loginUser != null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ConferencesStatisticScene.fxml"));   //ConferencesStatisticScene

            //tạo scene
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            //gửi thông tin loginUser nếu đã đăng nhập sang ConferencesStatisticScene
            ConferencesStatisticSceneController controller = loader.getController();
            controller.receiveLoginUserInfor(this.loginUser);

            //tạo stage, chuyển ConferencesStatisticScene
            Stage window = new Stage();
            window.setScene(scene);
            window.setResizable(false);
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Màn hình thống kê hội nghị của user");
            window.showAndWait();   
        }
    }
    
    /**
     * Đăng nhập quyền admin khi click vào adminLoginButton
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void clickOnAdminLoginButton(ActionEvent event) throws IOException{
        //hiển thị dialog đăng nhập và lấy username, password mà người dùng
        //đã nhập trong dialog
        Optional<Pair<String, String>> result = LoginDialog.showLoginDialog("Đăng nhập quyền admin");   
        
        //người dùng thoát dialog và ko lấy được chuỗi
        if(result.isPresent() == false){
            return;
        }
        
        //người dùng đã yêu cầu login quyền admin
        String username = result.get().getKey();
        String password = result.get().getValue();

        //kiểm tra tài khoản và mật khẩu đăng nhập có hợp lệ
        boolean check = checkAdminValid(username, password);
        if (check == true) {  //hợp lệ
            //chuyển sang màn hình quản lý của admin
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AdminScene.fxml"));   //lấy location màn hình AdminScene

            //tạo scene
            Parent detailConferenceParent = loader.load();
            Scene detailConferenceScene = new Scene(detailConferenceParent);
            
            //gửi thông tin loginAdmin nếu đã đăng nhập sang AdminScene
            AdminSceneController controller = loader.getController();
            controller.receiveLoginAdminInfor(this.loginAdmin);

            //tạo stage, chuyển AdminScene
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(detailConferenceScene);
            window.setResizable(false);
            window.setTitle("Màn hình quyền admin");
            window.show();
        } else {
            AlertDialog.showAlertDialog("Tài khoản và mật khẩu không hợp lệ");
        }
    }
    
    /**
     * Kiểm tra username và pass để đăng nhập quyền admin có hợp lệ
     * @param username
     * @param password
     * @return 
     */
    private boolean checkAdminValid(String username, String password){
        password = MD5Library.MD5(password);
        List<Admins> list = AdminsDAO.getAll();
        boolean flag = false;
        
        for (Admins admin : list) {
            if (admin.getUsername().compareTo(username) == 0 && admin.getPass().compareTo(password) == 0) {   //hợp lệ
                flag = true;
                this.loginAdmin = admin;
                break;
            }
        }
        return flag;
    }
    
    
}
