package org.example.bs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableColumn<BookDeta, String> searchDate;

    @FXML
    private TableColumn<BookDeta, String> searchID;

    @FXML
    private TableColumn<BookDeta, String> searchauthor;

    @FXML
    private TableColumn<BookDeta, String> searchdis;

    @FXML
    private TableColumn<BookDeta, String> searchname;

    @FXML
    private TableColumn<BookDeta, String> searchprice;

    @FXML
    private TableColumn<BookDeta, String> searchtype;
    @FXML
    private TableView<BookDeta> searchtable;

    @FXML
    private TextField textsearch;

    public void search() throws SQLException {
        Static.search = textsearch.getText();
        showlist();
    }
    public ObservableList<BookDeta> detaList() throws SQLException {
        ObservableList<BookDeta> listdeta = FXCollections.observableArrayList();
        String myadmin = "SELECT * FROM bookdeta WHERE bookname = '"+Static.search+"'";
        connect = Detabase.CODB();

        try {
            prepare = connect.prepareStatement(myadmin);
            resultSet = prepare.executeQuery();
            BookDeta BD;
            while (resultSet.next()) {
                BD = new BookDeta(resultSet.getInt("id"),
                        resultSet.getString("bookid"),
                        resultSet.getString("bookname"),
                        resultSet.getString("type"),
                        resultSet.getString("Description"),
                        resultSet.getDouble("price"),
                        resultSet.getString("Author"),
                        resultSet.getDate("date"));
                listdeta.add(BD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listdeta;
    }

    private ObservableList<BookDeta> LI;

    public void showlist() throws SQLException {
        LI = detaList();
        searchID.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("ID"));
        searchname.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Name"));
        searchtype.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Type"));
        searchdis.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Description"));
        searchprice.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Price"));
        searchauthor.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Author"));
        searchDate.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Date"));

        searchtable.setItems(LI);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
