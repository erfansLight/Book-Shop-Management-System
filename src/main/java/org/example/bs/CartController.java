package org.example.bs;

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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class CartController extends WishlistController implements Initializable {

    @FXML
    private TextField textcheckID, textcheckname, textcheckprice, textchecktype, textQu;
    @FXML
    private TableView<SalesData> checktable;
    @FXML
    private TableColumn<SalesData, String> checkID, checkName, checkType, checkPrice, checkQuantity, checkDate;

    private CartRepository cartRepo;
    private BookRepository bookRepo;
    private AlterBox alterBox;
    private int userId = UserSession.getCurrentUser().getId();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = Database.CODB(); // فقط یکبار باز میشه
            cartRepo = new CartRepository(connection);
            bookRepo = new BookRepository();
            refreshCartTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshCartTable() throws SQLException {
        ObservableList<SalesData> list = cartRepo.getCartItems(userId);
        checkID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        checkName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        checkType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        checkPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        checkQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        checkDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        checktable.setItems(list);
    }

    @FXML
    public void select() {
        SalesData sd = checktable.getSelectionModel().getSelectedItem();
        if (sd == null) return;

        textcheckID.setText(sd.getID());
        textcheckname.setText(sd.getName());
        textchecktype.setText(sd.getType());
        textcheckprice.setText(String.valueOf(sd.getPrice()));
        textQu.setText(String.valueOf(sd.getQuantity()));
        Constants.bookId = sd.getID();
    }

    @FXML
    public void buy(ActionEvent event) throws SQLException, IOException {
        if (textQu.getText().isEmpty()) {
            showError("Please fill out quantity field");
            return;
        }

        int realBookId = bookRepo.getRealBookId(textcheckID.getText());
        double totalPrice = bookRepo.getBookPrice(realBookId) * Integer.parseInt(textQu.getText());

        // Insert order
//        bookRepo.insertOrder(userId, realBookId, Integer.parseInt(textQu.getText()), totalPrice);
        cartRepo.purchaseBook(userId, realBookId, Integer.parseInt(textQu.getText()), totalPrice);

        // Remove from cart
        cartRepo.removeFromCart(userId, realBookId);

        showSuccess("Purchase successful!");
        clearFields();
        refreshCartTable();
        navigateToUserPage(event);
    }

    @FXML
    public void update() throws SQLException {
        if (textcheckID.getText().isEmpty() || textQu.getText().isEmpty()) {
            showError("Please fill out all fields");
            return;
        }

        int realBookId = bookRepo.getRealBookId(textcheckID.getText());
        int newQuantity = Integer.parseInt(textQu.getText());
        cartRepo.updateQuantity(userId, realBookId, newQuantity);

        showSuccess("Quantity updated successfully.");
        clearFields();
        refreshCartTable();
    }

    @Override
    public void Deletebtn() {
        try {
            if (Constants.bookId.isEmpty()) {
                showError("Please select an item to delete");
                return;
            }
            int realBookId = bookRepo.getRealBookId(Constants.bookId);
            cartRepo.removeFromCart(userId, realBookId);

            showSuccess("Item successfully removed from cart.");
            clearFields();
            refreshCartTable();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error while deleting from cart.");
        }
    }

    @FXML
    private void showError(String msg) {
        alterBox = new AlterBox();
        alterBox.error(msg);
    }

    @FXML
    private void showSuccess(String msg) {
        alterBox = new AlterBox();
        alterBox.update(msg);
    }

    @FXML
    private void clearFields() {
        textcheckID.clear();
        textcheckname.clear();
        textchecktype.clear();
        textcheckprice.clear();
        textQu.clear();
        Constants.bookId = "";
    }

    @FXML
    private void navigateToUserPage(ActionEvent event) throws IOException {
        Parent root = load(getClass().getResource("UserPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
