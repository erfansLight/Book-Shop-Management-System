package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    private String rename;


    public void resetbtn(ActionEvent event)  {
        if(fpusername.getText().isEmpty() || fpfavoritecity.getText().isEmpty()
                || fpbirthday.getValue() == null){
            error = new Error();
            error.setfield("Please fill out all field");
        }else{
            try {
                connect = Database.CODB();
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
                    rename = fpusername.getText();
                    Switch s = new Switch();
                    s.switchto(event,"ChangePass.fxml");
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
                connect = Database.CODB();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try{
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
