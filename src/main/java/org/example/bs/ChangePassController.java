package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class ChangePassController {

    @FXML
    private TextField fpusername;
    @FXML
    private TextField fpfavoritecity;
    @FXML
    private DatePicker fpbirthday;
    @FXML
    private PasswordField nPass;
    @FXML
    private PasswordField nPass2;

    private final AuthService authService = new AuthService();
    private final AlterBox alterBox = new AlterBox();

    @FXML
    public void resetbtn(ActionEvent event) {
        String username = fpusername.getText();
        String city = fpfavoritecity.getText();
        LocalDate birthday = fpbirthday.getValue();

        if (username.isEmpty() || city.isEmpty() || birthday == null) {
            alterBox.error("Please fill out all fields.");
            return;
        }

        try {
            User user = authService.findUserByDetails(username, city, birthday);
            if (user != null) {
//                UserSession.setCurrentUser(user);
//                alterBox.update("User verified. You can now change your password.");
//                SwitchScene s1 = new SwitchScene();
//                s1.switchto(event, "ChangePass.fxml");
                User verifiedUser = new User();
                verifiedUser.setName(username);
                UserSession.setCurrentUser(verifiedUser);

                SwitchScene s = new SwitchScene();
                s.switchto(event, "ChangePass.fxml");
            } else {
                alterBox.error("Invalid information.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            alterBox.error("Database error.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void switchForgetPass(ActionEvent event) throws IOException {
        new SwitchScene().switchto(event, "ForgetPass.fxml");
    }

    @FXML
    public void Change(ActionEvent event) {
        String pass1 = nPass.getText();
        String pass2 = nPass2.getText();

        if (pass1.isEmpty() || pass2.isEmpty()) {
            alterBox.error("Please fill out all fields.");
            return;
        }

        if (!pass1.equals(pass2)) {
            alterBox.error("The passwords do not match.");
            return;
        }

        try {
            if (authService.changePassword(UserSession.getCurrentUser().getName(), pass1)) {
                alterBox.update("Successfully updated password.");
                new SwitchScene().switchto(event, "hello-view.fxml");
            } else {
                alterBox.error("Failed to update password.");
            }
        } catch (SQLException | java.io.IOException e) {
            e.printStackTrace();
            alterBox.error("Database error.");
        }
    }

    @FXML
    public void switchtoLogin(ActionEvent event) throws IOException {
        SwitchScene s1 = new SwitchScene();
        s1.switchto(event, "hello-view.fxml");
    }
}
