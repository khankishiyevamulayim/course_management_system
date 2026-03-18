package org.example.coursemanagementsystem.util;


import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6; // 6 rəqəmli OTP

    public static String generate() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); // 0-9 arası rəqəm
        }
        return otp.toString();
    }
}