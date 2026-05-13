package com.example.springboot.iocdemo;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *           CONSTRUCTOR INJECTION - GIẢI THÍCH CHI TIẾT
 * ════════════════════════════════════════════════════════════════════════════
 * 
 * CÂUHỎI 1: Tại sao Constructor Injection gọi là IMMUTABLE?
 * ────────────────────────────────────────────────────────────────────────────
 * 
 * Immutable = Không thay đổi được
 * 
 * Ví dụ:
 * 
 *   public class UserRepository {
 *       private final Database database;  // ← final: không thể thay đổi
 *       
 *       public UserRepository(Database database) {
 *           this.database = database;  // ← Gán DUY NHẤT 1 lần trong constructor
 *       }
 *   }
 * 
 * Khi bạn tạo object:
 *   UserRepository repo = new UserRepository(new MySQLDatabase());
 *   
 * Từ lúc này, database KHÔNG THỂ thay đổi nữa:
 *   repo.database = new PostgresDatabase();  // ❌ LỖI! không thể gán lại
 * 
 * ═══════════════════════════════════════════════════════════════════════════
 * 
 * Tại sao IMMUTABLE lại LÀ ĐIỀU TỐT?
 * ─────────────────────────────────────────────────────────────────────────
 * 
 * 1️⃣ THREAD-SAFE (An toàn trong môi trường đa luồng)
 * ─────────────────
 * 
 * Giả sử bạn có 2 request từ 2 người dùng khác nhau:
 * 
 * ❌ Nếu MUTABLE (dùng Setter Injection):
 * 
 *    public class UserService {
 *        private Database database;  // ← Có thể thay đổi!
 *        
 *        public void setDatabase(Database db) {
 *            this.database = db;
 *        }
 *    }
 * 
 *    Request 1:                    Request 2:
 *    service.database = MySQL      service.database = MySQL
 *    [Đang xử lý...]               Thay đổi tại đây:
 *                                  service.database = Postgres  ← Lỗi!
 *                                  
 *    → Database bị thay đổi giữa chừng → DATA BỊ SAI
 * 
 * ✅ Nếu IMMUTABLE (dùng Constructor Injection):
 * 
 *    public class UserService {
 *        private final Database database;  // ← Không thể thay đổi!
 *        
 *        public UserService(Database db) {
 *            this.database = db;
 *        }
 *        // Không có setter!
 *    }
 * 
 *    Request 1:                    Request 2:
 *    UserService service1          UserService service2
 *    database = MySQL              database = MySQL
 *    (không thể thay đổi)          (không thể thay đổi)
 *    
 *    → Mỗi request có database riêng → AN TOÀN
 * 
 * 2️⃣ DỄ DỰ ĐOÁN HÀNH VI (Predictable)
 * ────────────────────
 * 
 * Khi bạn nhìn thấy:
 * 
 *    UserService service = new UserService(database);
 *    // Sau dòng này, 100% chắc service.database = database
 *    // Không ai có thể thay đổi nó nữa!
 * 
 * 3️⃣ DỄ DEBUG
 * ────────────
 * 
 * Vì dependency KHÔNG THAY ĐỔI, khi có lỗi bạn không cần lo:
 * "Ai đó có thay đổi database ở đâu đó không?"
 * 
 * ════════════════════════════════════════════════════════════════════════════
 * 
 * CÂUHỎI 2: Tại sao HẠN CHẾ "Constructor có nhiều parameter"?
 * ────────────────────────────────────────────────────────────────────────────
 * 
 * Ví dụ, nếu bạn có một UserService phức tạp:
 * 
 *   public class UserService {
 *       private final UserRepository userRepo;
 *       private final EmailService emailService;
 *       private final NotificationService notificationService;
 *       private final LoggingService loggingService;
 *       private final CacheService cacheService;
 *       private final ConfigService configService;
 *       private final ValidationService validationService;
 *       
 *       public UserService(
 *           UserRepository userRepo,
 *           EmailService emailService,
 *           NotificationService notificationService,
 *           LoggingService loggingService,
 *           CacheService cacheService,
 *           ConfigService configService,
 *           ValidationService validationService
 *       ) {
 *           this.userRepo = userRepo;
 *           this.emailService = emailService;
 *           this.notificationService = notificationService;
 *           this.loggingService = loggingService;
 *           this.cacheService = cacheService;
 *           this.configService = configService;
 *           this.validationService = validationService;
 *       }
 *   }
 * 
 * ❌ VẤN ĐỀ:
 * 
 * 1. Constructor quá DÀI, khó đọc:
 *    UserService service = new UserService(
 *        userRepo,
 *        emailService,
 *        notificationService,
 *        loggingService,
 *        cacheService,
 *        configService,
 *        validationService
 *    );
 *    
 *    → Ai biết parameter nào là gì?
 *    → Dễ truyền sai thứ tự
 * 
 * 2. TESTING KHÓ HƠNРن:
 *    Khi viết unit test, phải mock tất cả 7 dependency:
 *    
 *    @Test
 *    public void testCreateUser() {
 *        UserService service = new UserService(
 *            mock(UserRepository.class),
 *            mock(EmailService.class),
 *            mock(NotificationService.class),
 *            mock(LoggingService.class),
 *            mock(CacheService.class),
 *            mock(ConfigService.class),
 *            mock(ValidationService.class)
 *        );
 *        // Setup test
 *    }
 *    
 *    → Rất phiền toiีและ dễ nhầm lẫn
 * 
 * 3. REFACTORING KHÓ:
 *    Nếu bạn muốn thêm dependency mới:
 *    
 *    - Phải sửa constructor
 *    - Phải sửa tất cả nơi tạo UserService
 *    - Phải sửa tất cả test
 *    
 * ════════════════════════════════════════════════════════════════════════════
 * 
 * GIẢI PHÁP cho "Constructor có nhiều parameter"
 * ────────────────────────────────────────────────────────────────────────────
 * 
 * ✅ CÁCH 1: Dùng BUILDER PATTERN
 * 
 *    UserService service = new UserService.Builder()
 *        .withUserRepository(userRepo)
 *        .withEmailService(emailService)
 *        .withNotificationService(notificationService)
 *        .build();
 *    
 *    → Rõ ràng hơn
 *    → Không cần nhớ thứ tự
 * 
 * ✅ CÁCH 2: Nhóm dependency thành nhỏ
 * 
 *    // Thay vì:
 *    public UserService(UserRepository userRepo, EmailService emailService,
 *                      NotificationService notification, ...) {}
 *    
 *    // Hãy tạo class chứa các liên quan:
 *    public class NotificationServices {
 *        private final EmailService emailService;
 *        private final NotificationService notificationService;
 *        private final LoggingService loggingService;
 *    }
 *    
 *    // Constructor gọn hơn:
 *    public UserService(UserRepository userRepo, 
 *                      NotificationServices notificationServices) {}
 * 
 * ✅ CÁCH 3: Dùng Spring @Configuration
 * 
 *    @Configuration
 *    public class AppConfig {
 *        @Bean
 *        public UserService userService(UserRepository userRepo,
 *                                       EmailService emailService,
 *                                       ...) {
 *            return new UserService(userRepo, emailService, ...);
 *        }
 *    }
 *    
 *    → Spring giúp tạo object
 *    → Constructor vẫn gọn
 * 
 * ════════════════════════════════════════════════════════════════════════════
 * 
 * KHAI LÀM RÕ: Immutable ≠ Khó sử dụng
 * ────────────────────────────────────────────────────────────────────────────
 * 
 * Immutable = An toàn (thread-safe)
 * "Constructor có nhiều parameter" = Khó đọc khi 1 object phụ thuộc nhiều thứ
 * 
 * SỰ THẬT:
 * - Nếu constructor có QUÁOOOO nhiều parameter
 *   → Đó là TẤN hiệu: Class của bạn đang làm quá nhiều việc
 *   → Nên REFACTOR (tách thành class nhỏ hơn)
 * 
 * - Thường:Service chỉ cần 3-5 dependency
 *   → Constructor hoàn toàn hợp lý
 * 
 * ════════════════════════════════════════════════════════════════════════════
 */

public class ConstructorInjectionExplained {
    
    public static void main(String[] args) {
        System.out.println("""
            ════════════════════════════════════════════════════════════════════════════
                        CONSTRUCTOR INJECTION - LỜI GIẢI THÍCH
            ════════════════════════════════════════════════════════════════════════════
            
            📌 IMMUTABLE (Không thay đổi):
               • Dependency được gán QUY NHẤT LẦN DUY NHẤT trong constructor
               • Sau đó KHÔNG THỂ thay đổi (dùng final)
               • Lợi ích: Thread-safe, an toàn, dễ debug
            
            📌 "Constructor có nhiều parameter":
               • Khi một class phụ thuộc NHIỀU dependency
               • Constructor sẽ DÀI
               • Nhưng đó là TẤN HIỆU: class đang làm quá nhiều
               • Giải pháp: tách thành class nhỏ hơn hoặc dùng Builder
            
            ════════════════════════════════════════════════════════════════════════════
            """);
    }
}

// ════════════════════════════════════════════════════════════════════════════
// DEMO THỰC TẾ
// ════════════════════════════════════════════════════════════════════════════

/**
 * ❌ MUTABLE (Setter Injection) - KHÔNG AN TOÀN
 */
class MutableUserServiceExample {
    private Database database;  // ← Có thể thay đổi!
    
    public void setDatabase(Database db) {
        this.database = db;
    }
    
    public void saveUser(String name) {
        if (database == null) {
            System.out.println("❌ LỖI: database = null (chưa inject!)");
            return;
        }
        database.save("user", name);
    }
}

/**
 * ✅ IMMUTABLE (Constructor Injection) - AN TOÀN
 */
class ImmutableUserServiceExample {
    private final Database database;  // ← final: không thể thay đổi!
    
    public ImmutableUserServiceExample(Database database) {
        if (database == null) {
            throw new IllegalArgumentException("database không được null!");
        }
        this.database = database;  // ← Gán LẦN DUY NHẤT
    }
    
    public void saveUser(String name) {
        // ✅ 100% chắc database không null và không thay đổi
        database.save("user", name);
    }
}

/**
 * Simulated Database interface
 */
interface Database {
    void save(String table, String data);
}

class MySQLDatabase implements Database {
    @Override
    public void save(String table, String data) {
        System.out.println("💾 Saving to MySQL: " + table + " = " + data);
    }
}

