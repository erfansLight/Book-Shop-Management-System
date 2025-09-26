package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private PasswordField passwordlog;
    @FXML
    private TextField usernamelog;

    private final UserRepository userRepository = new UserRepository();
    private final AlterBox alterBox = new AlterBox();

    public void switchtoCreateAcc(ActionEvent event) throws IOException {
        SwitchScene s1 = new SwitchScene();
        s1.switchto(event, "CreateAccount.fxml");
    }

    public void switchtoLogin(ActionEvent event) throws IOException {
        SwitchScene s1 = new SwitchScene();
        s1.switchto(event, "hello-view.fxml");
    }

    public void switchForgetPass(ActionEvent event) throws IOException {
        SwitchScene s1 = new SwitchScene();
        s1.switchto(event, "ForgetPass.fxml");
    }

//    public void loginbtn2(ActionEvent event) throws IOException {
//        String username = usernamelog.getText();
//        String password = passwordlog.getText();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            alterBox.error("Invalid information.");
//            return;
//        }
//
//        try {
//            User user = userRepository.findByUsernameAndPassword(username, password);
//
//            if (user != null) {
//                Constants.userId = user.getId();
//                Constants.name = user.getName();
//
//                SwitchScene s1 = new SwitchScene();
//                if ("user".equalsIgnoreCase(user.getRole())) {
//                    s1.switchto(event, "UserPage.fxml");
//                } else if ("admin".equalsIgnoreCase(user.getRole())) {
//                    s1.switchto(event, "AdminPage.fxml");
//                } else {
//                    alterBox.error("Invalid role.");
//                }
//            } else {
//                alterBox.error("Invalid username or password.");
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            alterBox.error("Database error.");
//        }
//    }
public void loginbtn2(ActionEvent event) throws IOException {
    String username = usernamelog.getText();
    String password = passwordlog.getText();

    if (username.isEmpty() || password.isEmpty()) {
        alterBox.error("Invalid information.");
        return;
    }

    try {
        User user = userRepository.findByUsernameAndPassword(username, password);

        if (user != null) {
            UserSession.setCurrentUser(user);

            SwitchScene s1 = new SwitchScene();
            if ("user".equalsIgnoreCase(user.getRole())) {
                s1.switchto(event, "UserPage.fxml");
            } else if ("admin".equalsIgnoreCase(user.getRole())) {
                s1.switchto(event, "AdminPage.fxml");
            } else {
                alterBox.error("Invalid role.");
            }
        } else {
            alterBox.error("Invalid username or password.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        alterBox.error("Database error.");
    }
}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
