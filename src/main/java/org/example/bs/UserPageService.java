package org.example.bs;

import java.sql.Connection;
import java.sql.SQLException;

public class UserPageService {
    private final CartRepository cartRepo;
    private final WishlistRepository wishRepo;
    private final int userId;

    public UserPageService(Connection connection, int userId) {
        this.cartRepo = new CartRepository(connection);
        this.wishRepo = new WishlistRepository(connection);
        this.userId = userId;
    }

    public String addBookToCart(int bookId, int quantity) throws SQLException {
        if (cartRepo.isBookInCart(userId, bookId)) {
            int oldQty = cartRepo.getQuantity(userId, bookId);
            cartRepo.updateQuantity(userId, bookId, oldQty + quantity);
            return "Quantity updated in cart.";
        } else {
            cartRepo.addToCart(userId, bookId, quantity);
            return "Book added to cart successfully.";
        }
    }

    public String addBookToWishlist(int bookId) throws SQLException {
        if (wishRepo.isBookInWishlist(userId, bookId)) {
            return "This book has already been added.";
        } else {
            wishRepo.addToWishlist(userId, bookId);
            return "Book added to wishlist successfully.";
        }
    }
}
