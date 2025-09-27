package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class CreateAccountController {

    @FXML
    private TextField usernameCR;
    @FXML
    private PasswordField passwordCR;
    @FXML
    private TextField QuestionCR;
    @FXML
    private DatePicker dateCR;
    @FXML
    private TextField email;

    private final AuthService authService = new AuthService();
    private final AlterBox alterBox = new AlterBox();

    public void createACbtn(ActionEvent event) {
        String username = usernameCR.getText();
        String password = passwordCR.getText();
        String city = QuestionCR.getText();
        LocalDate date = dateCR.getValue();
        String mail = email.getText();

        if (username.isEmpty() || password.isEmpty() || city.isEmpty() || date == null || mail.isEmpty()) {
            alterBox.error("Please fill out all fields.");
            return;
        }

        if (password.length() < 5) {
            alterBox.error("Your password should have at least 5 characters.");
            return;
        }

        User newUser = new User();
        newUser.setName(username);
        newUser.setPassword(password);
        newUser.setCity(city);
        newUser.setDate(date);
        newUser.setEmail(mail);
        newUser.setRole("user");

        try {
            if (authService.register(newUser)) {
                alterBox.update("Successfully registered account.");
                clearFields();
                new SwitchScene().switchto(event, "hello-view.fxml");
            } else {
                alterBox.error("Registration failed. Username or email may already exist.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            alterBox.error("Database error: " + e.getMessage());
        }
    }

    private void clearFields() {
        usernameCR.clear();
        passwordCR.clear();
        QuestionCR.clear();
        email.clear();
        dateCR.setValue(null);
    }

    public void switchtoLogin(ActionEvent event) throws IOException {
        SwitchScene s1 = new SwitchScene();
        s1.switchto(event, "hello-view.fxml");
    }
}
