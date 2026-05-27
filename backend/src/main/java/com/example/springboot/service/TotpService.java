package com.example.springboot.service;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class TotpService {

    private final DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator(32);

    public String generateSecret() {
        return secretGenerator.generate();
    }

    /**
     * Trả về QR code dưới dạng data URI (base64 PNG) để nhúng thẳng vào <img src="...">
     */
    public String generateQrDataUri(String phone, String secret) throws QrGenerationException {
        QrData data = new QrData.Builder()
                .label(phone)
                .secret(secret)
                .issuer("BaiHoc")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        ZxingPngQrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);
        return getDataUriForImage(imageData, generator.getImageMimeType());
    }

    /**
     * Xác minh mã 6 số người dùng nhập từ Google Authenticator
     */
    public boolean verifyCode(String secret, String code) {
        CodeVerifier verifier = new DefaultCodeVerifier(
                new DefaultCodeGenerator(),
                new SystemTimeProvider()
        );
        return verifier.isValidCode(secret, code);
    }
}
