package org.example.reflection.simple.anotation.dependency;

public class Calculator {
    public int total = 0;
    public void add(int value) {
        total += value;
    }
    public static void main(String[] args) {
        // Người thứ nhất muốn cộng 5
        Calculator cal1 = new Calculator();
        cal1.add(5);
        System.out.println("Tổng 1: " + cal1.total); // Kết quả: 5

        // Người thứ hai muốn cộng tiếp 10 vào "tổng chung"
        Calculator cal2 = new Calculator();
        cal2.add(10);
        System.out.println("Tổng 2: " + cal2.total); // Kết quả: 10 (Mất tiêu số 5 trước đó!)
    }
}
