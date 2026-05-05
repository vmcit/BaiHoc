package com.example.springboot.iocdemo;

/**
 * DEMO: IMMUTABLE vs MUTABLE - Chạy thử để hiểu rõ
 */
public class ImmutableVsMutableDemo {
    
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════════════════════");
        System.out.println("        DEMO: IMMUTABLE (Constructor) vs MUTABLE (Setter)");
        System.out.println("════════════════════════════════════════════════════════════════════════\n");
        
        // ═══════════════════════ PHẦN 1: MUTABLE (KHÔNG AN TOÀN) ═══════════════════════
        System.out.println("❌ PHẦN 1: MUTABLE với Setter Injection (KHÔNG AN TOÀN)\n");
        System.out.println("───────────────────────────────────────────────────────────");
        
        MutableExample mutable = new MutableExample();
        mutable.setName("Nguyễn Văn A");
        
        System.out.println("Lần 1: " + mutable.getName());
        
        // ⚠ Ai đó thay đổi tên giữa chừng
        System.out.println("\n⚠ Ai đó thay đổi name:");
        mutable.setName("Trần Thị B");
        
        System.out.println("Lần 2: " + mutable.getName());
        System.out.println("❌ Problem: Name bị thay đổi! Không an toàn!");
        
        // ═══════════════════════ PHẦN 2: IMMUTABLE (AN TOÀN) ═══════════════════════
        System.out.println("\n────────────────────────────────────────────────────────────\n");
        System.out.println("✅ PHẦN 2: IMMUTABLE với Constructor Injection (AN TOÀN)\n");
        System.out.println("───────────────────────────────────────────────────────────");
        
        ImmutableExample immutable = new ImmutableExample("Nguyễn Văn A");
        
        System.out.println("Lần 1: " + immutable.getName());
        System.out.println("✓ Name được gán vào constructor");
        
        // Cố gắng thay đổi (sẽ lỗi)
        System.out.println("\n⚠ Cố gắng thay đổi name...");
        System.out.println("immutable.name = \"Trần Thị B\"; // ❌ COMPILE ERROR!");
        System.out.println("               // Cannot assign to final variable");
        
        System.out.println("\nLần 2: " + immutable.getName());
        System.out.println("✅ Name không thể thay đổi! AN TOÀN!");
        
        // ═══════════════════════ PHẦN 3: THREAD-SAFETY ═══════════════════════
        System.out.println("\n────────────────────────────────────────────────────────────\n");
        System.out.println("🔒 PHẦN 3: Thread-Safety Demo\n");
        System.out.println("───────────────────────────────────────────────────────────");
        
        System.out.println("\n❌ MUTABLE (Không thread-safe):");
        System.out.println("   Thread 1: userService.setName(\"User1\")");
        System.out.println("   Thread 2:                          userService.setName(\"User2\")");
        System.out.println("   → Tên bị thay đổi, dữ liệu sai!");
        
        System.out.println("\n✅ IMMUTABLE (Thread-safe):");
        System.out.println("   Thread 1: new ImmutableExample(\"User1\")");
        System.out.println("   Thread 2: new ImmutableExample(\"User2\")");
        System.out.println("   → Mỗi thread có object riêng, an toàn!");
        
        // ═══════════════════════ PHẦN 4: CONSTRUCTOR PARAMETER ═══════════════════════
        System.out.println("\n────────────────────────────────────────────────────────────\n");
        System.out.println("📚 PHẦN 4: \"Constructor có nhiều parameter\" là gì?\n");
        System.out.println("───────────────────────────────────────────────────────────");
        
        System.out.println("\n❌ Nếu class phức tạp, constructor sẽ dài:");
        System.out.println("""
            public class ComplexService {
                public ComplexService(
                    Database db,           // 1
                    EmailService email,    // 2
                    NotificationService notify,  // 3
                    LoggingService log,    // 4
                    CacheService cache,    // 5
                    ConfigService config,  // 6
                    ValidationService val  // 7
                ) { ... }
            }
            
            Tạo object:
            new ComplexService(db, email, notify, log, cache, config, val);
            
            ❌ Khó đọc, dễ truyền sai thứ tự!
            """);
        
        System.out.println("\n✅ Giải pháp: Dùng Builder Pattern:");
        System.out.println("""
            new ComplexService.Builder()
                .withDatabase(db)
                .withEmailService(email)
                .withNotificationService(notify)
                .build();
            
            ✓ Rõ ràng, dễ hiểu!
            """);
        
        // ═══════════════════════ KẾT LUẬN ═══════════════════════
        System.out.println("\n────────────────────────────────────────────────────────────");
        System.out.println("💡 KẾT LUẬN:");
        System.out.println("────────────────────────────────────────────────────────────");
        System.out.println("""
            
            1️⃣ IMMUTABLE là ĐIỀU TỐT:
               • Không thể thay đổi → An toàn
               • Thread-safe → Không lo race condition
               • Dễ debug → Không lo ai thay đổi
            
            2️⃣ "Constructor có nhiều parameter" không phải lỗi:
               • Là TẤN HIỆU: class làm quá nhiều việc
               • Giải pháp: tách class hoặc dùng Builder
               • Thường: 3-5 parameter là OK
            
            3️⃣ Best Practice:
               ✅ Dùng Constructor Injection (IMMUTABLE)
               ✅ Giữ dependency < 5 cái
               ✅ Dùng Builder nếu cần nhiều parameter
            """);
    }
}

/**
 * ❌ MUTABLE Example (Setter Injection)
 * - Có thể thay đổi
 * - Không thread-safe
 */
class MutableExample {
    private String name;  // ← Có thể thay đổi!
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}

/**
 * ✅ IMMUTABLE Example (Constructor Injection)
 * - Không thể thay đổi
 * - Thread-safe
 */
class ImmutableExample {
    private final String name;  // ← final: không thể thay đổi!
    
    public ImmutableExample(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name không được null!");
        }
        this.name = name;  // ← Gán LẦN DUY NHẤT
    }
    
    public String getName() {
        return name;
    }
}

/**
 * Builder Pattern - Giải pháp cho "Constructor có nhiều parameter"
 */
class ComplexServiceBuilder {
    private String database;
    private String emailService;
    private String notificationService;
    private String loggingService;
    
    public ComplexServiceBuilder withDatabase(String db) {
        this.database = db;
        return this;
    }
    
    public ComplexServiceBuilder withEmailService(String email) {
        this.emailService = email;
        return this;
    }
    
    public ComplexServiceBuilder withNotificationService(String notify) {
        this.notificationService = notify;
        return this;
    }
    
    public ComplexServiceBuilder withLoggingService(String log) {
        this.loggingService = log;
        return this;
    }
    
    public void build() {
        System.out.println("✅ ComplexService built with:");
        System.out.println("  - database: " + database);
        System.out.println("  - emailService: " + emailService);
        System.out.println("  - notificationService: " + notificationService);
        System.out.println("  - loggingService: " + loggingService);
    }
}

