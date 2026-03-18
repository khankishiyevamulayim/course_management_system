package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.util.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OtpService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final long OTP_EXPIRE_MINUTES = 5;

    public String generateAndSaveOtp(String email) {
        String otp = OtpGenerator.generate();  // 6 rəqəmli random
        redisTemplate.opsForValue().set("otp:" + email, otp, OTP_EXPIRE_MINUTES, TimeUnit.MINUTES);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        String savedOtp = redisTemplate.opsForValue().get("otp:" + email);
        return otp.equals(savedOtp);
    }

    public void clearOtp(String email) {
        redisTemplate.delete("otp:" + email);
    }
}
