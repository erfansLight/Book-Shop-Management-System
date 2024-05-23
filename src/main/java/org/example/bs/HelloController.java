package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.sql.*;

import static javafx.fxml.FXMLLoader.load;

public class HelloController implements Initializable {
    @FXML
    private TextField QuestionCR;

    @FXML
    private Button createbtnCR;

    @FXML
    private DatePicker dateCR;

    @FXML
    private Button loginbtnCR;

    @FXML
    private PasswordField passwordCR;

    @FXML
    private TextField usernameCR;

    @FXML
    private Button createbtnlog;

    @FXML
    private Hyperlink forgetpass;

    @FXML
    private Button loginbtnlog;

    @FXML
    private PasswordField passwordlog;

    @FXML
    private TextField usernamelog;


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private Stage stage;
    private Scene scene;
    private Parent parent;
    private ActionEvent event;
    private Error error;
    private Alert alert;
    private String rename;

    public void switchtoCreateAcc(ActionEvent event) throws IOException {
        Parent root = load(getClass().getResource("CreateAccount.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchtoLogin(ActionEvent event) throws IOException {
        Parent root = load(getClass().getResource("hello-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchForgetPass(ActionEvent event) throws IOException {
        Parent root = load(getClass().getResource("ForgetPass.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void loginbtn2(ActionEvent event3) throws IOException {
        if (usernamelog.getText().isEmpty() || passwordlog.getText().isEmpty()) {
            error = new Error();
            error.setfield("Invalid information.");
        } else {
            String logdeta = "SELECT name, password FROM information WHERE name = ? and password = ? " +
                    "and role = 'user'";
            try {
                connect = Detabase.CODB();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                prepare = connect.prepareStatement(logdeta);
                prepare.setString(1, usernamelog.getText());
                prepare.setString(2, passwordlog.getText());
                resultSet = prepare.executeQuery();
                if (resultSet.next()) {
                    Parent root = load(getClass().getResource("ForgetPass.fxml"));//*********
                    stage = (Stage) ((Node) event3.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    error = new Error();
                    error.setfield("Invalid information.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loginbtn(ActionEvent event2) throws IOException {
        if (usernamelog.getText().isEmpty() || passwordlog.getText().isEmpty()) {
            error = new Error();
            error.setfield("Invalid information.");
        } else {
            String logindeta = "SELECT name, password FROM information WHERE name = ? and password = ? " +
                    "and role = 'admin'";
            try {
                connect = Detabase.CODB();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                prepare = connect.prepareStatement(logindeta);
                prepare.setString(1, usernamelog.getText());
                prepare.setString(2, passwordlog.getText());
                resultSet = prepare.executeQuery();
                if (resultSet.next()) {
                    Parent root = load(getClass().getResource("Adminpage.fxml"));
                    stage = (Stage) ((Node) event2.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    error = new Error();
                    error.setfield("Invalid information.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createACbtn() throws SQLException {
        if (usernameCR.getText().isEmpty() || passwordCR.getText().isEmpty() ||
                QuestionCR.getText().isEmpty() || dateCR.getValue() == null) {//*********
            error = new Error();
            error.setfield("Please fill out all field");
        } else if (passwordCR.getText().length() < 5) {
            error = new Error();
            error.setfield("Your password should have at least 5 characters.");
        } else {
            String signdeta = "INSERT INTO information (name , password, city,Date)" +
                    "VALUES(?,?,?,?)";
            connect = Detabase.CODB();
            try {
                String checkname = "SELECT name FROM information WHERE name = '" +
                        usernameCR.getText() + "'";
                prepare = connect.prepareStatement(checkname);
                resultSet = prepare.executeQuery();
                if (resultSet.next()) {
                    error = new Error();
                    error.setfield("This username has already been taken.");
                } else {
                    prepare = connect.prepareStatement(signdeta);
                    prepare.setString(1, usernameCR.getText());
                    prepare.setString(2, passwordCR.getText());
                    prepare.setString(3, QuestionCR.getText());
                    prepare.setString(4, String.valueOf(dateCR.getValue()));//*******
                    prepare.executeUpdate();

                    error = new Error();
                    error.update("Successfully registered Account.");

                    usernameCR.setText("");
                    passwordCR.setText("");
                    QuestionCR.setText("");
                    dateCR.setValue(LocalDate.parse("2020-05-01"));
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