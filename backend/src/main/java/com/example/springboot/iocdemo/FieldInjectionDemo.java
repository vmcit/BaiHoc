package com.example.springboot.iocdemo;

/**
 * DEMO: Field Injection - Chạy thử
 */
public class FieldInjectionDemo {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("   ✓ DEMO: FIELD INJECTION (@Autowired)");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        System.out.println("📝 Khái niệm:");
        System.out.println("   - Dependency được inject trực tiếp vào Field");
        System.out.println("   - Spring dùng Reflection để set giá trị");
        System.out.println("   - Gọn nhất nhưng cũng nguy hiểm nhất\n");
        
        System.out.println(">>> Tạo FieldInjectionExample (chưa inject):");
        FieldInjectionExample example = new FieldInjectionExample();
        
        System.out.println("\n>>> Gọi execute trước khi inject (sẽ có warning):");
        example.execute("Nguyễn Văn A");
        
        System.out.println("\n>>> Simulate: Spring inject via Reflection:");
        example.injectUserService(new UserServiceImpl());  // Giả lập @Autowired
        
        System.out.println("\n>>> Gọi execute sau khi inject:");
        example.execute("Trần Thị B");
        
        System.out.println("\n───────────────────────��────────────────────────────────────");
        System.out.println("✓ Ưu điểm:");
        System.out.println("  ✓ Gọn nhất - chỉ cần @Autowired annotation");
        System.out.println("  ✓ Linh hoạt như Setter");
        System.out.println("  ✓ Ít code boilerplate");
        
        System.out.println("\n✗ Nhược điểm:");
        System.out.println("  ✗ Kh�� test - cần Reflection");
        System.out.println("  ✗ Hidden dependency - không rõ cần gì");
        System.out.println("  ✗ Không thể dùng final fields");
        System.out.println("  ✗ NullPointerException nếu không inject");
        System.out.println("  ✗ Dễ quên inject (không lỗi compile)");
        System.out.println("  ✗ Khó debug");
        
        System.out.println("\n💡 Khi nào dùng:");
        System.out.println("  ✗ Tránh dùng (không khuyến nghị)");
        System.out.println("  ○ Chỉ dùng khi:Builder pattern");
        System.out.println("  ○ Thường Spring Framework không khuyến nghị");
        
        System.out.println("\n⚠ Cảnh báo:");
        System.out.println("  Google không khuyến nghị Field Injection");
        System.out.println("  Spring Team cũng khuyến nghị Constructor Injection");
        
        System.out.println("\n═════���═════════════════════════════════════════════════════\n");
    }
}

/**
 * Field Injection Example
 */
class FieldInjectionExample {
    
    // Field injection (giả sử có @Autowired)
    private UserService userService;
    
    public FieldInjectionExample() {
        System.out.println("  ✓ FieldInjectionExample được tạo");
    }
    
    // Simulate Spring Reflection injection
    public void injectUserService(UserService service) {
        this.userService = service;
        System.out.println("  ✓ UserService đã được inject via Reflection");
    }
    
    public void execute(String name) {
        if (userService != null) {
            userService.saveUser(name);
            userService.getUserInfo(name);
        } else {
            System.out.println("  ⚠ WARNING: userService = null (chưa inject!)");
            System.out.println("  ⚠ PROBLEM: NullPointerException sẽ xảy ra!");
        }
    }
}

