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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class CheckpageController extends WishController implements Initializable {

    private BookRepository bookRepo = new BookRepository();
    private AlterBox alterBox;
    private Stage stage;
    private Scene scene;

    @FXML
    private TextField textcheckID;
    @FXML
    private TextField textcheckname;
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

    private ObservableList<SalesData> ListCart;

    int userId = UserSession.getCurrentUser().getId();

    public ObservableList<SalesData> dataListCart() throws SQLException {
        ObservableList<SalesData> list_data = FXCollections.observableArrayList();
        String sql = "SELECT c.book_id, b.bookid, b.bookname, b.type, b.price, c.quantity, c.added_at " +
                "FROM cart c JOIN books b ON c.book_id = b.id WHERE c.user_id = ?";
        try (var conn = Database.CODB();
             var prepare = conn.prepareStatement(sql)) {

            prepare.setInt(1, userId);
            var resultSet = prepare.executeQuery();
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
        }
        return list_data;
    }

    public void showCartData() throws SQLException {
        ListCart = dataListCart();
        checkID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        checkName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        checkType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        checkPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        checkQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        checkDate.setCellValueFactory(new PropertyValueFactory<>("Date"));

        checktable.setItems(ListCart);
    }

    public void select() {
        SalesData customerData = checktable.getSelectionModel().getSelectedItem();
        if (customerData == null) return;

        textQu.setText(String.valueOf(customerData.getQuantity()));
        textcheckID.setText(customerData.getID());
        textcheckname.setText(customerData.getName());
        textchecktype.setText(customerData.getType());
        textcheckprice.setText(String.valueOf(customerData.getPrice()));
        Constants.bookId = customerData.getID();
    }

    public void buy(ActionEvent event) throws SQLException, IOException {
        if (textQu.getText().isEmpty()) {
            alterBox = new AlterBox();
            alterBox.error("Please fill out quantity field");
            return;
        }

        int realBookId = bookRepo.getRealBookId(textcheckID.getText());
        double bookPrice = bookRepo.getBookPrice(realBookId);
        int quantity = Integer.parseInt(textQu.getText());
        double totalPrice = bookPrice * quantity;

        // Insert order
        try (var conn = Database.CODB();
             var prepare = conn.prepareStatement(
                     "INSERT INTO orders (user_id, book_id, quantity, total_price, created_at) " +
                             "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)")) {

            prepare.setInt(1, userId);
            prepare.setInt(2, realBookId);
            prepare.setInt(3, quantity);
            prepare.setDouble(4, totalPrice);
            prepare.executeUpdate();
        }

        // Delete from cart
        try (var conn = Database.CODB();
             var prepare = conn.prepareStatement(
                     "DELETE FROM cart WHERE user_id = ? AND book_id = ?")) {

            prepare.setInt(1, userId);
            prepare.setInt(2, realBookId);
            prepare.executeUpdate();
        }

        alterBox = new AlterBox();
        alterBox.update("Purchase successful!");
        textQu.clear();

        Parent root = load(getClass().getResource("UserPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void Deletebtn() {
        try {
            if (Constants.bookId.isEmpty()) {
                alterBox = new AlterBox();
                alterBox.error("Please select an item to delete");
                return;
            }

            int realBookId = bookRepo.getRealBookId(Constants.bookId);

            try (var conn = Database.CODB();
                 var prepare = conn.prepareStatement(
                         "DELETE FROM cart WHERE book_id = ? AND user_id = ?")) {

                prepare.setInt(1, realBookId);
                prepare.setInt(2, userId);
                int affectedRows = prepare.executeUpdate();

                if (affectedRows > 0) {
                    alterBox = new AlterBox();
                    alterBox.update("Item successfully removed from cart.");
                    showCartData();
                    textQu.setText("");
                } else {
                    alterBox = new AlterBox();
                    alterBox.error("Item not found in your cart.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            alterBox = new AlterBox();
            alterBox.error("Error while deleting from cart.");
        }
    }


    public void update() throws SQLException {
        if (textcheckID.getText().isEmpty() || textQu.getText().isEmpty()) {
            alterBox = new AlterBox();
            alterBox.error("Please fill out all fields");
            return;
        }

        int realBookId = bookRepo.getRealBookId(textcheckID.getText());
        int newQuantity = Integer.parseInt(textQu.getText());

        try (var conn = Database.CODB();
             var prepare = conn.prepareStatement(
                     "UPDATE cart SET quantity = ?, added_at = CURRENT_TIMESTAMP WHERE book_id = ? AND user_id = ?")) {

            prepare.setInt(1, newQuantity);
            prepare.setInt(2, realBookId);
            prepare.setInt(3, userId);
            int affectedRows = prepare.executeUpdate();

            if (affectedRows > 0) {
                alterBox = new AlterBox();
                alterBox.update("Quantity updated successfully.");
                showCartData();
                textQu.setText("");
            } else {
                alterBox = new AlterBox();
                alterBox.error("Item not found in your cart.");
            }
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
