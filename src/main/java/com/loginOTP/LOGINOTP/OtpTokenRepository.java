package com.loginOTP.LOGINOTP;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    OtpToken findByUsername(String username);
}
