package org.example.bs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WishController implements Initializable {

    @FXML
    private TableView<wishData> wishtable;

    @FXML
    private TableColumn<wishData, String> wishID, wishname, wishtype, wishDescription, wishPrice, wishAuthor, wishDate;

    @FXML
    private TextField delete;

    private ObservableList<wishData> wishList;
    private WishlistRepository wishlistRepo;
    int userId = UserSession.getCurrentUser().getId();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connect = Database.CODB();
            wishlistRepo = new WishlistRepository(connect);
            loadWishlist();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadWishlist() throws SQLException {
        wishList = FXCollections.observableArrayList(wishlistRepo.getWishlist(userId));

        wishID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        wishname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        wishtype.setCellValueFactory(new PropertyValueFactory<>("Type"));
        wishDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        wishPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        wishAuthor.setCellValueFactory(new PropertyValueFactory<>("Author"));
        wishDate.setCellValueFactory(new PropertyValueFactory<>("Date"));

        wishtable.setItems(wishList);
    }

    public void Select() {
        wishData selected = wishtable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            delete.setText(String.valueOf(selected.getId()));
        }
    }

    public void Deletebtn() {
        try {
            int wishlistId = Integer.parseInt(delete.getText());
            wishlistRepo.removeBookFromWishlist(userId, wishlistId);
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
