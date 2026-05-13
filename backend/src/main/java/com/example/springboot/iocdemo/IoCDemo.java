package com.example.springboot.iocdemo;

/**
 * Demo main class - Hiển thị khái niệm Bean và IoC Container
 */
public class IoCDemo {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   DEMO: Bean và IoC Container");
        System.out.println("========================================\n");
        
        // ===== CÁCH 1: Tạo object trực tiếp (Tight Coupling) =====
        System.out.println(">>> CÁCH 1: Tạo object trực tiếp (Mà không dùng Container)");
        UserService service1 = new UserServiceImpl();
        service1.saveUser("Nguyễn Văn A");
        System.out.println("⚠ Problem: Code phụ thuộc trực tiếp vào implementation\n");
        
        // ===== CÁCH 2: Dùng IoC Container (Loose Coupling) =====
        System.out.println(">>> CÁCH 2: Dùng IoC Container (Best Practice)");
        SimpleIoCContainer container = new SimpleIoCContainer();
        
        // Lấy Bean từ Container và sử dụng
        UserService service2 = container.getUserService();
        service2.saveUser("Trần Thị B");
        service2.getUserInfo("Trần Thị B");
        
        // ===== CÁCH 3: Setter Injection =====
        System.out.println("\n>>> CÁCH 3: Setter Injection (Thay đổi Bean sau khi tạo)");
        SimpleIoCContainer container2 = new SimpleIoCContainer();
        
        // Tạo một Bean khác
        UserService customService = new UserServiceImpl() {
            @Override
            public void saveUser(String name) {
                System.out.println("[CUSTOM SERVICE] Lưu user custom: " + name);
            }
        };
        
        // Inject Bean mới vào Container
        container2.setUserService(customService);
        container2.executeUserOperation("Lê Văn C");
        
        // ===== CÁCH 4: Dùng Container để execute operation =====
        System.out.println("\n>>> CÁCH 4: Container quản lý và thực thi operation");
        SimpleIoCContainer container3 = new SimpleIoCContainer();
        container3.executeUserOperation("Phạm Văn D");
        
        System.out.println("\n========================================");
        System.out.println("   Kết luận:");
        System.out.println("   - Bean: Object được quản lý bởi Container");
        System.out.println("   - IoC: Container chịu trách nhiệm tạo & inject dependency");
        System.out.println("   - Lợi ích: Loose coupling, dễ test & maintain");
        System.out.println("========================================");
    }
}

