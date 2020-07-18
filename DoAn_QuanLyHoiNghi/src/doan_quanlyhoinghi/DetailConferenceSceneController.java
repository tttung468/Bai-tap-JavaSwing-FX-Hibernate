/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doan_quanlyhoinghi;

import java.io.IOException;
import java.net.URL;
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
        String str;
        
        this.selectedConference = conference;
        this.nameConferenceLabel.setText(selectedConference.getConferenceName());
        this.briefDescriptionLabel.setText(selectedConference.getBriefDescription());
        this.detailedDescriptionLabel.setText(selectedConference.getDetailedDescription());
        this.placeLabel.setText(selectedConference.getPlaces().getPlaceName());
        str = Integer.toString(selectedConference.getRegisteredAttendees());
        str += " / " + Integer.toString(selectedConference.getPlaces().getCapacity());
        this.attendeesLabel.setText(str);
        this.timeLabel.setText("0000-00-00");
    }
}
