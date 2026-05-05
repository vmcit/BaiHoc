package com.example.springboot.iocdemo;

/**
 * DEMO: Setter Injection - Chạy thử
 */
public class SetterInjectionDemo {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("   ✓ DEMO: SETTER INJECTION");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        System.out.println("📝 Khái niệm:");
        System.out.println("   - Dependency được inject qua Setter method");
        System.out.println("   - Dependency có thể null (optional)");
        System.out.println("   - Mutable: Có thể thay đổi sau khi tạo\n");
        
        System.out.println(">>> Tạo SetterInjectionExample (chưa inject):");
        SetterInjectionExample example = new SetterInjectionExample();
        
        System.out.println("\n>>> Gọi execute trước khi inject (sẽ có warning):");
        example.execute("Nguyễn Văn A");
        
        System.out.println("\n>>> Inject UserService qua Setter:");
        UserService userService = new UserServiceImpl();
        example.setUserService(userService);
        
        System.out.println("\n>>> Gọi execute sau khi inject:");
        example.execute("Trần Thị B");
        
        System.out.println("\n>>> Thay đổi Bean (Mutable):");
        UserService customService = new UserServiceImpl() {
            @Override
            public void saveUser(String name) {
                System.out.println("[CUSTOM] Saving: " + name);
            }
        };
        example.setUserService(customService);
        example.execute("Lê Văn C");
        
        System.out.println("\n────────────────────────────────────���──────────────────────");
        System.out.println("✓ Ưu điểm:");
        System.out.println("  ✓ Linh hoạt - thay đổi Bean runtime");
        System.out.println("  ✓ Optional dependency");
        System.out.println("  ✓ Giải quyết circular dependency");
        System.out.println("  ✓ Constructor đơn giản");
        
        System.out.println("\n✗ Nhược điểm:");
        System.out.println("  ✗ Object có thể incomplete nếu quên inject");
        System.out.println("  ✗ Không thread-safe (thay đổi sau tạo)");
        System.out.println("  ✗ Không rõ dependency bắt buộc hay optional");
        System.out.println("  ✗ Runtime errors (NullPointerException)");
        
        System.out.println("\n💡 Khi nào dùng:");
        System.out.println("  ○ Dependency là OPTIONAL");
        System.out.println("  ○ Cần thay đổi Bean runtime");
        System.out.println("  ○ Không có nhiều dependency");
        
        System.out.println("\n═══════════════════════════════════════════════════════════\n");
    }
}

/**
 * Setter Injection Example
 */
class SetterInjectionExample {
    
    private UserService userService;
    
    // Default constructor
    public SetterInjectionExample() {
        System.out.println("  ✓ SetterInjectionExample được tạo");
    }
    
    // Setter Injection: dependency đi vào qua method
    public void setUserService(UserService userService) {
        this.userService = userService;
        System.out.println("  ✓ UserService đã được inject via Setter");
    }
    
    public void execute(String name) {
        if (userService != null) {
            userService.saveUser(name);
            userService.getUserInfo(name);
        } else {
            System.out.println("  ⚠ WARNING: userService = null (chưa inject!)");
        }
    }
}

