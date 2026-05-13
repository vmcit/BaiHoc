package com.example.springboot.iocdemo;

/**
 * DEMO: Setter Injection - Tính bất biến (Mutability Problem)
 *
 * Chứng minh 3 vấn đề nguy hiểm khi dùng Setter Injection:
 *   1. Đối tượng không hoàn chỉnh (Incomplete object)
 *   2. Dependency bị thay thế lén lút sau khi tạo (Unexpected replacement)
 *   3. Thread-safety: đa luồng thay setter đồng thời gây ra lỗi
 */
public class SetterInjectionMutabilityDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("════════════════════════════════════════════════════════════════════════════");
        System.out.println("    DEMO: Setter Injection - Tính bất biến & Trường hợp tác động xấu");
        System.out.println("════════════════════════════════════════════════════════════════════════════\n");

        demoIncompleteObject();
        demoUnexpectedReplacement();
        demoThreadSafetyProblem();
        demoConstructorInjectionComparison();
        printConclusion();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // VẤN ĐỀ 1: INCOMPLETE OBJECT - Dùng trước khi inject xong
    // ═══════════════════════════════════════════════════════════════════════
    static void demoIncompleteObject() {
        System.out.println("❌ VẤN ĐỀ 1: Incomplete Object (Đối tượng chưa hoàn chỉnh)\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            Setter Injection KHÔNG đảm bảo dependency được inject trước khi dùng.
            Ai đó có thể gọi method trước khi gọi setter → NullPointerException!
            """);

        // Tạo OrderProcessor nhưng QUÊN inject emailService
        SetterOrderProcessor processor = new SetterOrderProcessor();
        // processor.setEmailService(new RealEmailService()); ← bị quên!

        System.out.println("  [CODE]");
        System.out.println("  SetterOrderProcessor processor = new SetterOrderProcessor();");
        System.out.println("  // Quên gọi: processor.setEmailService(new RealEmailService());");
        System.out.println("  processor.processOrder(\"ORD-001\");  ← gọi khi chưa inject\n");

        System.out.println("  [KẾT QUẢ]");
        try {
            processor.processOrder("ORD-001");
        } catch (NullPointerException e) {
            System.out.println("  ❌ NullPointerException: emailService chưa được inject!");
            System.out.println("     Lỗi tại: " + e.getStackTrace()[0]);
        }
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // VẤN ĐỀ 2: UNEXPECTED REPLACEMENT - Thay dependency lén lút
    // ═══════════════════════════════════════════════════════════════════════
    static void demoUnexpectedReplacement() {
        System.out.println("❌ VẤN ĐỀ 2: Unexpected Replacement (Thay dependency sau khi tạo)\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            Setter Injection cho phép THAY ĐỔI dependency bất cứ lúc nào.
            Code khác có thể silently swap dependency → hành vi thay đổi bất ngờ!
            """);

        RealEmailService realEmail   = new RealEmailService("smtp.company.com");
        FakeEmailService fakeEmail   = new FakeEmailService();
        SetterOrderProcessor processor = new SetterOrderProcessor();

        // Ban đầu inject đúng
        processor.setEmailService(realEmail);

        System.out.println("  Bước 1: Inject RealEmailService → xử lý ORD-001");
        processor.processOrder("ORD-001");

        // Một module khác trong hệ thống silently thay setter
        System.out.println("\n  Bước 2: Module khác gọi setEmailService(fakeEmail) lén lút...");
        processor.setEmailService(fakeEmail); // ← đây là vấn đề!

        System.out.println("\n  Bước 3: Vẫn tưởng dùng RealEmailService nhưng thực ra là Fake!");
        processor.processOrder("ORD-002");

        System.out.println("\n  ❌ KẾT QUẢ: Email của ORD-002 bị nuốt mất! Không ai biết!");
        System.out.println("     → Với Constructor Injection (final), bước 2 sẽ bị COMPILE ERROR\n");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // VẤN ĐỀ 3: THREAD SAFETY - Đa luồng thay setter đồng thời
    // ═══════════════════════════════════════════════════════════════════════
    static void demoThreadSafetyProblem() throws InterruptedException {
        System.out.println("❌ VẤN ĐỀ 3: Thread Safety (Đa luồng tranh nhau thay setter)\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            Khi nhiều thread cùng gọi setter và method đồng thời:
            - Thread A inject emailServiceA → chưa kịp xử lý
            - Thread B inject emailServiceB → ghi đè
            - Thread A xử lý nhưng dùng emailServiceB của Thread B!
            """);

        SetterOrderProcessor sharedProcessor = new SetterOrderProcessor();

        // Thread A: inject SmtpEmailService rồi xử lý 3 đơn
        Thread threadA = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                sharedProcessor.setEmailService(new RealEmailService("smtp-A.server.com"));
                // Context switch có thể xảy ra ở đây!
                sharedProcessor.processOrderWithThread("A-ORD-00" + i, "Thread-A");
                try { Thread.sleep(5); } catch (InterruptedException ignored) {}
            }
        });

        // Thread B: inject khác rồi xử lý 3 đơn
        Thread threadB = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                sharedProcessor.setEmailService(new FakeEmailService());
                // Context switch có thể xảy ra ở đây!
                sharedProcessor.processOrderWithThread("B-ORD-00" + i, "Thread-B");
                try { Thread.sleep(5); } catch (InterruptedException ignored) {}
            }
        });

        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();

        System.out.println("\n  ❌ Quan sát: Thread-A có thể đã dùng FakeEmailService của Thread-B!");
        System.out.println("     → Race condition: không thể đoán được email nào được gửi thật\n");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // SO SÁNH: Constructor Injection giải quyết tất cả vấn đề trên
    // ═══════════════════════════════════════════════════════════════════════
    static void demoConstructorInjectionComparison() {
        System.out.println("✅ SO SÁNH: Constructor Injection giải quyết tất cả vấn đề trên\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            final field + Constructor Injection đảm bảo:
            
            class OrderProcessor {
                private final EmailService emailService;  // ← final = bất biến
            
                public OrderProcessor(EmailService emailService) {
                    // ① Bắt buộc inject khi tạo → không thể "quên"
                    // ② Không thể gán null (nếu check Objects.requireNonNull)
                    this.emailService = Objects.requireNonNull(emailService);
                }
                // ③ Không có setter → không ai thay được sau khi tạo
                // ④ Thread-safe: final field được publish an toàn trong JMM
            }
            """);

        System.out.println("  [DEMO] Constructor Injection - không thể bị vấn đề trên:");

        // ① Bắt buộc inject khi tạo
        System.out.println("\n  ① Không thể tạo nếu không inject:");
        System.out.println("     new ImmutableOrderProcessor(null) → exception ngay lập tức");
        try {
            new ImmutableOrderProcessor(null);
        } catch (NullPointerException e) {
            System.out.println("     ❌ NullPointerException tại constructor → phát hiện sớm!");
        }

        // ② Không thể thay dependency
        System.out.println("\n  ② Sau khi tạo, dependency không thể thay:");
        ImmutableOrderProcessor proc = new ImmutableOrderProcessor(new RealEmailService("smtp.company.com"));
        proc.processOrder("ORD-SAFE-001");
        System.out.println("     → Không có setter → không ai thay được emailService");

        // ③ Thread-safe
        System.out.println("\n  ③ Thread-safe: final field an toàn trong đa luồng\n");
    }

    // ═══════════════════════════════════════════════════════════════════════
    static void printConclusion() {
        System.out.println("════════════════════════════════════════════════════════════════════════════");
        System.out.println("💡 KẾT LUẬN: Setter Injection vs Constructor Injection");
        System.out.println("════════════════════════════════════════════════════════════════════════════");
        System.out.println("""
            
            ┌─────────────────────────┬──────────────────────┬──────────────────────┐
            │ Tiêu chí                │ Setter Injection     │ Constructor Injection│
            ├─────────────────────────┼──────────────────────┼──────────────────────┤
            │ Incomplete object       │ ❌ Có thể xảy ra     │ ✅ Không thể         │
            │ Dependency bị thay thế  │ ❌ Ai cũng thay được │ ✅ final - không đổi │
            │ Thread safety           │ ❌ Race condition     │ ✅ JMM đảm bảo       │
            │ Nullable dependency     │ ❌ Có thể null        │ ✅ Check tại ctor    │
            │ Circular dependency     │ ✅ Giải quyết được    │ ❌ Khó               │
            │ Immutability            │ ❌ Mutable            │ ✅ Immutable         │
            └─────────────────────────┴──────────────────────┴──────────────────────┘
            
            📌 NGUYÊN TẮC:
               ✓ Dùng Constructor Injection làm mặc định
               ✓ Chỉ dùng Setter Injection khi giải quyết Circular Dependency
               ✓ Nếu dùng Setter Injection, document rõ ràng để tránh lạm dụng
            
            """);
    }
}

// ════════════════════════════════════════════════════════════════════════════
// CÁC CLASS HỖ TRỢ DEMO
// ════════════════════════════════════════════════════════════════════════════

interface EmailServiceDemo {
    void sendEmail(String to, String subject);
    String getName();
}

class RealEmailService implements EmailServiceDemo {
    private final String smtpHost;

    RealEmailService(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    @Override
    public void sendEmail(String to, String subject) {
        System.out.printf("  📧 [REAL - %s] Gửi email đến %s: \"%s\"%n", smtpHost, to, subject);
    }

    @Override
    public String getName() { return "RealEmailService(" + smtpHost + ")"; }
}

class FakeEmailService implements EmailServiceDemo {
    @Override
    public void sendEmail(String to, String subject) {
        System.out.printf("  🗑️  [FAKE] Email bị nuốt (không gửi): \"%s\"%n", subject);
    }

    @Override
    public String getName() { return "FakeEmailService (nuốt email)"; }
}

// ════════════════════════════════════════════════════════════════════════════
// SETTER INJECTION - Mutable (có vấn đề)
// ════════════════════════════════════════════════════════════════════════════
class SetterOrderProcessor {
    private EmailServiceDemo emailService; // ← KHÔNG final, có thể null, có thể bị thay

    public void setEmailService(EmailServiceDemo emailService) {
        this.emailService = emailService;
    }

    public void processOrder(String orderId) {
        System.out.printf("  → Xử lý đơn hàng %s...%n", orderId);
        // NullPointerException nếu chưa inject!
        emailService.sendEmail("customer@example.com", "Xác nhận đơn " + orderId);
    }

    public void processOrderWithThread(String orderId, String threadName) {
        // Capture ngay lúc này (có thể đã bị thread khác thay rồi)
        EmailServiceDemo current = this.emailService;
        String serviceName = (current != null) ? current.getName() : "NULL";
        System.out.printf("  [%s] Xử lý %s dùng: %s%n", threadName, orderId, serviceName);
        if (current != null) {
            current.sendEmail("customer@example.com", "Xác nhận " + orderId);
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
// CONSTRUCTOR INJECTION - Immutable (giải quyết tất cả vấn đề)
// ════════════════════════════════════════════════════════════════════════════
class ImmutableOrderProcessor {
    private final EmailServiceDemo emailService; // ← final = không thể thay

    ImmutableOrderProcessor(EmailServiceDemo emailService) {
        // Phát hiện lỗi ngay tại constructor
        if (emailService == null) throw new NullPointerException("emailService không được null!");
        this.emailService = emailService;
    }

    // Không có setter → không ai thay được dependency

    public void processOrder(String orderId) {
        System.out.printf("  → Xử lý đơn hàng %s...%n", orderId);
        emailService.sendEmail("customer@example.com", "Xác nhận đơn " + orderId);
    }
}
