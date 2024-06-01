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



public class Saleslist extends HelloController implements Initializable {
    @FXML
    private Button buyBack;

    @FXML
    private TextField TI;


    @FXML
    private TableView<SalesDeta> buytableview;

    @FXML
    private TableColumn<SalesDeta, String> buyDate;

    @FXML
    private TableColumn<SalesDeta, String> buyID;

    @FXML
    private TableColumn<SalesDeta, String> buyName;

    @FXML
    private TableColumn<SalesDeta, String> buyPrice;

    @FXML
    private TableColumn<SalesDeta, String> buyQuantity;

    @FXML
    private TableColumn<SalesDeta, String> buyType;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;

    public ObservableList<SalesDeta> detaList() throws SQLException {
        ObservableList<SalesDeta> listdeta = FXCollections.observableArrayList();
        String myadmin = "SELECT * FROM customer";
        connect = Detabase.CODB();

        try {
            prepare = connect.prepareStatement(myadmin);
            resultSet = prepare.executeQuery();
            SalesDeta CD;
            while (resultSet.next()) {
                CD = new SalesDeta();
                CD.setId(resultSet.getInt("id"));
                CD.setID(resultSet.getString("bookid"));
                CD.setName(resultSet.getString("bookname"));
                CD.setType(resultSet.getString("type"));
                CD.setPrice(resultSet.getDouble("price"));
                CD.setQuantity(resultSet.getInt("Quantity"));
                CD.setDate(resultSet.getDate("date"));
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

    private ObservableList<SalesDeta> List;

    public void showDetalist() throws SQLException {
        List = detaList();
        buyID.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("ID"));
        buyName.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("Name"));
        buyType.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("Type"));
        buyPrice.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("Price"));
        buyQuantity.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("Quantity"));
        buyDate.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("Date"));

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
