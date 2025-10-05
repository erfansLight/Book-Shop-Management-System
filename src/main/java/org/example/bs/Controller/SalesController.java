package org.example.bs.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.bs.Model.Sales;
import org.example.bs.Service.SalesService;
import org.example.bs.SwitchScene;

import java.io.IOException;
import java.sql.SQLException;

public class SalesController {

    @FXML
    private TextField TI;

    @FXML
    private TableView<Sales> buytableview;
    @FXML
    private TableColumn<Sales, String> buyID;
    @FXML
    private TableColumn<Sales, String> buyName;
    @FXML
    private TableColumn<Sales, String> buyType;
    @FXML
    private TableColumn<Sales, Double> buyPrice;
    @FXML
    private TableColumn<Sales, Integer> buyQuantity;
    @FXML
    private TableColumn<Sales, String> buyDate;

    private final SalesService salesService = new SalesService();

    @FXML
    public void initialize() {
        setupColumns();
        loadSalesData();
        loadTotalIncome();
    }

    private void setupColumns() {
        buyID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        buyName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        buyType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        buyPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        buyQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        buyDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
    }

    private void loadSalesData() {
        try {
            ObservableList<Sales> salesList = FXCollections.observableArrayList(salesService.getAllSales());
            buytableview.setItems(salesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTotalIncome() {
        try {
            double total = salesService.calculateTotalIncome();
            TI.setText("$" + total);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        new SwitchScene().switchto(event, "AdminPage.fxml");
    }
}

