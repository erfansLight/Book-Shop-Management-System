package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateAccount extends HelloController {
    @FXML
    private TextField QuestionCR;

    @FXML
    private DatePicker dateCR;

    @FXML
    private TextField email;

    @FXML
    private PasswordField passwordCR;

    @FXML
    private TextField usernameCR;
    private Error error;
    Connection connect;
    PreparedStatement prepare;
    ResultSet resultSet;
    ResultSet resultSetMail;


    public void createACbtn(ActionEvent event) {
        if (usernameCR.getText().isEmpty() || passwordCR.getText().isEmpty() ||
                QuestionCR.getText().isEmpty() || dateCR.getValue() == null ||
                email.getText().isEmpty()) {
            showError("Please fill out all fields.");
            return;
        }

        if (passwordCR.getText().length() < 5) {
            showError("Your password should have at least 5 characters.");
            return;
        }

        String insertUser = "INSERT INTO users (name, password, city, date, email, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connect = Database.CODB();
             PreparedStatement checkStmt = connect.prepareStatement(
                     "SELECT name, email FROM users WHERE name = ? OR email = ?")) {

            checkStmt.setString(1, usernameCR.getText());
            checkStmt.setString(2, email.getText());

            try (ResultSet rs = checkStmt.executeQuery()) {
                while (rs.next()) {
                    if (usernameCR.getText().equals(rs.getString("name"))) {
                        showError("This username has already been taken.");
                        return;
                    }
                    if (email.getText().equals(rs.getString("email"))) {
                        showError("This email has already been registered.");
                        return;
                    }
                }
            }

            try (PreparedStatement insertStmt = connect.prepareStatement(insertUser)) {
                insertStmt.setString(1, usernameCR.getText());
                insertStmt.setString(2, passwordCR.getText());
                insertStmt.setString(3, QuestionCR.getText());
                insertStmt.setDate(4, java.sql.Date.valueOf(dateCR.getValue()));
                insertStmt.setString(5, email.getText());
                insertStmt.setString(6, "user");

                insertStmt.executeUpdate();
            }

            mail sendEmail = new mail();
            sendEmail.email("sltanyh468@gmail.com", Static.mailPass, email.getText());

            showSuccess("Successfully registered account.");
            clearFields();
            switchtoLogin(event);

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void showError(String msg) {
        error = new Error();
        error.setfield(msg);
    }

    private void showSuccess(String msg) {
        error = new Error();
        error.update(msg);
    }

    private void clearFields() {
        usernameCR.setText("");
        passwordCR.setText("");
        QuestionCR.setText("");
        email.setText("");
    }
}
