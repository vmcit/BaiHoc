package com.example.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OtpService - Sinh OTP 6 số, lưu tạm trong bộ nhớ (TTL 5 phút).
 */
@Service
public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    private static final long OTP_TTL_MS = 5 * 60 * 1000L; // 5 phút

    @Value("${otp.webhook.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, long[]> otpStore = new ConcurrentHashMap<>();
    private final Map<String, String> otpCodeStore = new ConcurrentHashMap<>();

    // ─────────────────────────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────────────────────────

    public String sendOtp(String phone) {
        String otp = generateOtp();
        long expiresAt = System.currentTimeMillis() + OTP_TTL_MS;

        otpCodeStore.put(phone, otp);
        otpStore.put(phone, new long[]{expiresAt});

        log.info("[OTP-SIM] Phone={} OTP={}", phone, otp);
        postToWebhook(phone, otp);
        return otp;
    }


    public String peekOtp(String phone) {
        long[] meta = otpStore.get(phone);
        String stored = otpCodeStore.get(phone);
        if (meta == null || stored == null) return null;
        if (System.currentTimeMillis() > meta[0]) {
            otpStore.remove(phone);
            otpCodeStore.remove(phone);
            return null;
        }
        return stored;
    }


    public boolean verifyOtp(String phone, String otp) {
        long[] meta = otpStore.get(phone);
        String stored = otpCodeStore.get(phone);

        if (meta == null || stored == null) return false;
        if (System.currentTimeMillis() > meta[0]) {
            otpStore.remove(phone);
            otpCodeStore.remove(phone);
            return false;
        }
        if (stored.equals(otp)) {
            otpStore.remove(phone);
            otpCodeStore.remove(phone);
            return true;
        }
        return false;
    }

    // ─────────────────────────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────────────────────────

    private String generateOtp() {
        SecureRandom rng = new SecureRandom();
        int n = rng.nextInt(900000) + 100000; // 100000–999999
        return String.valueOf(n);
    }

    private void postToWebhook(String phone, String otp) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = String.format(
                "{\"phone\":\"%s\",\"message\":\"Ma OTP cua ban la: %s (het han sau 5 phut)\"}",
                phone, otp
            );

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(webhookUrl, entity, String.class);
            log.debug("[OTP] webhook sent for {}", phone);
        } catch (Exception e) {
            log.warn("[OTP] webhook unavailable: {}", e.getMessage());
        }
    }
}
