package com.example.springboot.iocdemo;

/**
 * DEMO THỰC TẾ: So sánh các loại Dependency Injection
 */
public class InjectionComparisonDemo {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("   SO SÁNH: CONSTRUCTOR vs SETTER vs FIELD INJECTION");
        System.out.println("═════════════════���═════════════════════════════════════════\n");
        
        // ════════════════════ CÁCH 1: CONSTRUCTOR INJECTION ════════════════════
        System.out.println(">>> 1️⃣  CONSTRUCTOR INJECTION");
        System.out.println("─── Đặc điểm: Dependency bắt buộc, Immutable ───");
        UserService service1 = new UserServiceImpl();
        ConstructorInjectionExample example1 = new ConstructorInjectionExample(service1);
        example1.execute("Nguyễn Văn A");
        System.out.println("✓ Lợi ích: Dependency bắt buộc, an toàn, dễ test");
        System.out.println("✗ Hạn chế: Constructor có nhiều parameter\n");
        
        // ════════════════════ CÁCH 2: SETTER INJECTION ════════════════════
        System.out.println(">>> 2️⃣  SETTER INJECTION");
        System.out.println("─── Đặc điểm: Dependency optional, Mutable ───");
        SetterInjectionExample example2 = new SetterInjectionExample();
        System.out.println("⚠ Lúc này userService = null (chưa inject)");
        example2.execute("Trần Thị B");  // Warning: userService null
        
        System.out.println("\nGọi setter để inject:");
        example2.setUserService(new UserServiceImpl());
        example2.execute("Trần Thị B");  // Bây giờ ok
        System.out.println("✓ Lợi ích: Linh hoạt, thay đổi runtime");
        System.out.println("✗ Hạn chế: Có thể quên inject (NullPointerException)\n");
        
        // ════════════════════ CÁCH 3: FIELD INJECTION ════════════════════
        System.out.println(">>> 3️⃣  FIELD INJECTION (Spring @Autowired)");
        System.out.println("─── Đặc điểm: Gọn nhất, dùng Reflection ───");
        FieldInjectionExample example3 = new FieldInjectionExample();
        System.out.println("⚠ Lúc này userService = null (chưa inject)");
        example3.execute("Lê Văn C");  // Warning: userService null
        
        System.out.println("\n(Giả sử Spring đã inject via @Autowired)");
        System.out.println("✓ Lợi ích: Gọn, ít code, sạch");
        System.out.println("✗ Hạn chế: Khó test, hidden dependency\n");
        
        // ════════════════════ BEAN SCOPE DEMO ════════════════════
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("   BEAN SCOPE: SINGLETON vs PROTOTYPE");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        // SINGLETON: Tất cả request dùng chung 1 object
        System.out.println(">>> SINGLETON SCOPE");
        System.out.println("─── 1 object cho toàn bộ ứng dụng ───");
        SingletonBeanExample singleton1 = SingletonBeanExample.getInstance();
        SingletonBeanExample singleton2 = SingletonBeanExample.getInstance();
        
        singleton1.increaseCounter();
        singleton1.increaseCounter();
        
        System.out.println("Request 1 - Counter: " + singleton1.getCounter());
        System.out.println("Request 2 - Counter: " + singleton2.getCounter());
        System.out.println("⚠ Chú ý: singleton1 === singleton2? " + (singleton1 == singleton2));
        System.out.println("⚠ Vấn đề: State chia sẻ, không thread-safe\n");
        
        // PROTOTYPE: Mỗi request tạo object mới
        System.out.println(">>> PROTOTYPE SCOPE");
        System.out.println("─── Object mới cho mỗi request ───");
        PrototypeBeanExample proto1 = new PrototypeBeanExample();
        PrototypeBeanExample proto2 = new PrototypeBeanExample();
        
        proto1.increaseCounter();
        proto1.increaseCounter();
        
        System.out.println("Request 1 - Counter: " + proto1.getCounter());
        System.out.println("Request 2 - Counter: " + proto2.getCounter());
        System.out.println("✓ Chú ý: proto1 === proto2? " + (proto1 == proto2));
        System.out.println("✓ Lợi ích: Mỗi request độc lập, thread-safe\n");
        
        // ════════════════════ KHUYẾN NGHỊ ════════════════════
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("   💡 KHUYẾN NGHỊ BEST PRACTICE");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("""
            
            1. Dependency Injection:
               ✓ Dùng CONSTRUCTOR INJECTION (ưu tiên nhất)
               ○ Dùng SETTER INJECTION cho optional dependency
               ✗ Tránh FIELD INJECTION (@Autowired trên field)
            
            2. Bean Scope:
               ✓ Dùng SINGLETON (mặc định) cho Service, Repository
                 → Stateless, tối ưu memory & performance
               
               ✓ Dùng PROTOTYPE cho stateful object
                 → Mỗi request có object riêng, thread-safe
               
               ✓ Dùng REQUEST cho Web Controllers
                 → 1 object/HTTP request
               
               ✓ Dùng SESSION cho user data
                 → 1 object/user session
            
            3. Lưu ý:
               ⚠ Singleton: Tránh có state (mutable field)
               ⚠ Prototype: Không quên cleanup resources
               ⚠ Thread-safety: Luôn cân nhắc khi dùng Singleton
            
            """);
    }
}

/**
 * SINGLETON: Container tạo 1 instance duy nhất
 */
class SingletonBeanExample {
    private static SingletonBeanExample instance;
    private int counter = 0;
    
    private SingletonBeanExample() {}
    
    public static synchronized SingletonBeanExample getInstance() {
        if (instance == null) {
            instance = new SingletonBeanExample();
            System.out.println("✓ Singleton Bean được tạo (chỉ lần đầu)");
        }
        return instance;
    }
    
    public void increaseCounter() {
        counter++;
    }
    
    public int getCounter() {
        return counter;
    }
}

/**
 * PROTOTYPE: Container tạo instance mới mỗi lần
 */
class PrototypeBeanExample {
    private int counter = 0;
    
    public PrototypeBeanExample() {
        System.out.println("✓ Prototype Bean được tạo (mỗi lần request)");
    }
    
    public void increaseCounter() {
        counter++;
    }
    
    public int getCounter() {
        return counter;
    }
}

