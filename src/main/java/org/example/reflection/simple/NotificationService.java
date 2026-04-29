package org.example.reflection.simple;

import java.lang.reflect.Method;

public class NotificationService {
    // Các hàm xử lý cụ thể (có thể là private để đóng gói)
    private void handleEmail(String msg) { System.out.println("Gửi Email: " + msg); }
    private void handleSMS(String msg) { System.out.println("Gửi SMS: " + msg); }
    private void handleZalo(String msg) { System.out.println("Gửi Zalo: " + msg); }

    public void process(String type, String message) {
        try {
            // Quy tắc: type là "Email" thì hàm tương ứng là "handleEmail"
            String methodName = "handle" + type;

            // 1. Tìm hàm dựa trên tên biến động
            Method method = this.getClass().getDeclaredMethod(methodName, String.class);

            // 2. Bẻ khóa nếu hàm là private
            method.setAccessible(true);

            // 3. Thực thi
            method.invoke(this, message);

        } catch (NoSuchMethodException e) {
            System.out.println("Lỗi: Không hỗ trợ kênh thông báo: " + type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NotificationService service = new NotificationService();

        // Dữ liệu này có thể đến từ Database hoặc API
        service.process("Email", "Chào mừng bạn!");
        service.process("Zalo", "Mã OTP của bạn là 1234");
        service.process("TikTok", "Tin nhắn này sẽ báo lỗi vì chưa có hàm handleTikTok");
    }
}
