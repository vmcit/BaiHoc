package org.example.generic.hoicham;

public class Payment {
    String description;
    double amount;

    public Payment(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public void process() {
        System.out.println("Đang xử lý thanh toán: " + description + " số tiền " + amount);
    }
}