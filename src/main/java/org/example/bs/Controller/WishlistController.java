package org.example.bs.Controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.bs.Database;
import org.example.bs.Model.Wishlist;
import org.example.bs.Service.WishlistService;
import org.example.bs.SwitchScene;
import org.example.bs.Session.UserSession;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WishlistController implements Initializable {

    @FXML private TableView<Wishlist> wishtable;
    @FXML private TableColumn<Wishlist, String> wishID, wishname, wishtype, wishDescription, wishPrice, wishAuthor, wishDate;
    @FXML private TextField delete;

    private WishlistService wishlistService;
    private ObservableList<Wishlist> wishList;
    private int userId = UserSession.getCurrentUser().getId();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connect = Database.CODB();
            wishlistService = new WishlistService(connect);
            loadWishlist();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadWishlist() throws SQLException {
        wishList = wishlistService.loadWishlist(userId);

        wishID.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBookID()));
        wishname.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        wishtype.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));
        wishDescription.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        wishPrice.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));
        wishAuthor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAuthor()));
        wishDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate().toString()));

        wishtable.setItems(wishList);
    }

    public void Select() {
        Wishlist selected = wishtable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            delete.setText(String.valueOf(selected.getId()));
        }
    }

    public void Deletebtn() {
        try {
            int wishlistId = Integer.parseInt(delete.getText());
            wishlistService.removeBook(userId, wishlistId);
            loadWishlist();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent event) {
        try {
            new SwitchScene().switchto(event, "UserPage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
