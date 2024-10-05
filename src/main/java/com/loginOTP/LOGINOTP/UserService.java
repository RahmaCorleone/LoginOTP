package com.loginOTP.LOGINOTP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;  // Assuming you have a UserRepository

    // Method to save a user (for registration)
    public boolean saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;  // User already exists
        }
        user.setEnabled(false);  // Mark user as inactive initially
        userRepository.save(user);
        return true;
    }

    // Method to activate a user after successful OTP verification
    public void activateUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setEnabled(true);  // Activate the user
            userRepository.save(user);
        }
    }

    // Method to find a user by username
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
