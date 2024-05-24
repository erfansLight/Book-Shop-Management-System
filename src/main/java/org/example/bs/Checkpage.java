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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class Checkpage implements Initializable {
    private Connection connect;
    private PreparedStatement prepare;
    private Error error;
    private ResultSet resultSet;
    @FXML
    private TextField textcheckID;
    private Stage stage;
    private Scene scene;

    @FXML
    private TextArea address;

    @FXML
    private TextField textcheckname;

    @FXML
    private TextField textcheckname2;

    @FXML
    private TextField textcheckprice;

    @FXML
    private TextField textchecktype;

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

    public void select() {
        CustomerDeta customerDeta = checktable.getSelectionModel().getSelectedItem();

        textQu.setText(String.valueOf(customerDeta.getQuantity()));
        textcheckID.setText(customerDeta.getID());
        textcheckname.setText(customerDeta.getName());
        textchecktype.setText(customerDeta.getType());
        textcheckprice.setText(String.valueOf(customerDeta.getPrice()));
        Static.cid = customerDeta.getid();
        Static.cdate = String.valueOf(customerDeta.getDate());
    }

    public void buy(ActionEvent event) throws SQLException, IOException {
        if(address.getText().isEmpty()){
            error = new Error();
            error.setfield("Please fill out all field");
        }else {
            connect = Detabase.CODB();
            try {
                String insertdeta = "INSERT INTO customer" +
                        " (bookid, bookname, type, price, Quantity,customername, date)" + "VALUES(?,?,?,?,?,?,?)";
                prepare = connect.prepareStatement(insertdeta);
                prepare.setString(1, textcheckID.getText());
                prepare.setString(2, textcheckname.getText());
                prepare.setString(3, textchecktype.getText());
                prepare.setString(4, textcheckprice.getText());
                prepare.setString(5, textQu.getText());
                prepare.setString(6, Static.name);
                java.util.Date date = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(7, String.valueOf(sqlDate));

                prepare.executeUpdate();


            } catch (Exception e) {
                e.printStackTrace();
            }
            Deletebtn();
            address.setText("");
            Parent root = load(getClass().getResource("UserPage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void Deletebtn() {
        if (Static.cid == 0) {
            error = new Error();
            error.setfield("Please fill out all field");
        } else {
            String delet = "DELETE FROM checkpage WHERE id = " + Static.cid;
            try {
                prepare = connect.prepareStatement(delet);
                prepare.executeUpdate();
                error = new Error();
                error.update("Successful");

                showDetalist();
                textQu.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update() throws SQLException {
        if (Static.cid == 0 || textQu.getText().isEmpty()) {
            error = new Error();
            error.setfield("Please fill out all field");
        } else {
            String Update = "UPDATE checkpage SET bookid = '" + textcheckID.getText() + "', bookname = '" +
                    textcheckname.getText() + "', type = '" + textchecktype.getText() + "', price = '" +
                    textcheckprice.getText() + "', Quantity = '" + textQu.getText() +
                    "', Date = '" + Static.cdate + "' WHERE id = " + Static.cid;

            connect = Detabase.CODB();
            try {

                prepare = connect.prepareStatement(Update);
                prepare.executeUpdate();

                error = new Error();
                error.update("Successfully updated.");

                showDetalist();
                textQu.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
