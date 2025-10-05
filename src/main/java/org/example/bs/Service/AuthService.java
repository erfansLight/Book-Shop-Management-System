package org.example.bs.Service;

import org.example.bs.AlterBox;
import org.example.bs.Repository.AuthRepository;
import org.example.bs.Model.User;

import java.sql.SQLException;
import java.time.LocalDate;

public class AuthService {

    private final AuthRepository authRepository = new AuthRepository();
    private final AlterBox alterBox = new AlterBox();

    // ورود
    public User login(String username, String password) throws SQLException {
        if (username.isEmpty() || password.isEmpty()) {
            alterBox.error("Invalid information.");
            return null;
        }
        return authRepository.findByUsernameAndPassword(username, password);
    }

    // ثبت‌نام
    public boolean register(User user) throws SQLException {
        if (user.getName().isEmpty() || user.getPassword().isEmpty() ||
                user.getEmail().isEmpty() || user.getCity().isEmpty() || user.getDate() == null) {
            alterBox.error("Please fill out all fields.");
            return false;
        }

        if (user.getPassword().length() < 5) {
            alterBox.error("Password must be at least 5 characters.");
            return false;
        }

        if (authRepository.existsByUsernameOrEmail(user.getName(), user.getEmail())) {
            alterBox.error("Username or email already exists.");
            return false;
        }

        return authRepository.createUser(user);
    }

    // تغییر رمز
    public boolean changePassword(String username, String newPassword) throws SQLException {
        if (newPassword.isEmpty() || newPassword.length() < 5) {
            alterBox.error("Password must be at least 5 characters.");
            return false;
        }
        return authRepository.updatePassword(username, newPassword);
    }

    // پیدا کردن کاربر با جزئیات (برای فراموشی رمز)
    public User findUserByDetails(String username, String city, LocalDate birthday) throws SQLException {
        return authRepository.findUserByDetails(username, city, birthday);
    }
}
