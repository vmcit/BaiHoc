package org.example.reflection.simple.anotation.dependency.proxy;

class ZaloPay implements Payment {
    public void pay(double amount) {
        System.out.println("Thực hiện thanh toán qua ZaloPay: " + amount + "đ");
    }
}
