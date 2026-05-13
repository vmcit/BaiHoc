package org.example.generic.thucte;

public class Product {
    String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Sản phẩm: " + name + " (Giá: " + price + ")";
    }
}
