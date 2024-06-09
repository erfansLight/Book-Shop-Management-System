package org.example.bs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Date;

import static javafx.fxml.FXMLLoader.load;

public class AdminController extends HelloController implements Initializable {

    @FXML
    private TableView<BookData> dashbordtableview;

    @FXML
    private TableColumn<BookData,String> dashcolAuthor;

    @FXML
    private TableColumn<BookData,String> dashcolDate;

    @FXML
    private TableColumn<BookData,String> dashcolDescription;

    @FXML
    private TableColumn<BookData,String> dashcolIDProduct;

    @FXML
    private TableColumn<BookData,String> dashcolPrice;

    @FXML
    private TableColumn<BookData,String> dashcolProductname;

    @FXML
    private TableColumn<BookData,String> dashcolType;

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
    private Error error;
    public ObservableList<BookData> detaList() throws SQLException {
         ObservableList<BookData> listdeta = FXCollections.observableArrayList();
         String myadmin = "SELECT * FROM bookdeta";
         connect = Database.CODB();

         try{
             prepare = connect.prepareStatement(myadmin);
             resultSet = prepare.executeQuery();
             BookData bookDeta;
             while(resultSet.next()){
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
         }catch (Exception e){
             e.printStackTrace();
         }
         return listdeta;
    }
    private ObservableList<BookData> List;
    public void showDetalist() throws SQLException {
        List = detaList();
        dashcolIDProduct.setCellValueFactory(new PropertyValueFactory<BookData,String>("ID"));
        dashcolProductname.setCellValueFactory(new PropertyValueFactory<BookData, String>("Name"));
        dashcolType.setCellValueFactory(new PropertyValueFactory<BookData, String>("Type"));
        dashcolDescription.setCellValueFactory(new PropertyValueFactory<BookData, String>("Description"));
        dashcolPrice.setCellValueFactory(new PropertyValueFactory<BookData, String>("Price"));
        dashcolAuthor.setCellValueFactory(new PropertyValueFactory<BookData, String>("Author"));
        dashcolDate.setCellValueFactory(new PropertyValueFactory<BookData, String>("Date"));

        dashbordtableview.setItems(List);
    }

    public void Addbtn() throws SQLException {
        if(textproductID.getText().isEmpty()|| textProductname.getText().isEmpty()||
        textType.getText().isEmpty() || textAuthor.getText().isEmpty() ||
        textPrice.getText().isEmpty() || textDescription.getText().isEmpty()){
            error = new Error();
            error.setfield("Please fill out all field");
        }else{
            String checkbookid = "SELECT bookid FROM bookdeta WHERE bookid = '" +
                    textproductID.getText()+"'";
            connect = Database.CODB();
            try{

                statement = connect.createStatement();
                resultSet = statement.executeQuery(checkbookid);
                if(resultSet.next()){
                    error = new Error();
                    error.setfield("This book has already been taken.");
                }else {
                    String insertdeta = "INSERT INTO bookdeta" +
                            " (bookid, bookname, type, Description, price, Author, date)" +"VALUES(?,?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertdeta);
                    prepare.setString(1,textproductID.getText());
                    prepare.setString(2,textProductname.getText());
                    prepare.setString(3,textType.getText());
                    prepare.setString(4,textDescription.getText());
                    prepare.setString(5,textPrice.getText());
                    prepare.setString(6,textAuthor.getText());
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(7,String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    error = new Error();
                    error.update("Successfully Added.");

                    showDetalist();
                    clearbtn();
                }
            }catch (Exception e){e.printStackTrace();}

        }
    }
    public void clearbtn(){
        textproductID.setText("");
        textProductname.setText("");
        textType.setText("");
        textDescription.setText("");
        textPrice.setText("");
        textAuthor.setText("");
        Static.id=0;
    }
    public void Select(){
        BookData bt = dashbordtableview.getSelectionModel().getSelectedItem();

        textproductID.setText(bt.getID());
        textProductname.setText(bt.getName());
        textType.setText(bt.getType());
        textAuthor.setText(bt.getAuthor());
        textPrice.setText(String.valueOf(bt.getPrice()));
        textDescription.setText(bt.getDescription());
        Static.date = String.valueOf(bt.getDate());
        Static.id = bt.getId();
    }
    public void Updatebtn() throws SQLException {
        if(textproductID.getText().isEmpty()|| textProductname.getText().isEmpty()||
                textType.getText().isEmpty() || textAuthor.getText().isEmpty() ||
                textPrice.getText().isEmpty() || textDescription.getText().isEmpty() ||
                Static.id == 0){
            error = new Error();
            error.setfield("Please fill out all field");
        }else {
            String Update = "UPDATE bookdeta SET bookid = '"+textproductID.getText()+"', bookname = '"+
                    textProductname.getText()+"', type = '"+textType.getText()+"', Description = '"+
                    textDescription.getText()+"', price = '"+textPrice.getText()+"',Author = '"+
                    textAuthor.getText()+"', date = '"+ Static.date+"' WHERE id = "+Static.id;

            connect = Database.CODB();
            try{

                prepare = connect.prepareStatement(Update);
                prepare.executeUpdate();

                error = new Error();
                error.update("Successfully updated.");

                showDetalist();
                clearbtn();
            }catch (Exception e){e.printStackTrace();}
        }
    }
    public void customerbtn(ActionEvent event) throws IOException {
        Switch s = new Switch();
        s.switchto(event,"Saleslist.fxml");
    }
    public void Deletebtn() throws SQLException {
        if(Static.id == 0){
            error = new Error();
            error.setfield("Please fill out all field");
        }else {
            String delet = "DELETE FROM bookdeta WHERE id = "+ Static.id;
            try{
                prepare = connect.prepareStatement(delet);
                prepare.executeUpdate();
                error = new Error();
                error.update("Successfully deleted");

                showDetalist();
                clearbtn();
            }catch (Exception e){e.printStackTrace();}        }
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
