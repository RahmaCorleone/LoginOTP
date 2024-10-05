package com.loginOTP.LOGINOTP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;  // Assuming you have a UserService to manage user-related operations

    // Endpoint to register a new user (add this if it's not already implemented)
    @PostMapping("/register")
    public String registerUser(@RequestBody User user, Model model) {
        // Save the user in the database, send OTP, etc.
        boolean isUserSaved = userService.saveUser(user);  // Assuming a saveUser method in UserService

        if (isUserSaved) {
            otpService.generateAndSendOtp(user.getUsername(), user.getEmail());  // Generate and send OTP
            return "redirect:/verify-otp?username=" + user.getUsername();  // Redirect to OTP verification page
        } else {
            model.addAttribute("error", "User registration failed.");
            return "register";  // Show the registration page again in case of failure
        }
    }

    // Endpoint to show the OTP verification page
    @GetMapping("/verify-otp")
    public String showOtpVerificationPage(@RequestParam String username, Model model) {
        // Adding the 'username' to the model to be accessible in the view
        model.addAttribute("username", username);
        return "verify-otp";  // This view (Thymeleaf template) will use 'username'
    }

    // Endpoint to handle OTP verification
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String username, @RequestParam String otp, Model model) {
        boolean isValidOtp = otpService.validateOtp(username, otp);  // Assuming the OTP validation logic is correct

        if (isValidOtp) {
            // Success: OTP is valid, user can log in
            userService.activateUser(username);  // Assuming there's a method to activate the user in UserService
            return "redirect:/login";  // Redirect to login page
        } else {
            // Failure: OTP is invalid or expired, show error message
            model.addAttribute("error", "Invalid or expired OTP. Please try again.");
            model.addAttribute("username", username);  // Retain the username to resend the OTP
            return "verify-otp";  // Stay on OTP verification page
        }
    }

    // Optionally, add a resend OTP endpoint if needed
    @PostMapping("/resend-otp")
    public String resendOtp(@RequestParam String username, Model model) {
        User user = userService.findUserByUsername(username);  // Assuming you have this method in UserService
        if (user != null) {
            otpService.generateAndSendOtp(user.getUsername(), user.getEmail());
            model.addAttribute("message", "OTP has been resent to your email.");
            return "verify-otp";  // Return to the OTP verification page
        } else {
            model.addAttribute("error", "User not found.");
            return "error";  // Show an error page or message
        }
    }
}
