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
    private TableColumn<BookDeta,String> wishAuthor;

    @FXML
    private TableColumn<BookDeta,String> wishDate;

    @FXML
    private TableColumn<BookDeta,String> wishDescription;

    @FXML
    private TableColumn<BookDeta,String> wishID;

    @FXML
    private TableColumn<BookDeta,String> wishPrice;

    @FXML
    private TableColumn<BookDeta,String> wishname;

    @FXML
    private TableView<BookDeta> wishtable;

    @FXML
    private TableColumn<BookDeta,String> wishtype;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private Stage stage;
    private Scene scene;

    public ObservableList<BookDeta> detaList() throws SQLException {
        ObservableList<BookDeta> listdeta = FXCollections.observableArrayList();
        String myadmin = "SELECT * FROM wishlist WHERE customername = '"+Static.name+"'";
        connect = Detabase.CODB();

        try{
            prepare = connect.prepareStatement(myadmin);
            resultSet = prepare.executeQuery();
            BookDeta bookDeta;
            while(resultSet.next()){
                bookDeta = new BookDeta(resultSet.getInt("id"),
                        resultSet.getString("bookid"),
                        resultSet.getString("bookname"),
                        resultSet.getString("type"),
                        resultSet.getString("Description"),
                        resultSet.getDouble("price"),
                        resultSet.getString("Author"),
                        resultSet.getDate("date"));
                listdeta.add(bookDeta);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listdeta;
    }
    private ObservableList<BookDeta> List;
    public void showDetalist() throws SQLException {
        List = detaList();
        wishID.setCellValueFactory(new PropertyValueFactory<BookDeta,String>("ID"));
        wishname.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Name"));
        wishtype.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Type"));
        wishDescription.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Description"));
        wishPrice.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Price"));
        wishAuthor.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Author"));
        wishDate.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Date"));

        wishtable.setItems(List);
    }
    public void back(ActionEvent event) throws IOException {
        Parent root = load(getClass().getResource("UserPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
