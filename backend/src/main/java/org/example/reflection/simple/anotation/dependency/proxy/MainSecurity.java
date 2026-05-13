package org.example.reflection.simple.anotation.dependency.proxy;

import java.lang.reflect.Proxy;

public class MainSecurity {
    public static void main(String[] args) {
        // 1. Giả lập người dùng dùng Token SAI
        Payment momoReal = new MomoPayment();
        Payment momoProxy = (Payment) Proxy.newProxyInstance(
                Payment.class.getClassLoader(),
                new Class[]{Payment.class},
                new SecurityProxyHandler(momoReal, "WRONG_TOKEN")
        );

        try {
            System.out.println("--- Thử thanh toán Momo với token sai ---");
            momoProxy.pay(50000);
        } catch (Exception e) {
            System.out.println("Kết quả: " + e.getMessage());
        }

        System.out.println("\n------------------------------------\n");

        // 2. Giả lập người dùng dùng Token ĐÚNG nhưng vượt hạn mức
        Payment zaloReal = new ZaloPay();
        Payment zaloProxy = (Payment) Proxy.newProxyInstance(
                Payment.class.getClassLoader(),
                new Class[]{Payment.class},
                new SecurityProxyHandler(zaloReal, "SECRET_TOKEN_123")
        );

        try {
            System.out.println("--- Thử thanh toán ZaloPay vượt hạn mức (2 triệu) ---");
            zaloProxy.pay(2000000);
        } catch (Exception e) {
            System.out.println("Kết quả: " + e.getMessage());
        }
    }
}
