package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateAccount extends HelloController{
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

    public void createACbtn(ActionEvent event) throws SQLException {
        if (usernameCR.getText().isEmpty() || passwordCR.getText().isEmpty() ||
                QuestionCR.getText().isEmpty() || dateCR.getValue() == null ||
                email.getText().isEmpty()) {
            error = new Error();
            error.setfield("Please fill out all field");
        } else if (passwordCR.getText().length() < 5) {
            error = new Error();
            error.setfield("Your password should have at least 5 characters.");
        } else {
            String signdeta = "INSERT INTO information (name , password, city,Date)" +
                    "VALUES(?,?,?,?)";
            connect = Database.CODB();
            try {
                String checkname = "SELECT name FROM information WHERE name = '" +
                        usernameCR.getText() + "'";
                prepare = connect.prepareStatement(checkname);
                resultSet = prepare.executeQuery();
                if (resultSet.next()) {
                    error = new Error();
                    error.setfield("This username has already been taken.");
                } else {
                    Static.createname = usernameCR.getText();
                    mail il = new mail();
                    il.email("sltanyh468@gmail.com","vtesxybfbodpkhvq",
                            email.getText());
                    prepare = connect.prepareStatement(signdeta);
                    prepare.setString(1, usernameCR.getText());
                    prepare.setString(2, passwordCR.getText());
                    prepare.setString(3, QuestionCR.getText());
                    prepare.setString(4, String.valueOf(dateCR.getValue()));
                    //prepare.setString(5,roleCR.getText());
                    prepare.executeUpdate();

                    error = new Error();
                    error.update("Successfully registered Account.");

                    usernameCR.setText("");
                    passwordCR.setText("");
                    QuestionCR.setText("");
                    email.setText("");
                    //dateCR.setValue(LocalDate.parse("2020,6,2"));
                    switchtoLogin(event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
