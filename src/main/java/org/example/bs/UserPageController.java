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


public class UserPageController implements Initializable {
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private Error error;

    @FXML
    private TableView<BookDeta> Usertableview;

    @FXML
    private TableColumn<BookDeta, String> customName;

    @FXML
    private TableColumn<BookDeta, String> customAuthor;

    @FXML
    private TableColumn<BookDeta, String> customDate;

    @FXML
    private TableColumn<BookDeta, String> customDescription;

    @FXML
    private TableColumn<BookDeta, String> customID;

    @FXML
    private TableColumn<BookDeta, String> customPrice;

    @FXML
    private TableColumn<BookDeta, String> customType;

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


    public ObservableList<BookDeta> detaList() throws SQLException {
        ObservableList<BookDeta> listdeta = FXCollections.observableArrayList();
        String myadmin = "SELECT * FROM bookdeta";
        connect = Detabase.CODB();

        try {
            prepare = connect.prepareStatement(myadmin);
            resultSet = prepare.executeQuery();
            BookDeta bookDeta;
            while (resultSet.next()) {
                bookDeta = new BookDeta(resultSet.getInt("id"),
                        resultSet.getString("bookid"),
                        resultSet.getString("bookname"),
                        resultSet.getString("type"),
                        resultSet.getString("Description"),
                        resultSet.getDouble("price"),
                        resultSet.getString("Author"),
                        resultSet.getDate("date"));
                listdeta.add(bookDeta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listdeta;
    }

    private ObservableList<BookDeta> List;

    public void showDetalist() throws SQLException {
        List = detaList();
        customID.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("ID"));
        customName.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Name"));
        customType.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Type"));
        customDescription.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Description"));
        customPrice.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Price"));
        customAuthor.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Author"));
        customDate.setCellValueFactory(new PropertyValueFactory<BookDeta, String>("Date"));

        Usertableview.setItems(List);
    }

    public void Select() {
        BookDeta bt = Usertableview.getSelectionModel().getSelectedItem();

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

    public void Signout(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "hello-view.fxml");
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
            connect = Detabase.CODB();
            try {
                String checkname = "SELECT bookname FROM wishlist WHERE bookname = '" +
                        textProductname.getText() + "'";
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
