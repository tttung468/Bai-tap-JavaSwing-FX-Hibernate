/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pojos.Conferences;

/**
 * FXML Controller class
 *
 * @author ThanhTung
 */
public class DetailConferenceSceneController implements Initializable {

    @FXML
    private Button backGuestScene;
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
    
    private Conferences selectedConference;
    @FXML
    private ImageView imageView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    /**
     * Chọn button backGuestScene thì sẽ chuyển về Guest scene
     * @param event
     * @throws IOException 
     */
    @FXML
    private void backToGuestScene(ActionEvent event) throws IOException{
        Parent detailConferenceParent = FXMLLoader.load(getClass().getResource("GuestView.fxml"));
        Scene detailConferenceScene = new Scene(detailConferenceParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(detailConferenceScene);
        window.show();
        window.setResizable(false);
    }
    
    /**
     * Khi chuyển sang màn hình xem chi tiết hội nghị sẽ tự động load thông tin hội nghị đã chọn lên màn hình
     * @param conference 
     */
    public void initConferenceInfor(Conferences conference){
        String tempStr = "\\doan_quanlyhoinghi\\conferenceImage\\";
        SimpleDateFormat ft = new SimpleDateFormat ("E yyyy-MM-dd");
        
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
    public void centerImage() {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
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
}
