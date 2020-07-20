/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import DAO.ConferencesDAO;
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
    private MenuItem LoginMenuItem;
    @FXML
    private MenuItem helpMenuItem;

    private ObservableList<Conferences> observableList;
    Users loginUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadConferencesIntoTableView();     //tải thông tin hội nghị vào table view
        this.watchingDetailButton.setDisable(true);     //không cho người dùng xem chi tiết cho đến khi chọn 1 hội nghị
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
        String imageLink = "\\doan_quanlyhoinghi\\conferenceImage\\fit@hcmus.jpg";
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

}
