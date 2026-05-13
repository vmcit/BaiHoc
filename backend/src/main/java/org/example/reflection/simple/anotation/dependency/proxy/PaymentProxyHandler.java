package org.example.reflection.simple.anotation.dependency.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class PaymentProxyHandler implements InvocationHandler {
    private Object realObject;

    public PaymentProxyHandler(Object realObject) {
        this.realObject = realObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Logic TRƯỚC khi gọi hàm thật
        System.out.println("[LOG] Bắt đầu giao dịch lúc: " + new java.util.Date());

        // Gọi hàm thật trên đối tượng thật
        Object result = method.invoke(realObject, args);

        // Logic SAU khi gọi hàm thật
        System.out.println("[LOG] Giao dịch hoàn tất thành công!");
        return result;
    }
}
