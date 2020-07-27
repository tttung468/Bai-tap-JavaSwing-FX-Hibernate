/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import DAO.ConferencesDAO;
import DAO.PlacesDAO;
import DAO.RegisteredUsersDAO;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.table.TableRowExpanderColumn;
import pojos.Admins;
import pojos.Conferences;
import pojos.Places;
import pojos.RegisteredUsers;

/**
 * FXML Controller class
 *
 * @author ThanhTung
 */
public class ConferencesManageSceneController implements Initializable {

    @FXML
    private TableView<Conferences> conferenceTableView;
    @FXML
    private TableView<RegisteredUsers> registerdUserTableView;
    @FXML
    private TableColumn<RegisteredUsers, Integer> idRegisteredUserCol;
    @FXML
    private TableColumn<RegisteredUsers, Integer> idRegisteredConferenceCol;
    @FXML
    private TextField nameConferenceText;
    @FXML
    private TextField briefDescriptionText;
    @FXML
    private TextField detailDescriptionText;
    @FXML
    private TextField imageLinkText;
    @FXML
    private TextField timeText;
    @FXML
    private TextField placeText;
    @FXML
    private Button acceptUserButton;
    @FXML
    private Button detailConferenceButton;
    @FXML
    private Button insertConferenceButton;
    @FXML
    private Button backButton;
    
    private Admins loginAdmin;
    private TableColumn<Conferences, String> conferenceNameCol;
    private TableColumn<Conferences, Integer> idConferenceCol;
    private TableColumn<Conferences, String> placeNameCol;
    private TableColumn<Conferences, Integer> attendeesCol;
    private TableColumn<Conferences, Integer> capacityCol;
    private ObservableList<Conferences> conferencesList;
    private ObservableList<RegisteredUsers> registeredUsersList;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadConferencesIntoTableView();     //tải thông tin hội nghị vào table view
        loadRegisterdUserIntoTableView();   //tải thông tin user đang chờ duyệt tham gia vào table view
    }    
    
    /**
     * Lấy thông tin của tất cả hội nghị trong DB và tải vào table view
     * 
     */
    private void loadConferencesIntoTableView() {
        TableRowExpanderColumn<Conferences> expander = new TableRowExpanderColumn<>(this::createEditor);
        
        conferencesList = FXCollections.observableArrayList();
        conferencesList.addAll(ConferencesDAO.getAll());    //lấy danh sách hội nghị
       
        //set up column
        idConferenceCol = new TableColumn<>("ID");
        idConferenceCol.setMinWidth(50);
        idConferenceCol.setCellValueFactory(new PropertyValueFactory<>("conferenceId"));
        
        conferenceNameCol = new TableColumn<>("Tên hội nghị");
        conferenceNameCol.setMinWidth(280);
        conferenceNameCol.setCellValueFactory(new PropertyValueFactory<>("conferenceName"));
        
        placeNameCol = new TableColumn<>("Địa điểm");
        placeNameCol.setMinWidth(170);
        placeNameCol.setCellValueFactory(new Callback<CellDataFeatures<Conferences, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Conferences, String> p) {
                return new SimpleStringProperty(p.getValue().getPlaces().getPlaceName());
            }
        });
        
        attendeesCol = new TableColumn<>("Tham gia");
        attendeesCol.setMinWidth(50);
        attendeesCol.setCellValueFactory(new Callback<CellDataFeatures<Conferences, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<Conferences, Integer> p) {
                return new SimpleIntegerProperty(p.getValue().getRegisteredAttendees()).asObject();
            }
        });

        capacityCol = new TableColumn<>("Sức chứa");
        capacityCol.setMinWidth(50);
        capacityCol.setCellValueFactory(new Callback<CellDataFeatures<Conferences, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<Conferences, Integer> p) {
                return new SimpleIntegerProperty(p.getValue().getPlaces().getCapacity()).asObject();
            }
        });
        
        //set up table view
        conferenceTableView.getColumns().addAll(expander, idConferenceCol, conferenceNameCol, 
                placeNameCol, attendeesCol, capacityCol);
        conferenceTableView.setItems(this.conferencesList);
    }
    
    /**
     * Lấy thông tin của user đang chờ duyệt và tải vào table view 
     * 
     */
    private void loadRegisterdUserIntoTableView(){
        registeredUsersList = FXCollections.observableArrayList();
        registeredUsersList.addAll(getUserNotAccepted());    //lấy danh sách user
        
        //set up column
        idRegisteredUserCol.setCellValueFactory(new Callback<CellDataFeatures<RegisteredUsers, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<RegisteredUsers, Integer> param) {
                return new SimpleIntegerProperty(param.getValue().getId().getUserId()).asObject();
            }
        });
        
        idRegisteredConferenceCol.setCellValueFactory(new Callback<CellDataFeatures<RegisteredUsers, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<RegisteredUsers, Integer> param) {
                return new SimpleIntegerProperty(param.getValue().getId().getConferenceId()).asObject();
            }
        });
        
        //set up table view
        registerdUserTableView.setItems(registeredUsersList);
    }
    
    /**
     * Lấy các user chưa được duyệt tham dự
     * 
     * @return 
     */
    private List<RegisteredUsers> getUserNotAccepted(){
        List<RegisteredUsers> list = new ArrayList<>();  //list các user chưa được duyệt
        List<RegisteredUsers> fullUsersList = RegisteredUsersDAO.getAll();
        int i = 0;
        
        for (RegisteredUsers next : fullUsersList) {
            if(next.getIsAccepted() != true){   //user đã được duyệt tham dự
                list.add(next);
            }
            
            i++;
        }
        
        return list;
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
    
    /**
     * tạo thêm cột row details cho tableView
     * 
     * @param param
     * @return 
     */
    private GridPane createEditor(TableRowExpanderColumn.TableRowDataFeatures<Conferences> param) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); 
        Conferences conference = param.getValue();
        GridPane editor = new GridPane();
        editor.setPadding(new Insets(10));
        editor.setHgap(10);
        editor.setVgap(5);

        TextField nameText = new TextField(conference.getConferenceName());
        TextField localBriefDescriptionText = new TextField(conference.getBriefDescription());
        TextField localDetailDescriptionText = new TextField(conference.getDetailedDescription());
        TextField localImageLinkText = new TextField(conference.getImageLink());
        TextField localTimeText = new TextField(ft.format(conference.getOrganizedTime()));
        TextField localPlaceText = new TextField(conference.getPlaces().getPlaceId().toString());
        
        nameText.setMinWidth(500);
        localBriefDescriptionText.setMinWidth(500);
        localDetailDescriptionText.setMinWidth(500);
        localImageLinkText.setMinWidth(500);
        localTimeText.setMinWidth(500);
        localPlaceText.setMinWidth(500);
        
        Button save = new Button("Cập nhật");
        save.setOnAction(e -> {
            //kiểm tra các trường dữ liệu có rỗng
            if(nameText.getText().compareTo("") == 0 || localBriefDescriptionText.getText().compareTo("") == 0
                    || localDetailDescriptionText.getText().compareTo("") == 0 || localImageLinkText.getText().compareTo("") == 0
                    || localTimeText.getText().compareTo("") == 0 || localPlaceText.getText().compareTo("") == 0){
                AlertDialog.showAlertDialog("Nhập đầy đủ thông tin để cập nhật");
                param.toggleExpanded();
                return;
            }
            
            //kiểm tra id Place có tồn tại
            Places place = PlacesDAO.getByID(Integer.parseInt(localPlaceText.getText()));
            Places prePlace = conference.getPlaces();   //lấy giá trị place trước khi nhập Place mới
            if(place != null){  //tồn tại
                conference.setPlaces(place);    //cập nhật id place
            } else {
                AlertDialog.showAlertDialog("Địa điểm không tồn tại");
                param.toggleExpanded();
                return;
            }

            //cập nhật lại giá trị trong conference
            conference.setConferenceName(nameText.getText());
            conference.setBriefDescription(localBriefDescriptionText.getText());
            conference.setDetailedDescription(localDetailDescriptionText.getText());
            conference.setImageLink(localImageLinkText.getText());
            Date preDate = conference.getOrganizedTime();   //lấy giá trị Date trước khi nhập Date mới
            try {
                conference.setOrganizedTime(ft.parse(localTimeText.getText()));
            } catch (ParseException ex) {
                Logger.getLogger(ConferencesManageSceneController.class.getName()).log(Level.SEVERE, null, ex);
                AlertDialog.showAlertDialog("Nhập sai định dạng ngày tháng");
                param.toggleExpanded();
                return;
            }    
            
            //kiểm tra có sử dụng đồng thời 1 địa điểm
            if (isLocationUsedConcurrently(conference) == true) {
                conference.setPlaces(prePlace);     //cập nhật lại place trước khi nhập giá trị mới
                conference.setOrganizedTime(preDate);   //cập nhật lại Date trước khi nhập giá trị mới
                AlertDialog.showAlertDialog("Địa điểm đã được sử dụng vào cùng thời điểm. Mời bạn nhập lại thời gian");
                param.toggleExpanded();
                return;
            }
            
            //cập nhật xuống DB
            if(ConferencesDAO.update(conference) == true){
                AlertDialog.showAlertDialog("Cập nhật thành công");
            } else {
                AlertDialog.showAlertDialog("Cập nhật thất bại");
            }
            
            param.toggleExpanded();
        });

        Button cancel = new Button("Hủy");
        cancel.setOnAction(e1 -> param.toggleExpanded());

        editor.addRow(0, new Label("ID hội nghị"), new Label(conference.getConferenceId().toString()));
        editor.addRow(1, new Label("Tên"), nameText);
        editor.addRow(2, new Label("Mô tả ngắn"), localBriefDescriptionText);
        editor.addRow(3, new Label("Mô tả chi tiết"), localDetailDescriptionText);
        editor.addRow(4, new Label("Hình ảnh"), localImageLinkText);
        editor.addRow(5, new Label("Thời gian"), localTimeText);
        editor.addRow(6, new Label("ID địa điểm"), localPlaceText);
        editor.addRow(7, save, cancel);

        return editor;
    }
    
    /**
     * Quay trở về màn hình quản lý của admin
     * 
     * @param event 
     */
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

    /**
     * Duyệt yêu cầu tham gia hội nghị của user
     * @param event 
     */
    @FXML
    private void clickOnAcceptUserButton(ActionEvent event) {
        RegisteredUsers registeredUser = registerdUserTableView.getSelectionModel().getSelectedItem();
        
        if(registeredUser != null){
            //Kiểm tra hội nghị có còn chỗ trống
            Conferences conference = registeredUser.getConferences();
            int capacity = conference.getPlaces().getCapacity();
            int attendees = conference.getRegisteredAttendees();
            
            if(capacity - attendees > 0){   //còn chỗ
                //cập nhật trạng thái của registerUser
                registeredUser.setIsAccepted(true);
                boolean check1 = RegisteredUsersDAO.update(registeredUser);
                
                //cập nhật lại hội nghị
                conference.setRegisteredAttendees(attendees + 1);
                boolean check2 = ConferencesDAO.update(conference);
                
                if(check1 == true && check2 == true){
                    AlertDialog.showAlertDialog("Đã duyệt tham gia thành công");
                    registeredUsersList.remove(registeredUser);
                } else {
                    AlertDialog.showAlertDialog("Duyệt thất bại");
                }
            } else {
                AlertDialog.showAlertDialog("Hội nghị không còn chỗ.");
            }
            
        }
    }

    /**
     * Mở màn hình xem chi tiết thông tin hội nghị
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void clickOnDetailConferenceButton(ActionEvent event) throws IOException {
        Conferences conference = conferenceTableView.getSelectionModel().getSelectedItem();

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

    @FXML
    private void clickOnInsertConferenceButton(ActionEvent event) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Conferences conference = new Conferences();

        //kiểm tra các trường dữ liệu có rỗng
        if (nameConferenceText.getText().compareTo("") == 0 || briefDescriptionText.getText().compareTo("") == 0
                || detailDescriptionText.getText().compareTo("") == 0 || imageLinkText.getText().compareTo("") == 0
                || timeText.getText().compareTo("") == 0 || placeText.getText().compareTo("") == 0) {
            AlertDialog.showAlertDialog("Nhập đầy đủ thông tin để thêm mới hội nghị");
            return;
        }

        //kiểm tra id Place có tồn tại
        Places place = PlacesDAO.getByID(Integer.parseInt(placeText.getText()));
        if (place != null) {  //tồn tại
            conference.setPlaces(place);    //cập nhật id place
        } else {
            AlertDialog.showAlertDialog("Địa điểm không tồn tại");
            return;
        }

        //cập nhật lại giá trị trong conference
        conference.setConferenceName(nameConferenceText.getText());
        conference.setBriefDescription(briefDescriptionText.getText());
        conference.setDetailedDescription(detailDescriptionText.getText());
        conference.setImageLink(imageLinkText.getText());
        try {
            conference.setOrganizedTime(ft.parse(timeText.getText()));
        } catch (ParseException ex) {
            Logger.getLogger(ConferencesManageSceneController.class.getName()).log(Level.SEVERE, null, ex);
            AlertDialog.showAlertDialog("Nhập sai định dạng ngày tháng");
            return;
        }
        
        //kiểm tra có sử dụng đồng thời 1 địa điểm
        if (isLocationUsedConcurrently(conference) == true) {
            AlertDialog.showAlertDialog("Địa điểm đã được sử dụng vào cùng thời điểm. Mời bạn nhập lại thời gian");
            return;
        }
        
        //cập nhật lại tableView
        boolean check = ConferencesDAO.insert(conference);
        if(check == true){
            conferencesList.add(conference);
            AlertDialog.showAlertDialog("Thêm mới hội nghị thành công");
        } else {
            AlertDialog.showAlertDialog("Thêm mới thất bại");
        }
        
    }
    
    /**
     * kiểm tra hội nghị có sử dụng đồng thời 1 địa điểm
     * @return 
     */
    private boolean isLocationUsedConcurrently(Conferences conference){
        Date date1 = conference.getOrganizedTime();
        Date date2;

        for (Conferences tempConference : ConferencesDAO.getAll()) {
            date2 = tempConference.getOrganizedTime();
            
            if (date1.compareTo(date2) == 0 
                    && (conference.getPlaces().getPlaceId() == (int) tempConference.getPlaces().getPlaceId())) {               
                //kiểm tra hội nghị có bị trùng khi xử lí cập nhật
                if (conference.getConferenceId() != null) {
                    if (conference.getConferenceId() == (int) tempConference.getConferenceId()) {
                        continue;
                    }
                }
                
                return true;    //có hội nghị diễn ra trùng ngày
            }
        }
        
        return false;
    }

    
}
