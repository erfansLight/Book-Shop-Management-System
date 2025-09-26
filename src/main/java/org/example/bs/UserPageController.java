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
import java.sql.*;
import java.util.ResourceBundle;


public class UserPageController extends LoginController implements Initializable {
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private AlterBox alterBox;

    @FXML
    private TableView<BookData> Usertableview;

    @FXML
    private TableColumn<BookData, String> customName;

    @FXML
    private TableColumn<BookData, String> customAuthor;

    @FXML
    private TableColumn<BookData, String> customDate;

    @FXML
    private TableColumn<BookData, String> customDescription;

    @FXML
    private TableColumn<BookData, String> customID;

    @FXML
    private TableColumn<BookData, String> customPrice;

    @FXML
    private TableColumn<BookData, String> customType;

    @FXML
    private TextField textAuthor;

    @FXML
    private TextField textDescription;

    @FXML
    private TextField textPrice;

    @FXML
    private TextField textProductname;

    @FXML
    private TextField textQuantity;

    @FXML
    private TextField textType;

    @FXML
    private TextField textproductID;


    int userId = UserSession.getCurrentUser().getId();

    public ObservableList<BookData> dataList() throws SQLException {
        ObservableList<BookData> listdeta = FXCollections.observableArrayList();
        String sql = "SELECT * FROM books";
        connect = Database.CODB();

        try {
            prepare = connect.prepareStatement(sql);
            resultSet = prepare.executeQuery();
            BookData bookDeta;
            while (resultSet.next()) {
                bookDeta = new BookData();
                bookDeta.setId(resultSet.getInt("id"));
                bookDeta.setID(resultSet.getString("bookid"));
                bookDeta.setName(resultSet.getString("bookname"));
                bookDeta.setType(resultSet.getString("type"));
                bookDeta.setDescription(resultSet.getString("Description"));
                bookDeta.setPrice(resultSet.getDouble("price"));
                bookDeta.setAuthor(resultSet.getString("Author"));
                bookDeta.setDate(resultSet.getDate("date"));
                listdeta.add(bookDeta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listdeta;
    }

    private ObservableList<BookData> List;

    public void showDatalist() throws SQLException {
        List = dataList();
        customID.setCellValueFactory(new PropertyValueFactory<BookData, String>("ID"));
        customName.setCellValueFactory(new PropertyValueFactory<BookData, String>("Name"));
        customType.setCellValueFactory(new PropertyValueFactory<BookData, String>("Type"));
        customDescription.setCellValueFactory(new PropertyValueFactory<BookData, String>("Description"));
        customPrice.setCellValueFactory(new PropertyValueFactory<BookData, String>("Price"));
        customAuthor.setCellValueFactory(new PropertyValueFactory<BookData, String>("Author"));
        customDate.setCellValueFactory(new PropertyValueFactory<BookData, String>("Date"));

        Usertableview.setItems(List);
    }

    public void Select() {
        BookData bt = Usertableview.getSelectionModel().getSelectedItem();

        textproductID.setText(bt.getID());
        textProductname.setText(bt.getName());
        textType.setText(bt.getType());
        textAuthor.setText(bt.getAuthor());
        textPrice.setText(String.valueOf(bt.getPrice()));
        textDescription.setText(bt.getDescription());
        Constants.id = bt.getId();
    }

    public void Addbtn() throws SQLException {
        if (textQuantity.getText().isEmpty()) {
            alterBox = new AlterBox();
            alterBox.error("Please fill out quantity field");
            return;
        }

        connect = Database.CODB();

        try {
            String findBook = "SELECT id FROM books WHERE bookid = ?";
            prepare = connect.prepareStatement(findBook);
            prepare.setString(1, textproductID.getText());
            resultSet = prepare.executeQuery();

            int bookId;
            if (resultSet.next()) {
                bookId = resultSet.getInt("id");
            } else {
                alterBox = new AlterBox();
                alterBox.error("Book not found.");
                return;
            }

            int quantity = Integer.parseInt(textQuantity.getText());

            String checkSql = "SELECT quantity FROM cart WHERE user_id = ? AND book_id = ?";
            prepare = connect.prepareStatement(checkSql);
            prepare.setInt(1, userId);
            prepare.setInt(2, bookId);
            resultSet = prepare.executeQuery();

            if (resultSet.next()) {
                int oldQuantity = resultSet.getInt("quantity");
                int newQuantity = oldQuantity + quantity;

                String updateSql = "UPDATE cart SET quantity = ?, added_at = CURRENT_TIMESTAMP " +
                        "WHERE user_id = ? AND book_id = ?";
                prepare = connect.prepareStatement(updateSql);
                prepare.setInt(1, newQuantity);
                prepare.setInt(2, userId);
                prepare.setInt(3, bookId);
                prepare.executeUpdate();

                alterBox = new AlterBox();
                alterBox.update("Quantity updated in cart.");
            } else {
                String insertSql = "INSERT INTO cart (user_id, book_id, quantity) VALUES (?, ?, ?)";
                prepare = connect.prepareStatement(insertSql);
                prepare.setInt(1, userId);
                prepare.setInt(2, bookId);
                prepare.setInt(3, quantity);
                prepare.executeUpdate();

                alterBox = new AlterBox();
                alterBox.update("Book added to cart successfully.");
            }

            textQuantity.clear();

        } catch (Exception e) {
            e.printStackTrace();
            alterBox = new AlterBox();
            alterBox.error("Error while adding to cart.");
        }
    }


    public void next(ActionEvent event) throws IOException {
        SwitchScene s1 = new SwitchScene();
        s1.switchto(event, "checkpage.fxml");
    }

    public void search(ActionEvent event) throws IOException {
        SwitchScene s1 = new SwitchScene();
        s1.switchto(event, "Search.fxml");
    }

    public void showwishbtn(ActionEvent event) throws IOException {
        SwitchScene s1 = new SwitchScene();
        s1.switchto(event, "WishList.fxml");
    }


    public void Wishbtn() {
        try {
            Connection connect = Database.CODB();
            WishlistRepository repo = new WishlistRepository(connect);

            String findBookSql = "SELECT id FROM books WHERE bookid = ?";
            PreparedStatement stmt = connect.prepareStatement(findBookSql);
            stmt.setString(1, textproductID.getText());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("id");

                if (repo.isBookInWishlist(userId, bookId)) {
                    alterBox = new AlterBox();
                    alterBox.error("This book has already been added.");
                } else {
                    repo.addBookToWishlist(userId, bookId);
                    alterBox = new AlterBox();
                    alterBox.update("Book added to wishlist successfully.");
                }
            } else {
                alterBox = new AlterBox();
                alterBox.error("Book not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showDatalist();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

