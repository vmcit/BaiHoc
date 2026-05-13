package org.example.reflection.simple.anotation.dependency.proxy;

public class RealPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Đang thực hiện thanh toán: " + amount + "$");
    }
}
