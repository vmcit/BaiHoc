package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller mẫu cho Spring Boot Application
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Spring Boot Application!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

    // ─── API: Danh sách sản phẩm (fake data) ────────────────────
    @GetMapping("/api/products")
    public List<Product> getProducts() {
        //call database .
        return List.of(
            new Product(1,  "Laptop Dell XPS 15",     25_990_000, "Laptop",      true,  4.8),
            new Product(2,  "iPhone 15 Pro Max",       33_990_000, "Điện thoại",  true,  4.9),
            new Product(3,  "Samsung Galaxy S24",      22_990_000, "Điện thoại",  true,  4.7),
            new Product(4,  "Tai nghe Sony WH-1000XM5", 7_490_000, "Phụ kiện",   true,  4.8),
            new Product(5,  "Bàn phím cơ Keychron K2",  2_190_000, "Phụ kiện",   false, 4.6),
            new Product(6,  "Màn hình LG 27\" 4K",     12_500_000, "Màn hình",   true,  4.7),
            new Product(7,  "MacBook Air M3",          28_990_000, "Laptop",      true,  4.9),
            new Product(8,  "Chuột Logitech MX Master", 1_890_000, "Phụ kiện",   true,  4.5),
            new Product(9,  "SSD Samsung 1TB",          2_290_000, "Linh kiện",   true,  4.8),
            new Product(10, "iPad Pro 13\"",            28_490_000, "Tablet",     false, 4.7)
        );
    }

    // ─── Record đại diện cho một sản phẩm ────────────────────────
    record Product(
        int    id,
        String name,
        long   price,
        String category,
        boolean inStock,
        double  rating
    ) {}
}

