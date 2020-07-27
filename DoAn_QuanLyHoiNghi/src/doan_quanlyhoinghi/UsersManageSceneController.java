/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import DAO.ConferencesDAO;
import DAO.PlacesDAO;
import DAO.RegisteredUsersDAO;
import DAO.UsersDAO;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableRowExpanderColumn;
import pojos.Admins;
import pojos.Conferences;
import pojos.Places;
import pojos.RegisteredUsers;
import pojos.Users;

/**
 * FXML Controller class
 *
 * @author ThanhTung
 */
public class UsersManageSceneController implements Initializable {

    @FXML
    private TableView<Users> userTableView;
    @FXML
    private TextField filterTextField;
    @FXML
    private Button backButton;
    @FXML
    private Button filterButton;
    @FXML
    private Button blockAccessButton;
    
    private TableColumn<Users, Integer> idCol;
    private TableColumn<Users, String> usernameCol;
    private TableColumn<Users, String> emailCol;
    private TableColumn<Users, Boolean> statusCol;
    private Admins loginAdmin;
    private ObservableList<Users> usersList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadInforIntoUserTableView();
    }
    
    /**
     * lấy danh sách các user mà hệ thống quản lí
     * 
     */
    private void loadInforIntoUserTableView(){
        TableRowExpanderColumn<Users> expander = new TableRowExpanderColumn<>(this::createEditor);
        
        usersList = FXCollections.observableArrayList();
        usersList.addAll(UsersDAO.getAll());    //lấy danh sách hội nghị

        //set up column
        idCol = new TableColumn<>("ID");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameCol = new TableColumn<>("Username");
        usernameCol.setMinWidth(300);
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));      
        emailCol = new TableColumn<>("Email");
        emailCol.setMinWidth(250);
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusCol = new TableColumn<>("Bị chặn");
        statusCol.setMinWidth(150);
        statusCol.setCellValueFactory(new PropertyValueFactory<>("isBlocked"));

        //set up table view
        userTableView.getColumns().addAll(expander, idCol, usernameCol, emailCol, statusCol);
        userTableView.setItems(usersList);
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

    @FXML
    private void clickOnBackButton(ActionEvent event) throws IOException {
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
    }

    @FXML
    private void clickOnFilterButton(ActionEvent event) {
        if (usersList != null) {
            // Khởi tạo FilteredList với ObservableList (ban đầu hiển thị tất cả dữ liệu).
            FilteredList<Users> filteredData = new FilteredList<>(usersList, b -> true);

            // 2. Set Predicate của filteredData bất cứ khi nào bộ lọc thay đổi
            filteredData.setPredicate(user -> {
                // hiển thị tất cả user khi filterTextField trống 
                if (filterTextField.getText() == null || filterTextField.getText().isEmpty()) {
                    return true;
                }

                // So sánh name và id của từng hội nghị với filterTextField
                String lowerCaseFilter = filterTextField.getText().toLowerCase();
                
                if (user.getUsername().indexOf(lowerCaseFilter) != -1) {
                    return true; // tìm thấy Username
                } else if (String.valueOf((boolean)user.getIsBlocked()).indexOf(lowerCaseFilter) != -1) {
                    return true; // tìm thấy getIsBlocked
                } else {
                    return false; // không tìm thấy trường dữ liệu phù hợp
                }
            });

            // Khởi tạo SortedList với FilteredList
            SortedList<Users> sortedData = new SortedList<>(filteredData);

            // Liên kết bộ so sánh SortedList với bộ so sánh TableView
            // để việc sắp xếp TableView có hiệu lực
            sortedData.comparatorProperty().bind(userTableView.comparatorProperty());

            // Set tableView với danh sách đã được lọc
            userTableView.setItems(sortedData);
        } else {
            System.out.println("Danh sach rong");
        }
    }

    @FXML
    private void clickOnBlockAccessButton(ActionEvent event) {
        Users user = userTableView.getSelectionModel().getSelectedItem();
        int index = usersList.indexOf(user);
        
        if(user.getIsBlocked() == false){
            user.setIsBlocked(true);    
            usersList.set(index, user); //cập nhật lại trạng thái của bảng
            UsersDAO.update(user);
            
            for(RegisteredUsers registeredUser : RegisteredUsersDAO.getAllByIDUser(user.getUserId())){               
                //kiểm tra hội nghị mà user tham dự đã diễn ra chưa để hủy
                if(checkConferenceTakesPlace(registeredUser.getConferences()) == -1){   //chưa diễn ra                   
                    //kiểm tra đã được admin duyệt hay chưa
                    //nếu duyệt rồi thì phải cập nhật lại chỗ trống của hội nghị
                    if(registeredUser.getIsAccepted() == true){ //đã duyệt
                        int attendess = registeredUser.getConferences().getRegisteredAttendees() - 1;
                        registeredUser.getConferences().setRegisteredAttendees(attendess);
                        ConferencesDAO.update(registeredUser.getConferences());  //update lại chỗ trống của conference
                    }
                    
                    //xóa 1 dòng trong bảng registeredUser
                    RegisteredUsersDAO.delete(registeredUser);
                }
            }
        } else {
            AlertDialog.showAlertDialog("Người dùng đã bị chặn truy cập");
        }
        
    }
    
    /**
     * kiểm trta selectedConference đã diễn ra hay chưa, trả về -1 nếu chưa diễn
     * ra, 0 và 1 nếu đã diễn ra
     *
     * @return
     */
    private int checkConferenceTakesPlace(Conferences conference) {
        Date date = new java.util.Date();
        return date.compareTo(conference.getOrganizedTime());
    }
    
    /**
     * tạo thêm cột row details cho tableView
     * 
     * @param param
     * @return 
     */
    private GridPane createEditor(TableRowExpanderColumn.TableRowDataFeatures<Users> param) {
        Users user = UsersDAO.getByID(param.getValue().getUserId());
        HashSet<RegisteredUsers> set = new HashSet<>(user.getRegisteredUserses());
        int i;
        int ID;
        String str;
        GridPane editor = new GridPane();
        editor.setPadding(new Insets(10));
        editor.setHgap(10);
        editor.setVgap(5);

        Label label = new Label(user.getFullName());      
        label.setMinWidth(500);       

        Button cancel = new Button("Hủy");
        cancel.setOnAction(e1 -> param.toggleExpanded());

        editor.addRow(0, new Label("Danh sách hội nghị mà " + user.getUsername() + " đã tham gia:"));
        i = 1;
        for (RegisteredUsers registeredUsers : set) {
            if (registeredUsers.getIsAccepted() == true) {
                ID = registeredUsers.getConferences().getConferenceId();
                str = registeredUsers.getConferences().getConferenceName();
                str = "ID :" + ID + "\t\t-\tHội nghị: " + str;
                editor.addRow(i, new Label(str));

                i++;
            }
        }
        editor.addRow(i, cancel);

        return editor;
    }
}
