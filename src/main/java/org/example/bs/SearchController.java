package org.example.bs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class SearchController implements Initializable {
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    @FXML
    private TableColumn<BookData, String> searchDate;

    @FXML
    private TableColumn<BookData, String> searchID;

    @FXML
    private TableColumn<BookData, String> searchauthor;

    @FXML
    private TableColumn<BookData, String> searchdis;

    @FXML
    private TableColumn<BookData, String> searchname;

    @FXML
    private TableColumn<BookData, String> searchprice;

    @FXML
    private TableColumn<BookData, String> searchtype;
    @FXML
    private TableView<BookData> searchtable;

    @FXML
    private TextField textsearch;

    public void search() throws SQLException {
        Static.search = textsearch.getText();
        showlist();
    }
    public ObservableList<BookData> detaList() throws SQLException {
        ObservableList<BookData> listdeta = FXCollections.observableArrayList();
        String myadmin = "SELECT * FROM bookdeta WHERE bookname LIKE '%"+Static.search
                +"%' OR Author LIKE '%"+Static.search+"%'";
        connect = Database.CODB();

        try {
            prepare = connect.prepareStatement(myadmin);
            resultSet = prepare.executeQuery();
            BookData BD;
            while (resultSet.next()) {
                BD = new BookData();
                BD.setId(resultSet.getInt("id"));
                BD.setID(resultSet.getString("bookid"));
                BD.setName(resultSet.getString("bookname"));
                BD.setType(resultSet.getString("type"));
                BD.setDescription(resultSet.getString("Description"));
                BD.setPrice(resultSet.getDouble("price"));
                BD.setAuthor(resultSet.getString("Author"));
                BD.setDate(resultSet.getDate("date"));
                listdeta.add(BD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listdeta;
    }

    private ObservableList<BookData> LI;

    public void showlist() throws SQLException {
        LI = detaList();
        searchID.setCellValueFactory(new PropertyValueFactory<BookData, String>("ID"));
        searchname.setCellValueFactory(new PropertyValueFactory<BookData, String>("Name"));
        searchtype.setCellValueFactory(new PropertyValueFactory<BookData, String>("Type"));
        searchdis.setCellValueFactory(new PropertyValueFactory<BookData, String>("Description"));
        searchprice.setCellValueFactory(new PropertyValueFactory<BookData, String>("Price"));
        searchauthor.setCellValueFactory(new PropertyValueFactory<BookData, String>("Author"));
        searchDate.setCellValueFactory(new PropertyValueFactory<BookData, String>("Date"));

        searchtable.setItems(LI);
    }
    public void back(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "UserPage.fxml");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
