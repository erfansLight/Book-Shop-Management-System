package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;

import static javafx.fxml.FXMLLoader.load;

public class HelloController implements Initializable {
    @FXML
    private PasswordField passwordlog;

    @FXML
    private TextField usernamelog;


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private Error error;

    public void switchtoCreateAcc(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "CreateAccount.fxml");
    }

    public void switchtoLogin(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "hello-view.fxml");
    }

    public void switchForgetPass(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "ForgetPass.fxml");
    }
    public void loginbtn2(ActionEvent event) throws IOException {
        Static.name = usernamelog.getText();
        if (usernamelog.getText().isEmpty() || passwordlog.getText().isEmpty()) {
            error = new Error();
            error.setfield("Invalid information.");
        } else {
            String logdeta = "SELECT name, password FROM information WHERE name = ? and password = ? " +
                    "and role = 'user'";
            String logindeta = "SELECT name, password FROM information WHERE name = ? and password = ? " +
                    "and role = 'admin'";
            try {
                connect = Database.CODB();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                prepare = connect.prepareStatement(logdeta);
                prepare.setString(1, usernamelog.getText());
                prepare.setString(2, passwordlog.getText());
                resultSet = prepare.executeQuery();
                if (resultSet.next()) {
                    Switch s1 = new Switch();
                    s1.switchto(event, "UserPage.fxml");
                } else {
                    prepare = connect.prepareStatement(logindeta);
                    prepare.setString(1, usernamelog.getText());
                    prepare.setString(2, passwordlog.getText());
                    resultSet = prepare.executeQuery();
                    if(resultSet.next()){
                        Switch s1 = new Switch();
                        s1.switchto(event, "Adminpage.fxml");
                    }else {
                        error = new Error();
                        error.setfield("Invalid information.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}