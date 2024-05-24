package org.example.bs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class CustomerPage extends HelloController implements Initializable {
    @FXML
    private Button buyBack;


    @FXML
    private TableView<CustomerDeta> buytableview;

    @FXML
    private TableColumn<CustomerDeta, String> buyDate;

    @FXML
    private TableColumn<CustomerDeta, String> buyID;

    @FXML
    private TableColumn<CustomerDeta, String> buyName;

    @FXML
    private TableColumn<CustomerDeta, String> buyPrice;

    @FXML
    private TableColumn<CustomerDeta, String> buyQuantity;

    @FXML
    private TableColumn<CustomerDeta, String> buyType;

    @FXML
    private TableColumn<CustomerDeta, String> buycustomName;

    @FXML
    private TextField buytotalincome;
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet resultSet;

    public ObservableList<CustomerDeta> detaList() throws SQLException {
        ObservableList<CustomerDeta> listdeta = FXCollections.observableArrayList();
        String myadmin = "SELECT * FROM customer";
        connect = Detabase.CODB();

        try {
            prepare = connect.prepareStatement(myadmin);
            resultSet = prepare.executeQuery();
            CustomerDeta CD;
            while (resultSet.next()) {
                CD = new CustomerDeta(resultSet.getInt("id"),
                        resultSet.getString("bookid"),
                        resultSet.getString("bookname"),
                        resultSet.getString("type"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("Quantity"),
                        resultSet.getString("customername"),
                        resultSet.getDate("date"));
                listdeta.add(CD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listdeta;
    }

    private ObservableList<CustomerDeta> List;

    public void showDetalist() throws SQLException {
        List = detaList();
        buyID.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("ID"));
        buyName.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Name"));
        buyType.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Type"));
        buyPrice.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Price"));
        buyQuantity.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Quantity"));
        buycustomName.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("CustomerName"));
        buyDate.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Date"));

        buytableview.setItems(List);
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
