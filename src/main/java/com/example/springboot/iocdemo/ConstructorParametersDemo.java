package com.example.springboot.iocdemo;

/**
 * DEMO: Tại sao "Constructor có nhiều parameter" là HẠN CHẾ?
 * 
 * 演 PROBLEM: Constructor Hell (địa ngục tham số)
 */
public class ConstructorParametersDemo {
    
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════════════════════════");
        System.out.println("     DEMO: Tại sao \"Constructor có nhiều parameter\" lại là HẠN CHẾ?");
        System.out.println("════════════════════════════════════════════════════════════════════════════\n");
        
        // ═══════════════════════ PHẦN 1: CONSTRUCTOR BÌNH THƯỜNG (OK) ═══════════════════════
        System.out.println("✅ PHẦN 1: Constructor với ít tham số (BÌNH THƯỜNG)\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        
        // Đây là cách bình thường - 2-3 tham số
        UserRepository userRepo = new UserRepository();
        NotificationService notifyService = new NotificationService();
        
        SimpleUserService simpleService = new SimpleUserService(userRepo, notifyService);
        System.out.println("✓ SimpleUserService được tạo với 2 tham số");
        System.out.println("✓ Dễ đọc, dễ hiểu\n");
        
        // ═══════════════════════ PHẦN 2: CONSTRUCTOR PHỨC TẠP (HẠNCHẾ) ═══════════════════════
        System.out.println("❌ PHẦN 2: Constructor với NHIỀU tham số (HẠN CHẾ)\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        
        // Bây giờ UserService cần nhiều dependency hơn
        EmailService emailService = new EmailService();
        NotificationService notificationService = new NotificationService();
        LoggingService loggingService = new LoggingService();
        CacheService cacheService = new CacheService();
        ConfigService configService = new ConfigService();
        ValidationService validationService = new ValidationService();
        
        System.out.println("❌ HẠNCHẾ 1: Constructor dài và khó đọc:");
        System.out.println("""
            new ComplexUserService(
                userRepository,           // ← Tham số 1
                emailService,             // ← Tham số 2
                notificationService,      // ← Tham số 3
                loggingService,           // ← Tham số 4
                cacheService,             // ← Tham số 5
                configService,            // ← Tham số 6
                validationService         // ← Tham số 7
            );
            
            ❌ Khó theo dõi tham số nào là gì
            ❌ Dễ truyền sai thứ tự
            ❌ Khó bảo trì
            """);
        
        ComplexUserService complexService = new ComplexUserService(
            userRepo,
            emailService,
            notificationService,
            loggingService,
            cacheService,
            configService,
            validationService
        );
        System.out.println("→ ComplexUserService được tạo (nhưng khó đọc!)\n");
        
        // ═══════════════════════ PHẦN 3: HẠN CHẾ 2 - KHÓTEST ═══════════════════════
        System.out.println("❌ HẠNCHẾ 2: Khó viết Unit Test\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            Khi viết test, phải mock TẤT CẢ 7 tham số:
            
            @Test
            public void testCreateUser() {
                ComplexUserService service = new ComplexUserService(
                    mock(UserRepository.class),      // ← Mock 1
                    mock(EmailService.class),        // ← Mock 2
                    mock(NotificationService.class), // ← Mock 3
                    mock(LoggingService.class),      // ← Mock 4
                    mock(CacheService.class),        // ← Mock 5
                    mock(ConfigService.class),       // ← Mock 6
                    mock(ValidationService.class)    // ← Mock 7
                );
                
                // ...test code...
            }
            
            ❌ Rất phiền toàn
            ❌ Dễ nhầm lẫn thứ tự mock
            ❌ Setup test lâu
            """);
        
        // ═══════════════════════ PHẦN 4: HẠN CHẾ 3 - REFACTOR KHÓ ═══════════════════════
        System.out.println("❌ HẠNCHẾ 3: Khó Refactor (Thêm dependency mới)\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            Nếu thêm dependency mới (ví dụ SecurityService):
            
            1. Phải sửa constructor:
               public ComplexUserService(
                   UserRepository userRepo,
                   EmailService emailService,
                   ...
                   SecurityService securityService  // ← Thêm mới
               ) { ... }
            
            2. Phải sửa TẤT CẢ nơi tạo ComplexUserService:
               ComplexUserService service1 = new ComplexUserService(..., securityService);
               ComplexUserService service2 = new ComplexUserService(..., securityService);
               ComplexUserService service3 = new ComplexUserService(..., securityService);
               // ... + 50 chỗ khác
            
            3. Phải sửa TẤT CẢ unit test:
               mock(SecurityService.class), // ← Thêm mock này ở 100+ test
            
            ❌ Tốn thời gian
            ❌ Dễ quên 1 chỗ gây lỗi
            ❌ Refactor nguy hiểm
            """);
        
        // ═══════════════════════ PHẦN 5: GIẢI PHÁP 1 - BUILDER PATTERN ═══════════════════════
        System.out.println("✅ GIẢI PHÁP 1: Builder Pattern\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            Thay vì constructor dài:
            
            ✓ Dùng Builder:
            """);
        
        ComplexUserService serviceWithBuilder = new ComplexUserService.Builder()
            .withUserRepository(userRepo)
            .withEmailService(emailService)
            .withNotificationService(notificationService)
            .withLoggingService(loggingService)
            .withCacheService(cacheService)
            .withConfigService(configService)
            .withValidationService(validationService)
            .build();
        
        System.out.println("""
            
            ✓ Ưu điểm Builder:
               ✓ Dễ đọc - rõ ràng tham số nào
               ✓ Không lo thứ tự tham số
               ✓ Dễ thêm tham số mới
               ✓ Optional parameter dễ xử lý
            """);
        
        // ═══════════════════════ PHẦN 6: GIẢI PHÁP 2 - GROUPING DEPENDENCIES ═══════════════════════
        System.out.println("✅ GIẢI PHÁP 2: Nhóm dependency thành class nhỏ\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            Thay vì:
               public ComplexUserService(
                   UserRepository repo,
                   EmailService email,
                   NotificationService notification,
                   LoggingService logging,
                   CacheService cache,
                   ConfigService config,
                   ValidationService validation
               ) { }
            
            ✓ Nhóm thành class:
            """);
        
        // Tạo group services
        NotificationServices notificationServices = new NotificationServices(
            emailService,
            notificationService,
            loggingService
        );
        
        CachingAndConfigServices cachingServices = new CachingAndConfigServices(
            cacheService,
            configService
        );
        
        // Constructor gọn hơn:
        SimplifiedComplexUserService simplifiedService = 
            new SimplifiedComplexUserService(
                userRepo,
                notificationServices,    // ← Grouped
                cachingServices,         // ← Grouped
                validationService
            );
        
        System.out.println("""
               public SimplifiedComplexUserService(
                   UserRepository repo,
                   NotificationServices notificationServices,  // ← Grouped
                   CachingAndConfigServices cachingServices,   // ← Grouped
                   ValidationService validation
               ) { }
            
            ✓ Ưu điểm Grouping:
               ✓ Constructor gọn hơn (4 tham số thay vì 7)
               ✓ Dễ quản lý dependency liên quan
               ✓ Dễ test (mock group thay vì mock từng cái)
               ✓ Dễ refactor
            """);
        
        // ═══════════════════════ PHẦN 7: GIẢI PHÁP 3 - TÁCH CLASS ═══════════════════════
        System.out.println("✅ GIẢI PHÁP 3: Tách thành class nhỏ hơn\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            Nếu ComplexUserService quá phức tạp:
            
            ❌ 1 class + 7 dependency:
               ComplexUserService(repo, email, notify, log, cache, config, validation)
            
            ✓ Tách thành 3 class + ít dependency hơn:
               class UserDataService(repo, cache, validation) { }
               class UserNotificationService(email, notification, logging) { }
               class UserOrchestrationService(dataService, notificationService) { }
            
            ✓ Ưu điểm Tách class:
               ✓ Mỗi class có trách nhiệm rõ ràng (Single Responsibility)
               ✓ Constructor mỗi class đơn giản
               ✓ Dễ test, dễ tái sử dụng
               ✓ Dễ mở rộng, bảo trì
            """);
        
        // ═══════════════════════ KẾT LUẬN ═══════════════════════
        System.out.println("════════════════════════════════════════════════════════════════════════════");
        System.out.println("💡 KẾT LUẬN:");
        System.out.println("════════════════════════════════════════════════════════════════════════════");
        System.out.println("""
            
            "Constructor có nhiều parameter" là HẠN CHẾ vì:
            
            ❌ HẠNCHẾ:
               1. Constructor DÀI, khó đọc
               2. Dễ truyền sai thứ tự
               3. Viết test PHIỀN (phải mock nhiều)
               4. Refactor NGUY HIỂM (thêm parameter → sửa mọi nơi)
            
            ✅ GIẢI PHÁP:
               1. Builder Pattern (nếu constructor phức tạp)
               2. Grouping Dependencies (nhóm thành class nhỏ)
               3. Tách Class (nếu 1 class làm quá nhiều)
            
            📌 NGUYÊN TẮC:
               ✓ Constructor nên có < 5 tham số
               ✓ Nếu > 5 → Dùng Builder hoặc tách class
               ✓ Constructor > 7 → ĐÓ LÀ TẤN HIỆU (refactor!)
            
            """);
    }
}

// ════════════════════════════════════════════════════════════════════════════
// SERVICE CLASSES
// ════════════════════════════════════════════════════════════════════════════

/**
 * ✅ Simple Service - Constructor bình thường (2 tham số)
 */
class SimpleUserService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    
    public SimpleUserService(UserRepository userRepository, 
                            NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }
}

/**
 * ❌ Complex Service - Constructor PHỨC TẠP (7 tham số)
 * Đây là ví dụ "Constructor Hell"
 */
class ComplexUserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final LoggingService loggingService;
    private final CacheService cacheService;
    private final ConfigService configService;
    private final ValidationService validationService;
    
    // ❌ Constructor dài (Constructor Hell)
    public ComplexUserService(
        UserRepository userRepository,
        EmailService emailService,
        NotificationService notificationService,
        LoggingService loggingService,
        CacheService cacheService,
        ConfigService configService,
        ValidationService validationService
    ) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.notificationService = notificationService;
        this.loggingService = loggingService;
        this.cacheService = cacheService;
        this.configService = configService;
        this.validationService = validationService;
    }
    
    // ✅ GIẢI PHÁP 1: Builder Pattern
    public static class Builder {
        private UserRepository userRepository;
        private EmailService emailService;
        private NotificationService notificationService;
        private LoggingService loggingService;
        private CacheService cacheService;
        private ConfigService configService;
        private ValidationService validationService;
        
        public Builder withUserRepository(UserRepository repo) {
            this.userRepository = repo;
            return this;
        }
        
        public Builder withEmailService(EmailService email) {
            this.emailService = email;
            return this;
        }
        
        public Builder withNotificationService(NotificationService notification) {
            this.notificationService = notification;
            return this;
        }
        
        public Builder withLoggingService(LoggingService logging) {
            this.loggingService = logging;
            return this;
        }
        
        public Builder withCacheService(CacheService cache) {
            this.cacheService = cache;
            return this;
        }
        
        public Builder withConfigService(ConfigService config) {
            this.configService = config;
            return this;
        }
        
        public Builder withValidationService(ValidationService validation) {
            this.validationService = validation;
            return this;
        }
        
        public ComplexUserService build() {
            return new ComplexUserService(
                userRepository,
                emailService,
                notificationService,
                loggingService,
                cacheService,
                configService,
                validationService
            );
        }
    }
}

/**
 * ✅ GIẢI PHÁP 2: Nhóm dependency
 */
class NotificationServices {
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final LoggingService loggingService;
    
    public NotificationServices(EmailService emailService,
                               NotificationService notificationService,
                               LoggingService loggingService) {
        this.emailService = emailService;
        this.notificationService = notificationService;
        this.loggingService = loggingService;
    }
}

class CachingAndConfigServices {
    private final CacheService cacheService;
    private final ConfigService configService;
    
    public CachingAndConfigServices(CacheService cacheService,
                                   ConfigService configService) {
        this.cacheService = cacheService;
        this.configService = configService;
    }
}

/**
 * ✅ GIẢI PHÁP 2: Simplified Complex Service (Constructor gọn hơn)
 */
class SimplifiedComplexUserService {
    private final UserRepository userRepository;
    private final NotificationServices notificationServices;
    private final CachingAndConfigServices cachingServices;
    private final ValidationService validationService;
    
    public SimplifiedComplexUserService(
        UserRepository userRepository,
        NotificationServices notificationServices,
        CachingAndConfigServices cachingServices,
        ValidationService validationService
    ) {
        this.userRepository = userRepository;
        this.notificationServices = notificationServices;
        this.cachingServices = cachingServices;
        this.validationService = validationService;
    }
}

// ════════════════════════════════════════════════════════════════════════════
// DUMMY SERVICE CLASSES (for demo)
// ════════════════════════════════════════════════════════════════════════════

class UserRepository {}
class EmailService {}
class NotificationService {}
class LoggingService {}
class CacheService {}
class ConfigService {}
class ValidationService {}

