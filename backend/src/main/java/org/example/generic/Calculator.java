package org.example.generic;

public class Calculator<T extends Number> {
    private T number;

    public Calculator(T number) {
        this.number = number;
    }

    public double square() {
        // Vì T chắc chắn là Number, ta có thể gọi hàm doubleValue()
        return number.doubleValue() * number.doubleValue();
    }

    public static void main(String[] args) {
        Calculator<Integer> calcInt = new Calculator<>(5); // OK
        Calculator<Double> calcDouble = new Calculator<>(5.5); // OK
    }
}
