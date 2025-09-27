package org.example.bs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserPageController extends LoginController implements Initializable {

    private int userId;
    private UserPageService userService;
    private BookRepository bookRepo;
    private AlterBox alterBox;

    @FXML
    private TableView<Book> Usertableview;
    @FXML
    private TableColumn<Book, String> customID;
    @FXML
    private TableColumn<Book, String> customName;
    @FXML
    private TableColumn<Book, String> customType;
    @FXML
    private TableColumn<Book, String> customDescription;
    @FXML
    private TableColumn<Book, String> customPrice;
    @FXML
    private TableColumn<Book, String> customAuthor;
    @FXML
    private TableColumn<Book, String> customDate;

    @FXML
    private TextField textproductID;
    @FXML
    private TextField textProductname;
    @FXML
    private TextField textType;
    @FXML
    private TextField textAuthor;
    @FXML
    private TextField textPrice;
    @FXML
    private TextField textDescription;
    @FXML
    private TextField textQuantity;

    private ObservableList<Book> bookList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            userId = UserSession.getCurrentUser().getId();
            Connection connection = Database.CODB();
            userService = new UserPageService(connection, userId);
            bookRepo = new BookRepository();
            loadBooks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBooks() throws SQLException {
        List<Book> books = bookRepo.findAll();
        bookList = FXCollections.observableArrayList(books);

        customID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        customName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        customType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        customDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        customPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        customAuthor.setCellValueFactory(new PropertyValueFactory<>("Author"));
        customDate.setCellValueFactory(new PropertyValueFactory<>("Date"));

        Usertableview.setItems(bookList);
    }

    @FXML
    public void Select() {
        Book selected = Usertableview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            textproductID.setText(selected.getID());
            textProductname.setText(selected.getName());
            textType.setText(selected.getType());
            textAuthor.setText(selected.getAuthor());
            textPrice.setText(String.valueOf(selected.getPrice()));
            textDescription.setText(selected.getDescription());
            Constants.id = selected.getId();
        }
    }

    @FXML
    public void Addbtn() {
        if (textQuantity.getText().isEmpty()) {
            alterBox = new AlterBox();
            alterBox.error("Please fill out quantity field");
            return;
        }

        try {
            int quantity = Integer.parseInt(textQuantity.getText());
            int bookId = bookRepo.getRealBookId(textproductID.getText());
            String message = userService.addBookToCart(bookId, quantity);
            alterBox = new AlterBox();
            alterBox.update(message);
            textQuantity.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            alterBox = new AlterBox();
            alterBox.error("Error while adding to cart.");
        }
    }

    @FXML
    public void addToWishlist() {
        try {
            int bookId = bookRepo.getRealBookId(textproductID.getText());
            String message = userService.addBookToWishlist(bookId);
            alterBox = new AlterBox();
            alterBox.update(message);
        } catch (SQLException e) {
            e.printStackTrace();
            alterBox = new AlterBox();
            alterBox.error("Error while adding to wishlist.");
        }
    }

    @FXML
    public void Wishbtn() {
        try {
            int bookId = bookRepo.getRealBookId(textproductID.getText());
            String message = userService.addBookToWishlist(bookId);
            alterBox = new AlterBox();
            alterBox.update(message);
        } catch (SQLException e) {
            e.printStackTrace();
            alterBox = new AlterBox();
            alterBox.error("Error while adding to wishlist.");
        }
    }


    @FXML
    public void next(ActionEvent event) throws IOException {
        new SwitchScene().switchto(event, "checkpage.fxml");
    }

    @FXML
    public void search(ActionEvent event) throws IOException {
        new SwitchScene().switchto(event, "Search.fxml");
    }

    @FXML
    public void showwishbtn(ActionEvent event) throws IOException {
        new SwitchScene().switchto(event, "WishList.fxml");
    }
}


