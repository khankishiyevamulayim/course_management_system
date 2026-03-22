package org.example.coursemanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.util.OtpGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long OTP_EXPIRE_MINUTES = 5;
    private static final String OTP_PREFIX = "otp:";

    public String generateAndSaveOtp(String email) {
        String otp = OtpGenerator.generate();
        redisTemplate.opsForValue().set(
                key(email), otp, OTP_EXPIRE_MINUTES, TimeUnit.MINUTES);
        return otp;
    }

    public boolean validateAndClearOtp(String email, String otp) {
        String savedOtp = redisTemplate.opsForValue().get(key(email));
        if (otp.equals(savedOtp)) {
            redisTemplate.delete(key(email));
            return true;
        }
        return false;
    }

    public void clearOtp(String email) {
        redisTemplate.delete(key(email));
    }

    private String key(String email) {
        return OTP_PREFIX + email.toLowerCase();
    }
}