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

public class CheckpageController implements Initializable {
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
    private TableColumn<SalesDeta, String> checkDate;

    @FXML
    private TableColumn<SalesDeta, String> checkID;

    @FXML
    private TableColumn<SalesDeta, String> checkName;

    @FXML
    private TableColumn<SalesDeta, String> checkPrice;

    @FXML
    private TableColumn<SalesDeta, String> checkQuantity;

    @FXML
    private TableColumn<SalesDeta, String> checkType;

    @FXML
    private TableView<SalesDeta> checktable;

    @FXML
    private TextField textQu;

    public ObservableList<SalesDeta> detaList() throws SQLException {
        ObservableList<SalesDeta> listdeta = FXCollections.observableArrayList();
        String myadmin = "SELECT * FROM checkpage";
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

    private ObservableList<SalesDeta> List;

    public void showDetalist() throws SQLException {
        List = detaList();
        checkID.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("ID"));
        checkName.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("Name"));
        checkType.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("Type"));
        checkPrice.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("Price"));
        checkQuantity.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("Quantity"));
        checkDate.setCellValueFactory(new PropertyValueFactory<SalesDeta, String>("Date"));

        checktable.setItems(List);
    }

    public void select() {
        SalesDeta customerDeta = checktable.getSelectionModel().getSelectedItem();

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
                        " (bookid, bookname, type, price, Quantity,customername, Total, date)" + "VALUES(?,?,?,?,?,?,?,?)";
                prepare = connect.prepareStatement(insertdeta);
                prepare.setString(1, textcheckID.getText());
                prepare.setString(2, textcheckname.getText());
                prepare.setString(3, textchecktype.getText());
                prepare.setString(4, textcheckprice.getText());
                prepare.setString(5, textQu.getText());
                prepare.setString(6, Static.name);
                prepare.setDouble(7, Double.parseDouble(textcheckprice.getText())*
                        Double.parseDouble(textQu.getText()));
                java.util.Date date = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(8, String.valueOf(sqlDate));

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
