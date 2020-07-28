/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import DAO.ConferencesDAO;
import DAO.RegisteredUsersDAO;
import DAO.UsersDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Conferences;
import pojos.RegisteredUsers;
import pojos.RegisteredUsersId;
import pojos.Users;

/**
 * FXML Controller class
 *
 * @author ThanhTung
 */
public class ConferencesStatisticSceneController implements Initializable {

    @FXML
    private TableView<Conferences> conferenceStatisticTable;
    @FXML
    private TableColumn<Conferences, Integer> IDCol;
    @FXML
    private TableColumn<Conferences, String> conferenceNameCol;
    @FXML
    private TableColumn<Conferences, String> briefDescriptionCol;
    @FXML
    private TableColumn<Conferences, Date> timeCol;
    @FXML
    private Button watchingDetailButton;
    @FXML
    private TextField filterTextField;
    @FXML
    private Button filterButton;
    @FXML
    private Button cancelButton;
    
    private ObservableList<Conferences> observableList;
    private Users loginUser;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.watchingDetailButton.setDisable(true);     //không cho người dùng xem chi tiết cho đến khi chọn 1 hội nghị
        
    }    
    
    /**
     * Nhận thông tin LoginUser và load danh sách hội nghị đã tham gia
     *
     * @param loginUser
     */
    public void receiveLoginUserInfor(Users loginUser) {
        this.loginUser = UsersDAO.getByID(loginUser.getUserId());
        
        //load danh sách hội nghị
        loadInforIntoConferenceDetailTableView();
        
    }
    
    /**
     * lấy các hội nghị mà user đã tham gia sau khi nhận thông tin user được gửi
     * từ màn hình khác
     * 
     */
    private void loadInforIntoConferenceDetailTableView(){
        observableList = FXCollections.observableArrayList();
        observableList.addAll(getConferencesListUserJoin());    //lấy danh sách hội nghị

        //set up column
        IDCol.setCellValueFactory(new PropertyValueFactory<>("conferenceId"));
        conferenceNameCol.setCellValueFactory(new PropertyValueFactory<>("conferenceName"));
        briefDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("briefDescription"));    
        timeCol.setCellValueFactory(new PropertyValueFactory<>("organizedTime"));

        //set up table view
        conferenceStatisticTable.setItems(this.observableList);
    }
    
    /**
     * Lấy danh sách các hội nghị user đăng ký tham dự và được admin duyệt
     * 
     * @return 
     */
    private List<Conferences> getConferencesListUserJoin(){
        List<Conferences> list = new ArrayList<>();
        List<Conferences> fullConferencesList = ConferencesDAO.getAll();
        RegisteredUsers registeredUser;
        
        for (Conferences conferences : fullConferencesList) {
            registeredUser = RegisteredUsersDAO.getByID(
                    new RegisteredUsersId(loginUser.getUserId(), conferences.getConferenceId()));
            
            //không tìm thấy user tham gia conference này
            //hoặc user chưa được chấp nhận tham gia thì loại ra khỏi list
            if(registeredUser != null && registeredUser.getIsAccepted() == true){ 
               list.add(conferences);
            }
        }
        
        return list;
    }
    
    /**
     * cho người dùng xem chi tiết hội nghị khi nhấp vào tableView
     * 
     */
    @FXML
    private void clickOnTableView() {
        this.watchingDetailButton.setDisable(false);
    }

    /**
     * mở màn hình xem chi tiết hội nghị khi click vào WatchingDetailButton
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void clickOnWatchingDetailButton(ActionEvent event) throws IOException {
        Conferences conference = conferenceStatisticTable.getSelectionModel().getSelectedItem();

        if (conference != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DetailConferenceScene.fxml"));   //lấy location màn hình DetailConferenceScene

            //tạo scene
            Parent detailConferenceParent = loader.load();
            Scene detailConferenceScene = new Scene(detailConferenceParent);

            //gửi thông tin hội nghị sang DetailConferenceScene
            DetailConferenceSceneController controller = loader.getController();
            controller.receiveConferenceInfor(conference);        //gửi thông tin

            //yêu cầu disable các button thao tác
            controller.disableButton();

            //tạo stage, chuyển DetailConferenceScene
            Stage window = new Stage();
            window.setScene(detailConferenceScene);
            window.setResizable(false);
            window.setTitle("Màn hình xem chi tiết hội nghị");
            window.initModality(Modality.APPLICATION_MODAL);
            window.show();
        }
    }
    
    /**
     * lọc hội nghị theo tên hoặc theo ngày khi click vào FilterButton
     * 
     */
    @FXML
    private void clickOnFilterButton(){
        if (observableList != null) {
            // Khởi tạo FilteredList với ObservableList (ban đầu hiển thị tất cả dữ liệu).
            FilteredList<Conferences> filteredData = new FilteredList<>(observableList, b -> true);

            // 2. Set Predicate của filteredData bất cứ khi nào bộ lọc thay đổi
            filteredData.setPredicate(conference -> {
                // hiển thị tất cả hội nghị khi filterTextField trống 
                if (filterTextField.getText() == null || filterTextField.getText().isEmpty()) {
                    return true;
                }

                // So sánh name và id của từng hội nghị với filterTextField
                String lowerCaseFilter = filterTextField.getText().toLowerCase();

                if (conference.getConferenceName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // tìm thấy ConferenceName
                } else if (String.valueOf(conference.getOrganizedTime()).indexOf(lowerCaseFilter) != -1) {
                    return true; // tìm thấy OrganizedTime
                } else {
                    return false; // không tìm thấy trường dữ liệu phù hợp
                }
            });

            // Khởi tạo SortedList với FilteredList
            SortedList<Conferences> sortedData = new SortedList<>(filteredData);

            // Liên kết bộ so sánh SortedList với bộ so sánh TableView
            // để việc sắp xếp TableView có hiệu lực
            sortedData.comparatorProperty().bind(conferenceStatisticTable.comparatorProperty());

            // Set tableView với danh sách đã được lọc
            conferenceStatisticTable.setItems(sortedData);
        } else {
            System.out.println("Danh sach rong");
        }
        
    } 

    @FXML
    private void clickOnCancelButton(ActionEvent event) {
        Conferences conference = conferenceStatisticTable.getSelectionModel().getSelectedItem();

        if (conference != null) {
            String headerText = "Bạn có chắc chắn muốn hủy tham gia hội nghị này";
            String message = "Hội nghị: " + conference.getConferenceName();

            //mở hộp thoại xác nhận có muốn hủy tham dự
            Optional<ButtonType> option = ConfirmationDialog.showConfirmationDialog(headerText, message);

            //user đồng ý hủy
            if (checkConferenceTakesPlace(conference) != -1) { //kiểm tra hội nghị đã diễn ra hay chưa
                AlertDialog.showAlertDialog("Hội nghị đã diễn ra, không thể hủy tham gia");
                return;
            }     
            
            //hủy tham gia hội nghị
            if (option.get() == ButtonType.OK) {
                RegisteredUsers registeredUser = RegisteredUsersDAO.getByID(
                        new RegisteredUsersId(loginUser.getUserId(), conference.getConferenceId()));
                
                if(registeredUser != null){
                    Conferences conference1 = registeredUser.getConferences();
                    int n = registeredUser.getConferences().getRegisteredAttendees();
                    conference1.setRegisteredAttendees(n - 1);
                    ConferencesDAO.update(conference1);  //cập nhật lại số người tham gia
                    RegisteredUsersDAO.delete(registeredUser);  //xóa DS tham dự
                    AlertDialog.showAlertDialog("Hủy tham gia thành công");
                } else {
                    AlertDialog.showAlertDialog("Hội nghị này đã hủy tham gia");
                }
            }
            
        } else {
            AlertDialog.showAlertDialog("Bạn cần chọn 1 hội nghị để hủy tham gia");
        }
    }
    
    /**
     * kiểm trta selectedConference đã diễn ra hay chưa, trả về -1 nếu chưa diễn
     * ra, 0 và 1 nếu đã diễn ra
     *
     * @return
     */
    private int checkConferenceTakesPlace(Conferences selectedConference) {
        Date date = new java.util.Date();
        return date.compareTo(selectedConference.getOrganizedTime());
    }
}
