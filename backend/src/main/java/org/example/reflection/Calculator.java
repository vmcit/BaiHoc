package org.example.reflection;

public class Calculator {
    public void add(int a, int b) {
        System.out.println("Kết quả cộng: " + (a + b));
    }

    private void secretCalc(int n) {
        System.out.println("Tính toán bí mật với số: " + n);
    }
}
