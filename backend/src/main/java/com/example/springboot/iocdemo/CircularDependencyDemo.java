package com.example.springboot.iocdemo;

/**
 * DEMO: Circular Dependency (Phụ thuộc vòng tròn)
 * 
 * ⚠ PROBLEM: Class A cần Class B, Class B lại cần Class A
 * → Khó xử lý với Constructor Injection
 */
public class CircularDependencyDemo {
    
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════════════════════════════════════");
        System.out.println("              DEMO: Circular Dependency (Phụ thuộc vòng tròn)");
        System.out.println("════════════════════════════════════════════════════════════════════════════\n");
        
        // ═══════════════════════ PHẦN 1: CIRCULAR DEPENDENCY ÉTA NÀO ═══════════════════════
        System.out.println("🔄 PHẦN 1: Circular Dependency là gì?\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            Giả sử có 2 class:
            
            class UserService {
                private final OrderService orderService;
                
                public UserService(OrderService orderService) {
                    this.orderService = orderService;
                }
            }
            
            class OrderService {
                private final UserService userService;
                
                public OrderService(UserService userService) {
                    this.userService = userService;
                }
            }
            
            Tạo object:
            UserService userService = new UserService(orderService);
                                                         ^^^^^^^^^^^^
                                                      Nhưng cần tạo OrderService
                                                      
            OrderService orderService = new OrderService(userService);
                                                         ^^^^^^^^^^^
                                                      Nhưng cần tạo UserService
            
            → VÒNG LẶP VÔ TẬN: A cần B, B cần A!
            """);
        
        // ═══════════════════════ PHẦN 2: ❌ CONSTRUCTOR INJECTION - KHÔNG GIẢI QUYẾT ═══════════════════════
        System.out.println("\n❌ PHẦN 2: Constructor Injection - KHÔNG thể giải quyết\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            ❌ CỐ GẮNG:
            
            UserService userService = new UserService(new OrderService(...));
                                                      ^^^^^^^^^^^^^^^^^^^^^^^^
                                          Nhưng OrderService cần UserService!
            
            ❌ RESULT: COMPILE ERROR hoặc RUNTIME ERROR
               - Stack Overflow
               - Bean creation exception (Spring)
               - Circular reference detected
            """);

        // Demo thực tế: Circular dependency gây StackOverflowError
        System.out.println("  [DEMO THỰC TẾ] Cố gắng tạo đối tượng với Constructor circular dependency:");
        try {
            // Đây sẽ gây StackOverflowError vì A() gọi new B(), B() gọi new A(), vô tận
            CircularA a = new CircularA();
            System.out.println("  (không bao giờ tới đây)");
        } catch (StackOverflowError e) {
            System.out.println("  ❌ StackOverflowError xảy ra! Stack trace (5 dòng đầu):");
            StackTraceElement[] trace = e.getStackTrace();
            for (int i = 0; i < Math.min(5, trace.length); i++) {
                System.out.println("     at " + trace[i]);
            }
            System.out.println("     ... (lặp lại vô tận)");
        }
        System.out.println();
        
        // ═══════════════════════ PHẦN 3: ✅ GIẢI PHÁP 1 - SETTER INJECTION ═══════════════════════
        System.out.println("\n✅ PHẦN 3: GIẢI PHÁP 1 - Setter Injection\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            ✓ Ý tưởng: Không inject qua constructor, mà inject sau khi tạo object
            
            class UserService {
                private OrderService orderService;  // ← Không final
                
                public UserService() {
                    // Constructor rỗng - không cần dependency
                }
                
                public void setOrderService(OrderService orderService) {
                    this.orderService = orderService;
                }
            }
            
            class OrderService {
                private UserService userService;  // ← Không final
                
                public OrderService() {
                    // Constructor rỗng - không cần dependency
                }
                
                public void setUserService(UserService userService) {
                    this.userService = userService;
                }
            }
            
            Tạo object:
            UserService userService = new UserService();      // ← OK (không cần gì)
            OrderService orderService = new OrderService();   // ← OK (không cần gì)
            
            // Sau đó, inject dependency:
            userService.setOrderService(orderService);
            orderService.setUserService(userService);
            
            ✓ Giải quyết được Circular Dependency!
            """);
        
        try {
            // Demo Setter Injection solution
            UserServiceWithSetter userService = new UserServiceWithSetter();
            OrderServiceWithSetter orderService = new OrderServiceWithSetter();
            
            // Inject dependency
            userService.setOrderService(orderService);
            orderService.setUserService(userService);
            
            System.out.println("✓ Tạo thành công với Setter Injection!");
            userService.printInfo();
            orderService.printInfo();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        
        // ═══════════════════════ PHẦN 4: ✅ GIẢI PHÁP 2 - LAZY LOADING ═══════════════════════
        System.out.println("\n✅ PHẦN 4: GIẢI PHÁP 2 - Lazy Loading\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            ✓ Ý tưởng: Không inject ngay, chỉ lấy khi cần
            
            class UserService {
                private OrderService orderService;
                private final OrderServiceProvider provider;
                
                public UserService(OrderServiceProvider provider) {
                    this.provider = provider;
                }
                
                public void doSomething() {
                    // Lấy orderService chỉ khi cần (lazy loading)
                    OrderService order = provider.getOrderService();
                }
            }
            
            interface OrderServiceProvider {
                OrderService getOrderService();
            }
            
            ✓ Ưu điểm:
               • Tránh circular dependency
               • Chỉ tạo object khi cần (tiết kiệm memory)
               • Vẫn giữ được immutability
            """);
        
        try {
            // Demo Lazy Loading solution
            OrderServiceProviderImpl provider = new OrderServiceProviderImpl();
            UserServiceWithLazyLoading userService2 = new UserServiceWithLazyLoading(provider);
            
            System.out.println("✓ Tạo thành công với Lazy Loading!");
            userService2.doSomething();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        
        // ═══════════════════════ PHẦN 5: ✅ GIẢI PHÁP 3 - INTERFACE SEGREGATION ═══════════════════════
        System.out.println("\n✅ PHẦN 5: GIẢI PHÁP 3 - Interface Segregation\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            ✓ Ý tưởng: Tách dependency thành interface nhỏ
            
            Thay vì:
            class UserService {
                private OrderService orderService;  // ← Phụ thuộc toàn bộ class
            }
            
            Hãy:
            interface OrderCreator {
                void createOrder(...);
            }
            
            class UserService {
                private OrderCreator orderCreator;  // ← Phụ thuộc interface nhỏ
            }
            
            class OrderService implements OrderCreator {
                private UserService userService;
            }
            
            ✓ Lợi ích:
               • Giảm coupling (phụ thuộc interface, không class)
               • Dễ test (mock interface nhỏ)
               • Dễ tách circular dependency
            """);
        
        try {
            // Demo Interface Segregation solution
            UserServiceWithInterface userService3 = new UserServiceWithInterface(
                new OrderServiceWithInterface()
            );
            
            System.out.println("✓ Tạo thành công với Interface Segregation!");
            userService3.processUser();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        
        // ═══════════════════════ PHẦN 6: ✅ GIẢI PHÁP 4 - SPRING @Lazy ═══════════════════════
        System.out.println("\n✅ PHẦN 6: GIẢI PHÁP 4 - Spring @Lazy Annotation\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            ✓ Trong Spring Boot, dùng @Lazy để trì hoãn khởi tạo bean:
            
            @Service
            public class UserService {
                private final OrderService orderService;
            
                public UserService(@Lazy OrderService orderService) {
                    this.orderService = orderService;
                }
            }
            
            @Service
            public class OrderService {
                private final UserService userService;
            
                public OrderService(UserService userService) {
                    this.userService = userService;
                }
            }
            
            ✓ @Lazy tạo một Proxy thay cho OrderService thực.
              Khi nào gọi method thì mới khởi tạo OrderService thật.
            
            ✓ Spring xử lý:
              1. Tạo UserService với proxy của OrderService  (không cần OrderService thật)
              2. Tạo OrderService (UserService đã tồn tại → inject được)
              3. Proxy được resolve khi method được gọi lần đầu
            
            ⚠ Lưu ý:
               • Chỉ dùng @Lazy khi thật sự cần (circular dep khó tránh)
               • Ưu tiên tái thiết kế để loại bỏ circular dep
            """);

        // Demo Proxy concept (Plain Java)
        try {
            LazyProxyDemo.demo();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        // ═══════════════════════ PHẦN 7: ✅ GIẢI PHÁP 5 - EXTRACT SHARED SERVICE ═══════════════════════
        System.out.println("\n✅ PHẦN 7: GIẢI PHÁP 5 - Extract Shared Service (Best Practice)\n");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("""
            
            ✓ Circular dependency thường do thiết kế sai.
              Hãy tìm logic dùng chung và tách ra class mới.
            
            TRƯỚC (circular):
            ┌────────────────┐       ┌────────────────┐
            │  UserService   │──────▶│  OrderService  │
            │                │◀──────│                │
            └────────────────┘       └────────────────┘
            
            SAU (tách shared service):
            ┌────────────────┐       ┌────────────────────┐
            │  UserService   │──────▶│  NotificationService│
            │                │       └────────────────────┘
            └────────────────┘               ▲
                                             │
            ┌────────────────┐               │
            │  OrderService  │───────────────┘
            └────────────────┘
            
            Ví dụ: UserService và OrderService đều cần gửi notification
            → Tách NotificationService độc lập
            """);

        try {
            // Demo Extract Shared Service
            SharedNotificationService notif = new SharedNotificationService();
            UserServiceExtracted userSvc = new UserServiceExtracted(notif);
            OrderServiceExtracted orderSvc = new OrderServiceExtracted(notif);

            System.out.println("  ✓ Cả hai service tạo thành công - không circular dep!");
            userSvc.registerUser("Alice");
            orderSvc.placeOrder("ORD-999", "Alice");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        // ═══════════════════════ KẾT LUẬN ═══════════════════════
        System.out.println("\n════════════════════════════════════════════════════════════════════════════");
        System.out.println("💡 KẾT LUẬN:");
        System.out.println("════════════════════════════════════════════════════════════════════════════");
        System.out.println("""
            
            🔄 Circular Dependency:
               • Class A cần Class B
               • Class B cần Class A
               • ❌ Constructor Injection → StackOverflowError!
            
            ✅ GIẢI PHÁP (theo mức độ ưu tiên):
            
               1. EXTRACT SHARED SERVICE ⭐⭐⭐ (tốt nhất):
                  • Tái thiết kế, tách logic chung ra class mới
                  • Loại bỏ hoàn toàn circular dep
               
               2. INTERFACE SEGREGATION ⭐⭐⭐:
                  • Phụ thuộc interface nhỏ thay vì full class
                  • Loose coupling, dễ test
               
               3. LAZY LOADING ⭐⭐ (Spring @Lazy):
                  • Trì hoãn khởi tạo qua proxy
                  • Dùng khi không thể tái thiết kế
               
               4. SETTER INJECTION ⭐ (khi không còn lựa chọn):
                  • Tạo object rỗng, inject sau
                  • Nhược điểm: Mutable, có thể incomplete
            
            📌 NGUYÊN TẮC VÀNG:
               ✓ Circular dependency = DẤU HIỆU thiết kế có vấn đề
               ✓ Ưu tiên tái thiết kế thay vì workaround
               ✓ Nếu bắt buộc: dùng @Lazy (Spring) hoặc Setter Injection
            
            """);
    }
}

// ════════════════════════════════════════════════════════════════════════════
// GIẢI PHÁP 1: SETTER INJECTION
// ════════════════════════════════════════════════════════════════════════════

class UserServiceWithSetter {
    private OrderServiceWithSetter orderService;
    
    public UserServiceWithSetter() {
        // Constructor rỗng - không cần dependency
        System.out.println("  ✓ UserServiceWithSetter created (no dependency needed)");
    }
    
    public void setOrderService(OrderServiceWithSetter orderService) {
        this.orderService = orderService;
    }
    
    public void printInfo() {
        System.out.println("  → UserService has OrderService: " + (orderService != null));
    }
}

class OrderServiceWithSetter {
    private UserServiceWithSetter userService;
    
    public OrderServiceWithSetter() {
        // Constructor rỗng - không cần dependency
        System.out.println("  ✓ OrderServiceWithSetter created (no dependency needed)");
    }
    
    public void setUserService(UserServiceWithSetter userService) {
        this.userService = userService;
    }
    
    public void printInfo() {
        System.out.println("  → OrderService has UserService: " + (userService != null));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// GIẢI PHÁP 2: LAZY LOADING
// ════════════════════════════════════════════════════════════════════════════

interface OrderServiceProvider {
    OrderServiceWithLazyLoading getOrderService();
}

class OrderServiceProviderImpl implements OrderServiceProvider {
    @Override
    public OrderServiceWithLazyLoading getOrderService() {
        System.out.println("  → [Lazy Loading] OrderService được lấy khi cần");
        return new OrderServiceWithLazyLoading();
    }
}

class UserServiceWithLazyLoading {
    private final OrderServiceProvider orderServiceProvider;
    
    public UserServiceWithLazyLoading(OrderServiceProvider provider) {
        this.orderServiceProvider = provider;
        System.out.println("  ✓ UserServiceWithLazyLoading created (provider injected)");
    }
    
    public void doSomething() {
        System.out.println("  → Khi cần OrderService:");
        OrderServiceWithLazyLoading orderService = orderServiceProvider.getOrderService();
        System.out.println("  ✓ Got OrderService successfully");
    }
}

class OrderServiceWithLazyLoading {
    public OrderServiceWithLazyLoading() {
        System.out.println("  ✓ OrderServiceWithLazyLoading created");
    }
}

// ════════════════════════════════════════════════════════════════════════════
// GIẢI PHÁP 3: INTERFACE SEGREGATION
// ════════════════════════════════════════════════════════════════════════════

interface OrderCreator {
    void createOrder(String orderId);
}

class UserServiceWithInterface {
    private final OrderCreator orderCreator;
    
    public UserServiceWithInterface(OrderCreator orderCreator) {
        this.orderCreator = orderCreator;
        System.out.println("  ✓ UserServiceWithInterface created with OrderCreator interface");
    }
    
    public void processUser() {
        System.out.println("  → Processing user and creating order...");
        orderCreator.createOrder("ORD-001");
    }
}

class OrderServiceWithInterface implements OrderCreator {
    public OrderServiceWithInterface() {
        System.out.println("  ✓ OrderServiceWithInterface created (độc lập)");
    }
    
    @Override
    public void createOrder(String orderId) {
        System.out.println("  ✓ Order created: " + orderId);
    }
}

// ════════════════════════════════════════════════════════════════════════════
// DEMO VẤN ĐỀ: CIRCULAR DEPENDENCY gây StackOverflowError
// ════════════════════════════════════════════════════════════════════════════

class CircularA {
    private final CircularB b;
    public CircularA() {
        System.out.println("  → CircularA constructor gọi new CircularB()...");
        this.b = new CircularB(); // CircularB lại gọi new CircularA() → vô tận!
    }
}

class CircularB {
    private final CircularA a;
    public CircularB() {
        System.out.println("  → CircularB constructor gọi new CircularA()...");
        this.a = new CircularA(); // CircularA lại gọi new CircularB() → vô tận!
    }
}

// ════════════════════════════════════════════════════════════════════════════
// GIẢI PHÁP 4: SPRING @Lazy - Demo Proxy Concept (Plain Java)
// ════════════════════════════════════════════════════════════════════════════

class LazyProxyDemo {
    interface IOrderService {
        void processOrder(String id);
    }

    // Proxy: bọc IOrderService, chỉ tạo thật khi gọi method
    static class LazyOrderServiceProxy implements IOrderService {
        private IOrderService real = null;

        private IOrderService getReal() {
            if (real == null) {
                System.out.println("  → [Proxy] Khởi tạo OrderService thật lần đầu tiên...");
                real = id -> System.out.println("  ✓ [Real OrderService] processOrder: " + id);
            }
            return real;
        }

        @Override
        public void processOrder(String id) {
            getReal().processOrder(id);
        }
    }

    static class UserServiceLazy {
        private final IOrderService orderService; // nhận proxy, không phải object thật

        UserServiceLazy(IOrderService orderService) {
            this.orderService = orderService;
            System.out.println("  ✓ UserServiceLazy created (chỉ giữ proxy, chưa tạo OrderService thật)");
        }

        void checkout(String orderId) {
            System.out.println("  → UserServiceLazy.checkout() → gọi orderService.processOrder()");
            orderService.processOrder(orderId); // lúc này mới tạo OrderService thật
        }
    }

    static void demo() {
        LazyOrderServiceProxy proxy = new LazyOrderServiceProxy();        // chưa tạo OrderService thật
        UserServiceLazy userSvc = new UserServiceLazy(proxy);             // inject proxy
        System.out.println("  → Trước khi gọi method: OrderService thật chưa được tạo");
        userSvc.checkout("ORD-LAZY-001");                                 // lúc này mới tạo
        System.out.println("  ✓ @Lazy pattern hoạt động thành công!");
        System.out.println();
    }
}

// ════════════════════════════════════════════════════════════════════════════
// GIẢI PHÁP 5: EXTRACT SHARED SERVICE
// ════════════════════════════════════════════════════════════════════════════

class SharedNotificationService {
    public SharedNotificationService() {
        System.out.println("  ✓ SharedNotificationService created (độc lập, không phụ thuộc ai)");
    }

    public void send(String message) {
        System.out.println("  📧 [Notification] " + message);
    }
}

class UserServiceExtracted {
    private final SharedNotificationService notif;

    public UserServiceExtracted(SharedNotificationService notif) {
        this.notif = notif;
        System.out.println("  ✓ UserServiceExtracted created");
    }

    public void registerUser(String name) {
        System.out.println("  → Đăng ký user: " + name);
        notif.send("Chào mừng " + name + " đã đăng ký!");
    }
}

class OrderServiceExtracted {
    private final SharedNotificationService notif;

    public OrderServiceExtracted(SharedNotificationService notif) {
        this.notif = notif;
        System.out.println("  ✓ OrderServiceExtracted created");
    }

    public void placeOrder(String orderId, String user) {
        System.out.println("  → Tạo đơn hàng: " + orderId + " cho " + user);
        notif.send("Đơn hàng " + orderId + " của " + user + " đã được tạo!");
    }
}

