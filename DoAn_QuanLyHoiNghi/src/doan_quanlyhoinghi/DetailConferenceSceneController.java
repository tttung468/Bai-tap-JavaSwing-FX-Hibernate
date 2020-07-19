/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import DAO.UsersDAO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pojos.Conferences;
import pojos.RegisteredUsers;
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
    private Button registerButton;
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
     * Nhận thông tin hội nghị đã chọn từ màn hình GuestView
     * Khi chuyển sang màn hình xem chi tiết hội nghị sẽ tự động load thông tin
     * hội nghị đã chọn lên màn hình
     *
     * @param conference
     */
    public void initConferenceInfor(Conferences conference) {
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
            tempStr += "image0.jpg";                //ảnh không tồn tại, lấy ảnh mặc định
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
        centerImage();
    }

    //điều chỉnh ảnh vào chính giữa của imageView
    //nguồn: https://stackoverflow.com/questions/32781362/centering-an-image-in-an-imageview/32866286#32866286
    private void centerImage() {
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
     * Chọn backGuestSceneButton thì sẽ chuyển về Guest scene
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void clickOnbackGuestSceneButton(ActionEvent event) throws IOException {
        Parent detailConferenceParent = FXMLLoader.load(getClass().getResource("GuestView.fxml"));
        Scene detailConferenceScene = new Scene(detailConferenceParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(detailConferenceScene);
        window.show();
        window.setResizable(false);
    }

    @FXML
    private void clickOnRegisterButton(ActionEvent event) {
        String choiceResult = "";
        
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);

        //kiểm tra người dùng đã tham gia hội nghị này chưa
        //this.loginUser = UsersDAO.getByID(1);
        if (checkUserRegisterConference() == true) {
            alert.setContentText("Bạn đã tham gia hội nghị này");
            alert.showAndWait();
            return;
        }

        //kiểm tra hội nghị đã diễn ra hay chưa
        if (checkConferenceTakesPlace() != -1) {
            alert.setContentText("Hội nghị đã diễn ra, không thể đăng ký");
            alert.showAndWait();
            return;
        }
        
        //kiểm tra hội nghị còn chỗ trống
        if (checkConferenceHasEmptySeats() <= 0) {   //không còn chỗ
            alert.setContentText("Hội nghị không còn chỗ để đăng ký");
            alert.showAndWait();
            return;
        } 
        else {    //còn chỗ
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("", "Đăng ký bằng tài khoản có sẵn", "Tạo tài khoản mới để đăng ký");
            choiceDialog.setContentText("Chọn thao tác");
            choiceDialog.setHeaderText(null);
            choiceDialog.showAndWait();
            choiceResult = choiceDialog.getResult();  
        }

        //chuyển màn hình dựa vào thao tác vừa chọn
        if(choiceResult != null){
            if (choiceResult.compareTo("Đăng ký bằng tài khoản có sẵn") == 0) {
                System.out.println("dang nhap");
                
            } else if (choiceResult.compareTo("Tạo tài khoản mới để đăng ký") == 0) {
                System.out.println("dang ky tai khoan ");
                
            }
        }
    }

    /**
     * kiểm tra loginUser đã tham gia selectedConference hay chưa
     * trả về true nếu đã tham gia và ngược lại
     * @return 
     */
    private boolean checkUserRegisterConference(){
        if (this.loginUser != null) {
            HashSet<RegisteredUsers> set = new HashSet<>(loginUser.getRegisteredUserses());
            for (RegisteredUsers registeredUsers : set) {
                if (registeredUsers.getConferences().getConferenceId() == (int) selectedConference.getConferenceId()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * kiểm trta selectedConference đã diễn ra hay chưa
     * trả về -1 nếu chưa diễn ra, 0 và 1 nếu đã diễn ra
     * @return 
     */
    private int checkConferenceTakesPlace(){
        Date date = new java.util.Date();
        return date.compareTo(selectedConference.getOrganizedTime());
    }
    
    /**
     * kiểm tra hội nghị còn chỗ trống
     * trả về giá trị <= 0 là hết chỗ, > 0 là còn chỗ
     * @return 
     */
    private int checkConferenceHasEmptySeats(){
        return selectedConference.getPlaces().getCapacity() - selectedConference.getRegisteredAttendees();  
    }
}
