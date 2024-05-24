package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.fxml.FXMLLoader.load;

public class ChangePass extends HelloController{
    @FXML
    private DatePicker fpbirthday;
    @FXML
    private TextField fpfavoritecity;
    @FXML
    private TextField fpusername;
    @FXML
    private Button resetbtn;
    @FXML
    private PasswordField nPass;
    @FXML
    private PasswordField nPass2;
    private Error error;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private Stage stage;
    private Scene scene;
    private String rename;


//    public void switchtoLogin(ActionEvent event) throws IOException {
//        Parent root = load(getClass().getResource("hello-view.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
    public void switchForgetPass(ActionEvent event) throws IOException {
        Parent root = load(getClass().getResource("ForgetPass.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void resetbtn(ActionEvent event1)  {
        if(fpusername.getText().isEmpty() || fpfavoritecity.getText().isEmpty()
                || fpbirthday.getValue() == null){
            error = new Error();
            error.setfield("Please fill out all field");
        }else{
            try {
                connect = Detabase.CODB();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String checkdeta = "SELECT name, city, Date FROM information WHERE name =? and city =? and Date =?";
            try{
                Static.name =fpusername.getText();
                prepare = connect.prepareStatement(checkdeta);
                prepare.setString(1,fpusername.getText());
                prepare.setString(2,fpfavoritecity.getText());
                prepare.setString(3, String.valueOf(fpbirthday.getValue()));
                resultSet = prepare.executeQuery();
                if(resultSet.next()){
                    // System.out.println(fpusername.getText());
                    rename = fpusername.getText();
                    Parent root = load(getClass().getResource("ChangePass.fxml"));
                    stage = (Stage) ((Node) event1.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }else{
                    error = new Error();
                    error.setfield("Invalid information.");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void Change(){
        if(nPass.getText().isEmpty() || nPass2.getText().isEmpty()){
            error = new Error();
            error.setfield("Please fill out all field");
        }else if(nPass2.getText().equals(nPass.getText()) ){
            try {
                connect = Detabase.CODB();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try{
                System.out.println(rename);
                String updatep = "UPDATE information SET password = '" + nPass.getText()+"' WHERE name = '"
                        +Static.name+"'";
                prepare = connect.prepareStatement(updatep);
                prepare.executeUpdate();
                error = new Error();
                error.update("Successfully updated.");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            error = new Error();
            error.setfield("The passwords are not same");
        }
    }
}
