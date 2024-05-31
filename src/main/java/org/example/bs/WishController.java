package org.example.bs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class WishController implements Initializable {
    @FXML
    private TextField delete;

    @FXML
    private TableColumn<wishDeta, String> wishAuthor;

    @FXML
    private TableColumn<wishDeta, String> wishDate;

    @FXML
    private TableColumn<wishDeta, String> wishDescription;

    @FXML
    private TableColumn<wishDeta, String> wishID;

    @FXML
    private TableColumn<wishDeta, String> wishPrice;

    @FXML
    private TableColumn<wishDeta, String> wishname;

    @FXML
    private TableView<wishDeta> wishtable;

    @FXML
    private TableColumn<wishDeta, String> wishtype;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private Error error;

    public ObservableList<wishDeta> detaList() throws SQLException {
        ObservableList<wishDeta> listdeta = FXCollections.observableArrayList();
        String myadmin = "SELECT * FROM wishlist WHERE customername = '" + Static.name + "'";
        connect = Detabase.CODB();

        try {
            prepare = connect.prepareStatement(myadmin);
            resultSet = prepare.executeQuery();
            wishDeta wd;
            while (resultSet.next()) {
                wd = new wishDeta(resultSet.getInt("id"),
                        resultSet.getString("bookid"),
                        resultSet.getString("bookname"),
                        resultSet.getString("type"),
                        resultSet.getString("Description"),
                        resultSet.getDouble("price"),
                        resultSet.getString("Author"),
                        resultSet.getDate("date"));
                listdeta.add(wd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listdeta;
    }

    private ObservableList<wishDeta> List;

    public void showDetalist() throws SQLException {
        List = detaList();
        wishID.setCellValueFactory(new PropertyValueFactory<wishDeta, String>("ID"));
        wishname.setCellValueFactory(new PropertyValueFactory<wishDeta, String>("Name"));
        wishtype.setCellValueFactory(new PropertyValueFactory<wishDeta, String>("Type"));
        wishDescription.setCellValueFactory(new PropertyValueFactory<wishDeta, String>("Description"));
        wishPrice.setCellValueFactory(new PropertyValueFactory<wishDeta, String>("Price"));
        wishAuthor.setCellValueFactory(new PropertyValueFactory<wishDeta, String>("Author"));
        wishDate.setCellValueFactory(new PropertyValueFactory<wishDeta, String>("Date"));

        wishtable.setItems(List);
    }

    public void back(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "UserPage.fxml");
    }

    public void Select() {
        wishDeta w1 = wishtable.getSelectionModel().getSelectedItem();
        delete.setText(w1.getAuthor());
        Static.bookname = w1.getName();
    }

    public void Deletebtn() throws SQLException {
        String delet = "DELETE FROM wishlist WHERE bookname = '" +Static.bookname+"' AND customername = '"
                +Static.name+"' AND Author = '"+delete.getText()+"'";
        try {
            prepare = connect.prepareStatement(delet);
            prepare.executeUpdate();
            error = new Error();
            error.update("Successfully deleted");

            showDetalist();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showDetalist();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
