package com.loginOTP.LOGINOTP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    public void generateAndSendOtp(String username, String email) {
        String otp = String.valueOf(new Random().nextInt(999999));

        OtpToken otpToken = new OtpToken();
        otpToken.setOtp(otp);
        otpToken.setUsername(username);
        otpToken.setExpiryDate(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes
        otpTokenRepository.save(otpToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is " + otp);
        mailSender.send(message);
    }

    public boolean validateOtp(String username, String otp) {
        OtpToken otpToken = otpTokenRepository.findByUsername(username);
        if (otpToken != null && otpToken.getOtp().equals(otp) &&
                otpToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }
}
