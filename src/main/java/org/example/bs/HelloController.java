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

        String username = usernamelog.getText();
        String password = passwordlog.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Error error = new Error();
            error.setfield("Invalid information.");
            return;
        }

        String query = "SELECT role FROM users WHERE name = ? AND password = ?";

        try (Connection connect = Database.CODB();
             PreparedStatement prepare = connect.prepareStatement(query)) {

            prepare.setString(1, username);
            prepare.setString(2, password);

            try (ResultSet resultSet = prepare.executeQuery()) {
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    Switch s1 = new Switch();
                    if ("user".equalsIgnoreCase(role)) {
                        s1.switchto(event, "UserPage.fxml");
                    } else if ("admin".equalsIgnoreCase(role)) {
                        s1.switchto(event, "Adminpage.fxml");
                    } else {
                        Error error = new Error();
                        error.setfield("Invalid role.");
                    }
                } else {
                    Error error = new Error();
                    error.setfield("Invalid information.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Error error = new Error();
            error.setfield("Database error.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}