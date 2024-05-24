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

public class Checkpage implements Initializable {
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    @FXML
    private TableColumn<CustomerDeta, String> checkDate;

    @FXML
    private TableColumn<CustomerDeta, String> checkID;

    @FXML
    private TableColumn<CustomerDeta, String> checkName;

    @FXML
    private TableColumn<CustomerDeta, String> checkPrice;

    @FXML
    private TableColumn<CustomerDeta, String> checkQuantity;

    @FXML
    private TableColumn<CustomerDeta, String> checkType;

    @FXML
    private TableView<CustomerDeta> checktable;

    @FXML
    private TextField textQu;
    public ObservableList<CustomerDeta> detaList() throws SQLException {
        ObservableList<CustomerDeta> listdeta = FXCollections.observableArrayList();
        String myadmin = "SELECT * FROM checkpage";
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
        checkID.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("ID"));
        checkName.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Name"));
        checkType.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Type"));
        checkPrice.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Price"));
        checkQuantity.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Quantity"));
        checkDate.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Date"));

        checktable.setItems(List);
    }
    public void select(){
        CustomerDeta customerDeta = checktable.getSelectionModel().getSelectedItem();

        textQu.setText(String.valueOf(customerDeta.getQuantity()));
        Static.cid = customerDeta.getid();
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
