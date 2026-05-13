package com.example.springboot.iocdemo;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *           GIẢI THÍCH TOÀN DIỆN: CÁC LOẠI BEAN & DEPENDENCY INJECTION
 * ════════════════════════════════════════════════════════════════════════════
 * 
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║                                                                            ║
 * ║ 1. CONSTRUCTOR INJECTION (✓ Khuyến nghị nhất)                             ║
 * ║                                                                            ║
 * ╚════════════════════════════════════════════════════════════════════════════╝
 * 
 * Khái niệm:
 *    • Inject dependency qua Constructor parameter
 *    • Immutable: Không thể thay đổi dependency sau khi tạo
 *    • Dependency bắt buộc phải cung cấp
 * 
 * Ưu điểm (✓):
 *    ✓ Dependency bắt buộc (không null) - AN TOÀN
 *    ✓ Immutable - thread-safe
 *    ✓ Dễ test (không cần reflection/mock)
 *    ✓ Dễ theo dõi dependency (rõ ràng)
 *    ✓ Compile-time safety
 * 
 * Nhược điểm (✗):
 *    ✗ Constructor có nhiều parameter (Constructor Hell)
 *    ✗ Circular dependency khó xử lý
 * 
 * Khi dùng:
 *    ✓ Dependency là BẮTBUỘC
 *    ✓ Cần immutable objects
 *    ✓ Spring Boot ưu tiên (services, repositories)
 * 
 * Xem: ConstructorInjectionDemo.java để chạy thử
 * ────────────────────────────────────────────────────────────────────────────
 * 
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║                                                                            ║
 * ║ 2. SETTER INJECTION (○ Dùng khi cần optional)                             ║
 * ║                                                                            ║
 * ╚══════════════════════════════════��═════════════════════════════════════════╝
 * 
 * Khái niệm:
 *    • Inject dependency qua Setter method
 *    • Mutable: Có thể thay đổi dependency sau khi tạo
 *    • Dependency là OPTIONAL (có thể null)
 * 
 * Ưu điểm (✓):
 *    ✓ Linh hoạt - thay đổi dependency runtime
 *    ✓ Optional dependency
 *    ✓ Giải quyết circular dependency
 *    ✓ Constructor đơn giản (không phức tạp)
 * 
 * Nhược điểm (✗):
 *    ✗ Object có thể incomplete nếu quên inject
 *    ✗ Không thread-safe (mutable)
 *    ✗ Không rõ dependency bắt buộc hay optional
 *    ✗ Runtime NullPointerException
 * 
 * Khi dùng:
 *    ○ Dependency là OPTIONAL
 *    ○ Cần thay đổi Bean runtime
 *    ○ Không có circular dependency
 * 
 * Xem: SetterInjectionDemo.java để chạy thử
 * ────────────────────────────────────────────────────────────────────────────
 * 
 * ╔═════════════════��══════════════════════════════════════════════════════════╗
 * ║                                                                            ║
 * ║ 3. FIELD INJECTION (✗ KHÔNG khuyến nghị)                                  ║
 * ║                                                                            ║
 * ╚════════════════════════════════════════════════════════════════���═══════════╝
 * 
 * Khái niệm:
 *    • Inject dependency trực tiếp vào field (Spring @Autowired)
 *    • Dùng Reflection để set giá trị
 *    • Gọn nhất nhưng cũng nguy hiểm nhất
 * 
 * Ưu điểm (✓):
 *    ✓ Gọn - chỉ cần @Autowired annotation
 *    ✓ Linh hoạt như Setter
 *    ✓ Ít code boilerplate
 * 
 * Nhược điểm (✗):
 *    ✗ Khó test (cần Reflection/mock framework)
 *    ✗ Hidden dependency - không rõ cần gì
 *    ✗ Không thể dùng final fields
 *    ✗ NullPointerException nếu không inject
 *    ✗ Dễ quên inject (không lỗi compile)
 *    ✗ Khó debug
 * 
 * Khi dùng:
 *    ✗ TRÁNH dùng (không khuyến nghị)
 *    ○ Spring Team & Google khuyến nghị dùng Constructor thay thế
 * 
 * Xem: FieldInjectionDemo.java để hiểu tại sao không nên dùng
 * ────────────────────────────────────────────────────────────────────────────
 * 
 * ╔════════════════════════════════════════════════════════════════════════════╗
 * ║                                                                            ║
 * ║ 4. BEAN SCOPES - Phạm vi sống của Bean                                    ║
 * ║                                                                            ║
 * ╚════════════════════════════��═══════════════════════════════════════════════╝
 * 
 * A. SINGLETON (Mặc định, tối ưu)
 * ─────────────────────────────────
 *    • Container tạo 1 instance DUY NHẤT cho toàn bộ ứng dụng
 *    • Tất cả request dùng chung 1 object
 * 
 *    Ưu điểm:
 *    ✓ Tiết kiệm memory (chỉ 1 object)
 *    ✓ Nhanh (không tạo object mới)
 *    ✓ Stateless service TỐI ƯU
 * 
 *    Nhược điểm:
 *    ✗ Không thread-safe nếu có state (mutable field)
 *    ✗ State chia sẻ giữa các request
 * 
 *    Khi dùng: Service, Repository, Utility classes
 *    ⚠ QUAN TRỌNG: Giữ Bean stateless (không có field thay đổi)
 * 
 * B. PROTOTYPE
 * ─────────────────────────────────
 *    • Container tạo instance MỚI mỗi khi request
 * 
 *    Ưu điểm:
 *    ✓ Thread-safe (mỗi request có object riêng)
 *    ✓ State KHÔNG chia sẻ
 *    ✓ Độc lập hoàn toàn
 * 
 *    Nhược điểm:
 *    ✗ Tốn memory (tạo nhiều object)
 *    ✗ Chậm hơn Singleton (phải khởi tạo nhiều lần)
 *    ✗ @PreDestroy không gọi (container không quản lý cleanup)
 * 
 *    Khi dùng: Stateful object, Command objects, mutable bean
 * 
 * C. REQUEST (Chỉ Web Applications)
 * ─────────────────────────────────
 *    • Tạo 1 instance cho mỗi HTTP request
 *    • Tự động destroy khi request kết thúc
 * 
 *    Khi dùng: Web Controllers, Request-specific data
 * 
 * D. SESSION (Chỉ Web Applications)
 * ─────────────────────────────────
 *    • Tạo 1 instance cho mỗi HTTP session
 *    • Chia sẻ dữ liệu trong 1 user session
 * 
 *    Khi dùng: User cart, User preferences, Session-scoped data
 * 
 * ════════════════════════════════════════════════════════════════════════════
 * 
 * BẢNG SO SÁNH TÓMED:
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * INJECTION TYPE     | ƯUPOINT              | NHƯỢC ĐIỂM              | KHI DÙNG
 * ──────────��─────────────────────────────────────────────────────────────────
 * Constructor        | Bắt buộc, immutable  | Nhiều parameter        | Chính
 * Setter             | Linh hoạt, optional  | Có thể incomplete      | Optional
 * Field              | Gọn, ít code         | Khó test, hidden dep   | TRÁNH
 * 
 * SCOPE              | MEMORY               | THREAD-SAFE            | KHI DÙNG
 * ────────────────────────────────────────────────────────────────────────────
 * Singleton          | Tiết kiệm (1 obj)    | Không an toàn          | Mặc định
 * Prototype          | Tốn (nhiều obj)      | An toàn (riêng)         | Stateful
 * Request            | Tiết kiệm (1/req)    | An toàn (riêng)         | Web
 * Session            | Tiết kiệm (1/session)| An toàn (user)          | Web
 * 
 * ════════════════════════════════════════════════════════════════════════════
 * 
 * 💡 KHUYẾN NGHỊ BEST PRACTICE:
 * ─────────────────────────────────────────────────────────────────────────────
 * 
 * 1️⃣ INJECTION METHOD:
 *    ��� Ưu tiên: CONSTRUCTOR INJECTION (99% trường hợp)
 *    ○ Dùng: SETTER INJECTION khi dependency OPTIONAL
 *    ❌ Tránh: FIELD INJECTION (@Autowired trên field)
 * 
 * 2️⃣ BEAN SCOPE:
 *    ✅ Mặc định: SINGLETON (cho Service, Repository)
 *       → Stateless, tối ưu memory, nhanh
 *    ✅ Khi cần: PROTOTYPE (cho stateful object)
 *       → Mỗi request có object riêng, thread-safe
 *    ✅ Web app: REQUEST scope (cho controller)
 *    ✅ User data: SESSION scope (cho cart, preferences)
 * 
 * 3️⃣ SINGLETON RULES:
 *    ⚠ KHÔNG có mutable state (field thay đổi)
 *    ⚠ Thread-safe nếu có sharing data
 *    ⚠ Dependency inject qua Constructor
 * 
 * 4️⃣ TESTING:
 *    ✅ Constructor Injection → dễ test (mock constructor param)
 *    ⚠ Setter Injection → phải gọi setter trong test
 *    ❌ Field Injection → khó test (cần reflection)
 * 
 * ════════════════════════════════════════════════════════════════════════════
 */

public class BeanExplanationGuide {
    
    public static void main(String[] args) {
        System.out.println("""
            ════════════════════════════════════════════════════════════════════════════
                           DEPENDENCY INJECTION LEARNING GUIDE
            ════════════════════════════════════════════════════════════════════════════
            
            Chạy các demo này để hiểu rõ:
            
            1. ConstructorInjectionDemo.java
               → Cách inject qua Constructor (KHUYẾN NGHỊ)
               → Tại sao Singleton pattern cần bắt buộc dependency
            
            2. SetterInjectionDemo.java
               → Cách inject qua Setter (Optional)
               → Lợi ích & hạn chế so với Constructor
            
            3. FieldInjectionDemo.java
               → Cách inject qua Field (@Autowired)
               → Tại sao KHÔNG nên dùng
            
            4. InjectionComparisonDemo.java
               → So sánh trực tiếp cả 3 loại
               → Ví dụ Singleton vs Prototype scope
            
            5. IoCDemo.java
               → Demo đơn giản nhất về Bean & Container
               → Bắt đầu từ đây nếu mới!
            
            ════════════════════════════════════════════════════════════════════════════
            """);
    }
}

