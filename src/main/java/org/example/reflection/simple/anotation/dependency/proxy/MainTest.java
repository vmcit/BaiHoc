package org.example.reflection.simple.anotation.dependency.proxy;

import java.lang.reflect.Proxy;

public class MainTest {
    public static void main(String[] args) {
        // 1. Đối tượng Momo thật
        Payment momo = new MomoPayment();

        // 2. Tạo Proxy cho Momo
        Payment momoProxy = (Payment) Proxy.newProxyInstance(
                Payment.class.getClassLoader(),
                new Class[]{Payment.class},
                new LoggingProxyHandler(momo)
        );

        // 3. Đối tượng ZaloPay thật
        Payment zalo = new ZaloPay();

        // 4. Tạo Proxy cho ZaloPay (Dùng chung LoggingProxyHandler luôn!)
        Payment zaloProxy = (Payment) Proxy.newProxyInstance(
                Payment.class.getClassLoader(),
                new Class[]{Payment.class},
                new LoggingProxyHandler(zalo)
        );

        // CHẠY THỬ
        momoProxy.pay(50000); // Tự động có Log
        zaloProxy.pay(100000); // Tự động có Log
    }
}
