package org.example.reflection.simple.anotation.dependency.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class SecurityProxyHandler implements InvocationHandler {
    private Object target;
    private String userToken; // Giả lập token của người dùng

    public SecurityProxyHandler(Object target, String userToken) {
        this.target = target;
        this.userToken = userToken;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // --- LOGIC BẢO MẬT (KIỂM SOÁT TRUY CẬP) ---
        System.out.println("[Security Check] Đang kiểm tra quyền truy cập cho hàm: " + method.getName());

        if (!isValidToken(userToken)) {
            System.err.println("[Security Alert] Token không hợp lệ! Truy cập bị chặn.");
            throw new SecurityException("Bạn không có quyền thực hiện giao dịch này.");
        }

        // Nếu là hàm pay, kiểm tra thêm số tiền (Giả lập hạn mức)
        if (method.getName().equals("pay")) {
            double amount = (double) args[0];
            if (amount > 1000000) { // Hạn mức 1 triệu
                throw new SecurityException("Giao dịch vượt quá hạn mức cho phép!");
            }
        }

        // --- NẾU MỌI THỨ ỔN, MỚI GỌI ĐỐI TƯỢNG THẬT ---
        return method.invoke(target, args);
    }

    private boolean isValidToken(String token) {
        return "SECRET_TOKEN_123".equals(token); // Giả lập kiểm tra token đúng
    }
}