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

public class CheckpageController extends WishController implements Initializable {
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
    private TableColumn<SalesData, String> checkDate;

    @FXML
    private TableColumn<SalesData, String> checkID;

    @FXML
    private TableColumn<SalesData, String> checkName;

    @FXML
    private TableColumn<SalesData, String> checkPrice;

    @FXML
    private TableColumn<SalesData, String> checkQuantity;

    @FXML
    private TableColumn<SalesData, String> checkType;

    @FXML
    private TableView<SalesData> checktable;

    @FXML
    private TextField textQu;

    public ObservableList<SalesData> dataListCart() throws SQLException {
        ObservableList<SalesData> list_data = FXCollections.observableArrayList();
        String sql = "SELECT c.book_id, b.bookid, b.bookname, b.type, b.price, c.quantity, c.added_at " +
                "FROM cart c " +
                "JOIN books b ON c.book_id = b.id " +
                "WHERE c.user_id = ?";

        connect = Database.CODB();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Static.userId);
            resultSet = prepare.executeQuery();

            while (resultSet.next()) {
                SalesData CD = new SalesData();
                CD.setId(resultSet.getInt("book_id"));
                CD.setID(resultSet.getString("bookid"));
                CD.setName(resultSet.getString("bookname"));
                CD.setType(resultSet.getString("type"));
                CD.setPrice(resultSet.getDouble("price"));
                CD.setQuantity(resultSet.getInt("quantity"));
                CD.setDate(resultSet.getDate("added_at"));
                list_data.add(CD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_data;
    }

    private ObservableList<SalesData> ListCart;

    public void showCartData() throws SQLException {
        ListCart = dataListCart();
        checkID.setCellValueFactory(new PropertyValueFactory<SalesData, String>("ID"));
        checkName.setCellValueFactory(new PropertyValueFactory<SalesData, String>("Name"));
        checkType.setCellValueFactory(new PropertyValueFactory<SalesData, String>("Type"));
        checkPrice.setCellValueFactory(new PropertyValueFactory<SalesData, String>("Price"));
        checkQuantity.setCellValueFactory(new PropertyValueFactory<SalesData, String>("Quantity"));
        checkDate.setCellValueFactory(new PropertyValueFactory<SalesData, String>("Date"));

        checktable.setItems(ListCart);
    }

    public void select() {
        SalesData customerData = checktable.getSelectionModel().getSelectedItem();

        textQu.setText(String.valueOf(customerData.getQuantity()));
        textcheckID.setText(customerData.getID());
        textcheckname.setText(customerData.getName());
        textchecktype.setText(customerData.getType());
        textcheckprice.setText(String.valueOf(customerData.getPrice()));
        Static.bookId = customerData.getID();
    }

    public void buy(ActionEvent event) throws SQLException, IOException {
        if (address.getText().isEmpty()) {
            error = new Error();
            error.setfield("Please fill out all field");
        } else {
            connect = Database.CODB();
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
                prepare.setDouble(7, Double.parseDouble(textcheckprice.getText()) *
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
        if (Static.bookId.isEmpty()) {
            error = new Error();
            error.setfield("Please select an item to delete");
            return;
        }

        try {
            connect = Database.CODB();

            String findBookIdSql = "SELECT id FROM books WHERE bookid = ?";
            prepare = connect.prepareStatement(findBookIdSql);
            prepare.setString(1, Static.bookId);
            resultSet = prepare.executeQuery();

            int realBookId;
            if (resultSet.next()) {
                realBookId = resultSet.getInt("id");
            } else {
                error = new Error();
                error.setfield("Book not found in database.");
                return;
            }

            String deleteSql = "DELETE FROM cart WHERE book_id = ? AND user_id = ?";
            prepare = connect.prepareStatement(deleteSql);
            prepare.setInt(1, realBookId);
            prepare.setInt(2, Static.userId);
            int affectedRows = prepare.executeUpdate();

            if (affectedRows > 0) {
                error = new Error();
                error.update("Item successfully removed from cart.");
                showCartData();
                textQu.setText("");
            } else {
                error = new Error();
                error.setfield("Item not found in your cart.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            error = new Error();
            error.setfield("Error while deleting from cart.");
        }
    }

    public void update() {
        if (textcheckID.getText().isEmpty() || textQu.getText().isEmpty()) {
            error = new Error();
            error.setfield("Please fill out all fields");
            return;
        }

        try {
            connect = Database.CODB();

            String findBookSql = "SELECT id FROM books WHERE bookid = ?";
            prepare = connect.prepareStatement(findBookSql);
            prepare.setString(1, textcheckID.getText());
            resultSet = prepare.executeQuery();

            int realBookId;
            if (resultSet.next()) {
                realBookId = resultSet.getInt("id");
            } else {
                error = new Error();
                error.setfield("Book not found in database.");
                return;
            }

            int newQuantity = Integer.parseInt(textQu.getText());
            String updateSql = "UPDATE cart SET quantity = ?, added_at = CURRENT_TIMESTAMP " +
                    "WHERE book_id = ? AND user_id = ?";
            prepare = connect.prepareStatement(updateSql);
            prepare.setInt(1, newQuantity);
            prepare.setInt(2, realBookId);
            prepare.setInt(3, Static.userId);

            int affectedRows = prepare.executeUpdate();

            if (affectedRows > 0) {
                error = new Error();
                error.update("Quantity updated successfully.");
                showCartData(); // بروزرسانی جدول UI
                textQu.setText("");
            } else {
                error = new Error();
                error.setfield("Item not found in your cart.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            error = new Error();
            error.setfield("Error while updating cart.");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showCartData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
