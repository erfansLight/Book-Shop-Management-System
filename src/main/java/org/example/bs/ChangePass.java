package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static javafx.fxml.FXMLLoader.load;

public class ChangePass extends LoginController {
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


    public void resetbtn(ActionEvent event) {
        String username = fpusername.getText();
        String city = fpfavoritecity.getText();
        LocalDate birthday = fpbirthday.getValue();

        if (username.isEmpty() || city.isEmpty() || birthday == null) {
            AlterBox alterBox = new AlterBox();
            alterBox.error("Please fill out all fields.");
            return;
        }

        String query = "SELECT name FROM users WHERE name = ? AND city = ? AND Date = ?";

        try (Connection connect = Database.CODB();
             PreparedStatement prepare = connect.prepareStatement(query)) {

            prepare.setString(1, username);
            prepare.setString(2, city);
            prepare.setDate(3, java.sql.Date.valueOf(fpbirthday.getValue()));

            try (ResultSet resultSet = prepare.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setName(username);
                    UserSession.setCurrentUser(user);

                    SwitchScene s = new SwitchScene();
                    s.switchto(event, "ChangePass.fxml");
                } else {
                    AlterBox alterBox = new AlterBox();
                    alterBox.error("Invalid information.");
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            AlterBox alterBox = new AlterBox();
            alterBox.error("Database error.");
        }
    }

    public void Change(ActionEvent event) {
        String pass1 = nPass.getText();
        String pass2 = nPass2.getText();

        if (pass1.isEmpty() || pass2.isEmpty()) {
            AlterBox alterBox = new AlterBox();
            alterBox.error("Please fill out all fields.");
            return;
        }

        if (!pass1.equals(pass2)) {
            AlterBox alterBox = new AlterBox();
            alterBox.error("The passwords do not match.");
            return;
        }

        String updateQuery = "UPDATE users SET password = ? WHERE name = ?";

        try (Connection connect = Database.CODB();
             PreparedStatement prepare = connect.prepareStatement(updateQuery)) {

            prepare.setString(1, pass1);
            prepare.setString(2, UserSession.getCurrentUser().getName());

            int rows = prepare.executeUpdate();
            AlterBox alterBox = new AlterBox();
            if (rows > 0) {
                alterBox.update("Successfully updated.");
                switchtoLogin(event);
            } else {
                alterBox.error("Failed to update password.");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            AlterBox alterBox = new AlterBox();
            alterBox.error("Database error.");
        }
    }
}