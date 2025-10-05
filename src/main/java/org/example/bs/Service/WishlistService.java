package org.example.bs.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bs.Model.Wishlist;
import org.example.bs.Repository.WishlistRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(Connection connect) {
        this.wishlistRepository = new WishlistRepository(connect);
    }

    public ObservableList<Wishlist> loadWishlist(int userId) throws SQLException {
        List<Wishlist> list = wishlistRepository.getWishlist(userId);
        return FXCollections.observableArrayList(list);
    }

    public void removeBook(int userId, int wishlistId) throws SQLException {
        wishlistRepository.removeFromWishlist(userId, wishlistId);
    }

    public void addBook(int userId, int bookId) throws SQLException {
        if (!wishlistRepository.isBookInWishlist(userId, bookId)) {
            wishlistRepository.addToWishlist(userId, bookId);
        }
    }
}
