package org.example.reflection.simple.anotation.dependency.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

class LoggingProxyHandler implements InvocationHandler {
    private Object target; // Đối tượng thật (Momo hoặc ZaloPay)

    public LoggingProxyHandler(Object target) {
        this.target = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Logic thêm vào mà không cần sửa code gốc
        System.out.println("[LOG] Bắt đầu giao dịch lúc: " + LocalDateTime.now());
        System.out.println("[LOG] Phương thức đang gọi: " + method.getName());

        // Chạy hàm gốc (Momo hoặc ZaloPay sẽ thực hiện trả tiền ở đây)
        Object result = method.invoke(target, args);

        System.out.println("[LOG] Giao dịch kết thúc thành công.");
        System.out.println("------------------------------------");
        return result;
    }
}
