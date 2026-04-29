package org.example.reflection.simple.anotation.dependency.proxy;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        // 1. Đối tượng thật
        Payment realPayment = new RealPayment();
        System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");

        // 2. Tạo Proxy "đại diện" cho đối tượng thật
        Payment proxyInstance = (Payment) Proxy.newProxyInstance(
                Payment.class.getClassLoader(),
                new Class[] { Payment.class },
                new PaymentProxyHandler(realPayment)
        );

        // 3. Gọi hàm thông qua Proxy
        proxyInstance.pay(500.0);

    }
}


//public class $Proxy0 implements Payment {
//    private InvocationHandler handler;
//
//    public $Proxy0(InvocationHandler h) {
//        this.handler = h;
//    }
//
//    // Java tự viết hàm pay() cho Class giả này
//    @Override
//    public void pay(double amount) {
//        // Thay vì xử lý, nó gom tất cả thông tin lại...
//        Method method = Payment.class.getMethod("pay", double.class);
//        Object[] args = new Object[] { amount };
//
//        // ... rồi "ném" hết sang cho cái Handler mà bạn đã định nghĩa!
//        this.handler.invoke(this, method, args);
//    }
//}
