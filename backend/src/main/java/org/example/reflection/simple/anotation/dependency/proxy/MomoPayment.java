package org.example.reflection.simple.anotation.dependency.proxy;

class MomoPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Thực hiện thanh toán qua Momo: " + amount + "đ");
    }
}
