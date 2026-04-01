package org.example.generic.thucte;

public class EcommerceApp {
    public static void main(String[] args) {
        // ko dùng generic
//        ProductRepository pRepo = new ProductRepository();
//        pRepo.save(new Product("iPhone", 1000));
//
//        OrderRepository oRepo = new OrderRepository();
//        oRepo.save(new Order("BILL01", 5000));

        // kieu chung
//        ObjectRepository repo = new ObjectRepository();
//
//        repo.save(new Product("iPhone", 1000));
//        repo.save(new Order("BILL01", 5000));
//
//        // KHI LẤY RA: Bạn phải ép kiểu thủ công
//        Product p = (Product) repo.get(1);
//        System.out.println("Tên sản phẩm: " + p.name);


        // Quản lý sản phẩm
        BaseRepository<Product> productRepo = new BaseRepository<>();
        productRepo.save(new Product("iPhone 15", 1000));

        // Quản lý đơn hàng
        BaseRepository<Order> orderRepo = new BaseRepository<>();
        orderRepo.save(new Order("ORD-001", 1000));

        // Cả 2 đều dùng chung code của BaseRepository nhưng vẫn an toàn về kiểu dữ liệu
    }
}
