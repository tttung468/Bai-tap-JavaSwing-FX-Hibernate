/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import DAO.ConferencesDAO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pojos.Conferences;

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

    private ObservableList<Conferences> observableList;
    
    @FXML
    private Button watchingDetailButton;
    @FXML
    private MenuItem LoginMenuItem;
    @FXML
    private MenuItem helpMenuItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadConferencesIntoTableView();     //tải thông tin hội nghị vào table view
        this.watchingDetailButton.setDisable(true);     //không cho người dùng xem chi tiết cho đến khi chọn 1 hội nghị
    }
    
    /**
     * Khi khởi mở chương tình sẽ lấy thông tin (ID, tên, mô tả ngắn gọn) của tất cả hội nghị trong DB
     * và tải vào table view
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
     * chọn 1 hội nghị và chuyển sang màn hình xem chi tiết hội nghị
     * nếu chưa chọn hội nghị thì xuất thông báo yêu cầu chọn hội nghị
     * @param event
     * @throws IOException 
     */
    @FXML
    private void switchDetailScene(ActionEvent event) throws IOException{
        Conferences conference = conferenceTable.getSelectionModel().getSelectedItem();
      
        if (conference != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DetailConferenceScene.fxml"));

            //lấy thông tin của stage chi tiết hội nghị
            Parent detailConferenceParent = loader.load();
            Scene detailConferenceScene = new Scene(detailConferenceParent);

            //truyền thông tin hội nghị đã chọn
            DetailConferenceSceneController controller = loader.getController();
            controller.initConferenceInfor(conference);

            //chuyển màn hình chi tiết hội nghị
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(detailConferenceScene);
            window.show();
        }
    }
    
    /**
     * cho người dùng xem chi tiết hội nghị
     */
    @FXML
    private void clickOnTableView(){
        this.watchingDetailButton.setDisable(false);
    }
    
    @FXML
    private void showHelpDialog(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String imageLink = "\\doan_quanlyhoinghi\\conferenceImage\\fit_hcmus.jpg";
        Image image = new Image(imageLink);
        ImageView imageView = new ImageView(image);
        
        alert.setHeaderText(".");
        alert.setGraphic(imageView);
        alert.setContentText(" - Sinh viên thực hiện: Thái Thanh Tùng - 1712885\n"
                + " - Bài tập môn Lập trình ứng dụng Java - 17_32 - HK2_2019_2020:"
                + " Chương trình quản lý tổ chức Hội nghị"
                + ", sử dụng JavaFX kết hợp Hibernate với database MySQL.");
        alert.setTitle("Giới thiệu chương trình");
        alert.showAndWait();
    }
}
