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


public class UserPageController extends HelloController implements Initializable {
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private Error error;

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


    public ObservableList<BookData> detaList() throws SQLException {
        ObservableList<BookData> listdeta = FXCollections.observableArrayList();
        String myadmin = "SELECT * FROM bookdeta";
        connect = Database.CODB();

        try {
            prepare = connect.prepareStatement(myadmin);
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

    public void showDetalist() throws SQLException {
        List = detaList();
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
        Static.id = bt.getId();
    }

    public void Addbtn() throws SQLException {
        if (textQuantity.getText().isEmpty()) {
            error = new Error();
            error.setfield("Please fill out all field");
        } else {
            try {
                String insertdeta2 = "INSERT INTO checkpage" +
                        " (bookid, bookname, type, price, Quantity, date)" + "VALUES(?,?,?,?,?,?)";
                prepare = connect.prepareStatement(insertdeta2);
                prepare = connect.prepareStatement(insertdeta2);
                prepare.setString(1, textproductID.getText());
                prepare.setString(2, textProductname.getText());
                prepare.setString(3, textType.getText());
                prepare.setString(4, textPrice.getText());
                prepare.setString(5, textQuantity.getText());

                java.util.Date date = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(6, String.valueOf(sqlDate));

                prepare.executeUpdate();

                error = new Error();
                error.update("Successful");

                textQuantity.setText("");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void next(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "checkpage.fxml");
    }

    public void search(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "Search.fxml");
    }

    public void Wishbtn() throws SQLException {
        if (textproductID.getText().isEmpty() || textProductname.getText().isEmpty() ||
                textType.getText().isEmpty() || textAuthor.getText().isEmpty() ||
                textPrice.getText().isEmpty() || textDescription.getText().isEmpty()) {
            error = new Error();
            error.setfield("Please fill out all field");
        } else {
            connect = Database.CODB();
            try {
                String checkname = "SELECT bookname FROM wishlist WHERE bookname = '" +
                        textProductname.getText() + "' AND customername = '"+Static.name+"'";
                prepare = connect.prepareStatement(checkname);
                resultSet = prepare.executeQuery();
                if (resultSet.next()) {
                    error = new Error();
                    error.setfield("This book has already added.");
                } else {
                    String insertdeta = "INSERT INTO WishList" +
                            " (bookid, bookname, type, price, Description, Author, customername, date)" +
                            "VALUES(?,?,?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertdeta);
                    prepare = connect.prepareStatement(insertdeta);
                    prepare.setString(1, textproductID.getText());
                    prepare.setString(2, textProductname.getText());
                    prepare.setString(3, textType.getText());
                    prepare.setString(4, textPrice.getText());
                    prepare.setString(5, textDescription.getText());
                    prepare.setString(6, textAuthor.getText());
                    prepare.setString(7, Static.name);

                    java.util.Date date = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(8, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    error = new Error();
                    error.update("Successful");

                    textQuantity.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showwishbtn(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "WishList.fxml");
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
