package com.example.springboot.iocdemo;

/**
 * DEMO: Constructor Injection - Chạy thử
 */
public class ConstructorInjectionDemo {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("   ✓ DEMO: CONSTRUCTOR INJECTION");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        System.out.println("📝 Khái niệm:");
        System.out.println("   - Dependency được inject qua Constructor");
        System.out.println("   - Dependency bắt buộc phải có (không null)");
        System.out.println("   - Immutable: Không thể thay đổi sau khi tạo\n");
        
        System.out.println(">>> Tạo UserService Bean:");
        UserService userService = new UserServiceImpl();
        
        System.out.println("\n>>> Tạo ConstructorInjectionExample với Constructor Injection:");
        ConstructorInjectionExample example = new ConstructorInjectionExample(userService);
        
        System.out.println("\n>>> Sử dụng Bean để execute operation:");
        example.execute("Nguyễn Văn A");
        
        System.out.println("\n>>> Sử dụng lần thứ 2:");
        example.execute("Trần Thị B");
        
        System.out.println("\n───────────────────────────────────────────────────────────");
        System.out.println("✓ Ưu điểm:");
        System.out.println("  ✓ Dependency bắt buộc (compile-time safe)");
        System.out.println("  ✓ Immutable - không thể thay đổi sau tạo");
        System.out.println("  ✓ Thread-safe");
        System.out.println("  ✓ Dễ test - không cần reflection");
        
        System.out.println("\n✗ Nhược điểm:");
        System.out.println("  ✗ Constructor có nhiều parameter khó đọc");
        System.out.println("  ✗ Khó xử lý circular dependency");
        
        System.out.println("\n💡 Khi nào dùng:");
        System.out.println("  ✓ Dependency là BẮTBUỘC");
        System.out.println("  ✓ Cần dependency immutable");
        System.out.println("  ✓ Spring Boot ưu tiên này");
        
        System.out.println("\n═══════════════════════════════════════════════════════════\n");
    }
}

/**
 * Constructor Injection Example
 */
class ConstructorInjectionExample {
    
    private UserService userService;
    
    // Constructor Injection: dependency đi vào qua constructor
    public ConstructorInjectionExample(UserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("userService không được null!");
        }
        this.userService = userService;
        System.out.println("  ✓ ConstructorInjectionExample được tạo với UserService");
    }
    
    public void execute(String name) {
        userService.saveUser(name);
        userService.getUserInfo(name);
    }
}

