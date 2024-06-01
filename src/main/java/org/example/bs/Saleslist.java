package org.example.bs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;


public class Saleslist extends HelloController implements Initializable {
    @FXML
    private Button buyBack;

    @FXML
    private TextField TI;


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
                        resultSet.getDate("date"));
                listdeta.add(CD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listdeta;
    }
    private Stage stage;
    private Scene scene;
    public void back(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "Adminpage.fxml");
    }

    private ObservableList<CustomerDeta> List;

    public void showDetalist() throws SQLException {
        List = detaList();
        buyID.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("ID"));
        buyName.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Name"));
        buyType.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Type"));
        buyPrice.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Price"));
        buyQuantity.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Quantity"));
        buyDate.setCellValueFactory(new PropertyValueFactory<CustomerDeta, String>("Date"));

        buytableview.setItems(List);
    }
    private Double totalincome;
    public void total() throws SQLException {
        String total = "SELECT SUM(Total) FROM customer";
        connect = Detabase.CODB();
        try{
            prepare = connect.prepareStatement(total);
            resultSet = prepare.executeQuery();
            if(resultSet.next()){
                totalincome = resultSet.getDouble("SUM(Total)");
            }
            TI.setText("$"+totalincome);

        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showDetalist();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            total();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
