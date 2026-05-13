package com.example.springboot.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * EmailService - Xử lý gửi email
 */
@Service
public class EmailService {
    
    private final JavaMailSender javaMailSender;
    
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    
    /**
     * Gửi email quên mật khẩu
     */
    public void sendPasswordResetEmail(String toEmail, String username, String resetToken) {
        try {
            String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
            
            String emailBody = "Xin chào " + username + ",\n\n" +
                    "Bạn đã yêu cầu reset mật khẩu. Vui lòng sử dụng mã reset bên dưới:\n\n" +
                    "🔐 MÃ RESET: " + resetToken + "\n\n" +
                    "Hoặc click vào link bên dưới:\n" +
                    resetLink + "\n\n" +
                    "⚠️  Mã này sẽ hết hạn sau 15 phút.\n" +
                    "⚠️  Nếu bạn không yêu cầu reset mật khẩu, vui lòng bỏ qua email này.\n\n" +
                    "Trân trọng,\n" +
                    "BaiHoc Team";
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("🔐 Yêu cầu Reset Mật khẩu - BaiHoc");
            message.setText(emailBody);
            message.setFrom("noreply@baihoc.com");
            
            javaMailSender.send(message);
            
            System.out.println("✓ Email gửi thành công tới: " + toEmail);
        } catch (Exception e) {
            System.err.println("✗ Lỗi gửi email: " + e.getMessage());
            throw new RuntimeException("Không thể gửi email: " + e.getMessage());
        }
    }
    
    /**
     * Gửi email chào mừng đăng ký
     */
    public void sendWelcomeEmail(String toEmail, String username, String fullName) {
        try {
            String emailBody = "Xin chào " + fullName + ",\n\n" +
                    "👋 Chào mừng bạn đến với BaiHoc!\n\n" +
                    "Tài khoản của bạn đã được tạo thành công:\n" +
                    "  • Username: " + username + "\n" +
                    "  • Email: " + toEmail + "\n\n" +
                    "📌 Bạn có thể đăng nhập ngay tại:\n" +
                    "   http://localhost:8080\n\n" +
                    "Nếu có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi.\n\n" +
                    "Cảm ơn,\n" +
                    "BaiHoc Team";
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("👋 Chào mừng đến BaiHoc!");
            message.setText(emailBody);
            message.setFrom("noreply@baihoc.com");
            
            javaMailSender.send(message);
            
            System.out.println("✓ Email chào mừng gửi thành công tới: " + toEmail);
        } catch (Exception e) {
            System.err.println("✗ Lỗi gửi email chào mừng: " + e.getMessage());
            // Không throw exception, chỉ log vì đây là email không quan trọng
        }
    }
    
    /**
     * Gửi email reset mật khẩu thành công
     */
    public void sendPasswordResetSuccessEmail(String toEmail, String username) {
        try {
            String emailBody = "Xin chào " + username + ",\n\n" +
                    "✓ Mật khẩu của bạn đã được reset thành công!\n\n" +
                    "Bạn có thể đăng nhập bằng mật khẩu mới của mình.\n\n" +
                    "🔒 Nếu bạn không thực hiện hành động này, vui lòng liên hệ với chúng tôi ngay.\n\n" +
                    "Trân trọng,\n" +
                    "BaiHoc Team";
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("✓ Mật khẩu đã reset thành công");
            message.setText(emailBody);
            message.setFrom("noreply@baihoc.com");
            
            javaMailSender.send(message);
            
            System.out.println("✓ Email xác nhận reset thành công gửi tới: " + toEmail);
        } catch (Exception e) {
            System.err.println("✗ Lỗi gửi email xác nhận: " + e.getMessage());
        }
    }
}

