package org.example.bs.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.bs.AlterBox;
import org.example.bs.Repository.BookRepository;
import org.example.bs.Model.Book;
import org.example.bs.SwitchScene;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class AdminController extends LoginController implements Initializable {

    @FXML
    private TableView<Book> dashbordtableview;

    @FXML
    private TableColumn<Book, String> dashcolAuthor;

    @FXML
    private TableColumn<Book, String> dashcolDate;

    @FXML
    private TableColumn<Book, String> dashcolDescription;

    @FXML
    private TableColumn<Book, String> dashcolIDProduct;

    @FXML
    private TableColumn<Book, String> dashcolPrice;

    @FXML
    private TableColumn<Book, String> dashcolProductname;

    @FXML
    private TableColumn<Book, String> dashcolType;

    @FXML
    private TextField textAuthor;

    @FXML
    private TextField textDescription;

    @FXML
    private TextField textPrice;

    @FXML
    private TextField textProductname;

    @FXML
    private TextField textType;

    @FXML
    private TextField textproductID;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet resultSet;
    private AlterBox alterBox;


    private final BookRepository repo = new BookRepository();
    private ObservableList<Book> list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showDetalist();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showDetalist() throws SQLException {
        list = FXCollections.observableArrayList(repo.findAll());
        dashcolIDProduct.setCellValueFactory(new PropertyValueFactory<>("ID"));
        dashcolProductname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        dashcolType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        dashcolDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        dashcolPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        dashcolAuthor.setCellValueFactory(new PropertyValueFactory<>("Author"));
        dashcolDate.setCellValueFactory(new PropertyValueFactory<>("Date"));

        dashbordtableview.setItems(list);
    }

    public void Addbtn() throws SQLException {
        if (textproductID.getText().isEmpty() || textProductname.getText().isEmpty() ||
                textType.getText().isEmpty() || textAuthor.getText().isEmpty() ||
                textPrice.getText().isEmpty() || textDescription.getText().isEmpty()) {
            alterBox = new AlterBox();
            alterBox.error("Please fill out all field");
        } else {
            if (repo.existsByBookId(textproductID.getText())) {
                alterBox = new AlterBox();
                alterBox.error("This book has already been taken.");
                return;
            }

            Book book = new Book();
            book.setID(textproductID.getText());
            book.setName(textProductname.getText());
            book.setType(textType.getText());
            book.setAuthor(textAuthor.getText());
            book.setPrice(Double.parseDouble(textPrice.getText()));
            book.setDescription(textDescription.getText());
            book.setDate(new java.util.Date());

            repo.save(book);
            showDetalist();
            clearbtn();
        }
    }

    public void Updatebtn() throws SQLException {
        Book selected = dashbordtableview.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alterBox = new AlterBox();
            alterBox.error("Select a row first");
            return;
        }

        selected.setID(textproductID.getText());
        selected.setName(textProductname.getText());
        selected.setType(textType.getText());
        selected.setAuthor(textAuthor.getText());
        selected.setPrice(Double.parseDouble(textPrice.getText()));
        selected.setDescription(textDescription.getText());
        selected.setDate(new java.util.Date());

        repo.update(selected);
        showDetalist();
        clearbtn();
    }

    public void Deletebtn() throws SQLException {
        Book selected = dashbordtableview.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alterBox = new AlterBox();
            alterBox.error("Select a row first");
            return;
        }
        repo.delete(selected.getId());
        showDetalist();
        clearbtn();
    }

    public void clearbtn() {
        textproductID.setText("");
        textProductname.setText("");
        textType.setText("");
        textDescription.setText("");
        textPrice.setText("");
        textAuthor.setText("");
    }

    public void Select() {
        Book bt = dashbordtableview.getSelectionModel().getSelectedItem();
        if (bt != null) {
            textproductID.setText(bt.getID());
            textProductname.setText(bt.getName());
            textType.setText(bt.getType());
            textAuthor.setText(bt.getAuthor());
            textPrice.setText(String.valueOf(bt.getPrice()));
            textDescription.setText(bt.getDescription());
        }
    }

    public void customerbtn(ActionEvent event) throws IOException {
        SwitchScene s = new SwitchScene();
        s.switchto(event, "Saleslist.fxml");
    }

}