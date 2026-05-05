//package com.example.springboot.iocdemo.circle;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
///**
// * DEMO: Vòng đời Bean (Bean Lifecycle) trong Spring
// *
// * Chạy bằng:
// *   cmd /c "chcp 65001 > nul && set JAVA_HOME=C:\Users\cuongmanh.vu\.jdks\ms-17.0.18 && ^
// *   %JAVA_HOME%\bin\java -Dfile.encoding=UTF-8 ^
// *   -cp D:\Book\ocp\baihoc\BaiHoc\target\classes ^
// *   com.example.springboot.iocdemo.circle.BeanLifecycleDemoMain"
// */
//public class BeanLifecycleDemoMain {
//
//    public static void main(String[] args) {
//        printHeader();
//
//        // ══════════════════════════════════════════════════════════
//        // PHASE 1: KHỞI TẠO CONTEXT → Spring tạo và init bean
//        // ══════════════════════════════════════════════════════════
//        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//        System.out.println("  🔧 PHASE 1: ApplicationContext khởi động → Spring tạo & init bean");
//        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//
//        // Tạo context = Spring bắt đầu đọc config, tạo bean, gọi lifecycle
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(AppConfig.class);
//
//        // ══════════════════════════════════════════════════════════
//        // PHASE 2: SỬ DỤNG BEAN - Bean đã sẵn sàng
//        // ══════════════════════════════════════════════════════════
//        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//        System.out.println("  ✅ PHASE 2: Bean sẵn sàng → Ứng dụng đang chạy bình thường");
//        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//
//        LifecycleBean bean = context.getBean(LifecycleBean.class);
//        bean.doWork();
//        bean.doWork();
//
//        System.out.println("\n  (... ứng dụng đang chạy, bean phục vụ request ...)");
//
//        // ══════════════════════════════════════════════════════════
//        // PHASE 3: ĐÓNG CONTEXT → Spring hủy bean, gọi destroy hooks
//        // ══════════════════════════════════════════════════════════
//        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//        System.out.println("  🔴 PHASE 3: ApplicationContext đóng → Spring hủy bean");
//        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//
//        context.close(); // Gọi destroy lifecycle: @PreDestroy → destroy() → customDestroy()
//
//        // ══════════════════════════════════════════════════════════
//        // TỔNG KẾT
//        // ══════════════════════════════════════════════════════════
//        printSummary();
//    }
//
//    static void printHeader() {
//        System.out.println("════════════════════════════════════════════════════════════════════════════");
//        System.out.println("          DEMO: Vòng đời Bean (Bean Lifecycle) trong Spring");
//        System.out.println("════════════════════════════════════════════════════════════════════════════");
//        System.out.println("""
//
//            Vòng đời một Spring Bean có 3 PHASE:
//
//              🔧 PHASE 1: KHỞI TẠO  → Constructor → Inject → Aware → PostProcessor → Init
//              ✅ PHASE 2: SỬ DỤNG   → Bean sẵn sàng phục vụ
//              🔴 PHASE 3: HỦY       → PreDestroy → Destroy → Custom destroy
//
//            Các bước chi tiết:
//              Bước  1 │ Constructor
//              Bước  2 │ Setter Injection (inject dependencies)
//              Bước  3 │ BeanNameAware.setBeanName()
//              Bước  4 │ BeanFactoryAware.setBeanFactory()
//              Bước  5 │ ApplicationContextAware.setApplicationContext()
//              Bước  6 │ BeanPostProcessor.postProcessBeforeInitialization()
//              Bước  7 │ @PostConstruct
//              Bước  8 │ InitializingBean.afterPropertiesSet()
//              Bước  9 │ @Bean(initMethod)
//              Bước 10 │ BeanPostProcessor.postProcessAfterInitialization()
//                      │ ── Bean SẴN SÀNG ─────────────────────────────────
//              Bước 11 │ @PreDestroy
//              Bước 12 │ DisposableBean.destroy()
//              Bước 13 │ @Bean(destroyMethod)
//            """);
//    }
//
//    static void printSummary() {
//        System.out.println("\n════════════════════════════════════════════════════════════════════════════");
//        System.out.println("  💡 TỔNG KẾT: Khi nào dùng cái nào?");
//        System.out.println("════════════════════════════════════════════════════════════════════════════");
//        System.out.println("""
//
//            ┌──────────────────────────────────┬────────────────────────────────────┐
//            │ Hook                             │ Dùng khi                           │
//            ├──────────────────────────────────┼────────────────────────────────────┤
//            │ @PostConstruct                   │ ✅ Khuyến nghị - init logic        │
//            │ InitializingBean.afterProperties │ ⚠️  Internal Spring, ít dùng       │
//            │ @Bean(initMethod)                │ ✅ Khi bean từ thư viện ngoài      │
//            │ BeanPostProcessor                │ 🔧 Framework dev - tạo AOP proxy   │
//            │ @PreDestroy                      │ ✅ Khuyến nghị - cleanup           │
//            │ DisposableBean.destroy()         │ ⚠️  Internal Spring, ít dùng       │
//            │ @Bean(destroyMethod)             │ ✅ Khi bean từ thư viện ngoài      │
//            └──────────────────────────────────┴────────────────────────────────────┘
//
//            📌 NGUYÊN TẮC:
//               ✓ Dùng @PostConstruct / @PreDestroy cho bean của mình
//               ✓ Dùng @Bean(initMethod/destroyMethod) cho bean từ thư viện ngoài
//               ✓ BeanPostProcessor chỉ viết khi làm framework/infrastructure
//            """);
//    }
//}
