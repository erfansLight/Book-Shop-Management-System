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
    private TableColumn<Book, String> searchDate;

    @FXML
    private TableColumn<Book, String> searchID;

    @FXML
    private TableColumn<Book, String> searchauthor;

    @FXML
    private TableColumn<Book, String> searchdis;

    @FXML
    private TableColumn<Book, String> searchname;

    @FXML
    private TableColumn<Book, String> searchprice;

    @FXML
    private TableColumn<Book, String> searchtype;
    @FXML
    private TableView<Book> searchtable;

    @FXML
    private TextField textsearch;

    public void search() throws SQLException {
        Constants.search = textsearch.getText();
        showlist();
    }
    public ObservableList<Book> dataList() throws SQLException {
        ObservableList<Book> list_data = FXCollections.observableArrayList();
        String sql = "SELECT * FROM books WHERE bookname LIKE '%"+ Constants.search
                +"%' OR Author LIKE '%"+ Constants.search+"%'";
        connect = Database.CODB();

        try {
            prepare = connect.prepareStatement(sql);
            resultSet = prepare.executeQuery();
            Book BD;
            while (resultSet.next()) {
                BD = new Book();
                BD.setId(resultSet.getInt("id"));
                BD.setID(resultSet.getString("bookid"));
                BD.setName(resultSet.getString("bookname"));
                BD.setType(resultSet.getString("type"));
                BD.setDescription(resultSet.getString("Description"));
                BD.setPrice(resultSet.getDouble("price"));
                BD.setAuthor(resultSet.getString("Author"));
                BD.setDate(resultSet.getDate("date"));
                list_data.add(BD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_data;
    }

    private ObservableList<Book> LI;

    public void showlist() throws SQLException {
        LI = dataList();
        searchID.setCellValueFactory(new PropertyValueFactory<Book, String>("ID"));
        searchname.setCellValueFactory(new PropertyValueFactory<Book, String>("Name"));
        searchtype.setCellValueFactory(new PropertyValueFactory<Book, String>("Type"));
        searchdis.setCellValueFactory(new PropertyValueFactory<Book, String>("Description"));
        searchprice.setCellValueFactory(new PropertyValueFactory<Book, String>("Price"));
        searchauthor.setCellValueFactory(new PropertyValueFactory<Book, String>("Author"));
        searchDate.setCellValueFactory(new PropertyValueFactory<Book, String>("Date"));

        searchtable.setItems(LI);
    }
    public void back(ActionEvent event) throws IOException {
        SwitchScene s1 = new SwitchScene();
        s1.switchto(event, "UserPage.fxml");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
