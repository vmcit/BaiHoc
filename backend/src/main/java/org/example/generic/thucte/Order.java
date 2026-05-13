package org.example.generic.thucte;

public class Order {
    private String orderId;
    private double totalAmount;

    public Order(String orderId, double totalAmount) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Đơn hàng: " + orderId + " (Tổng: " + totalAmount + ")";
    }
}
