//package com.example.springboot.iocdemo.circle;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.beans.factory.BeanFactoryAware;
//import org.springframework.beans.factory.BeanNameAware;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//
///**
// * DEMO: Vòng đời Bean (Bean Lifecycle) - Spring
// *
// * Bean này implement tất cả các lifecycle interface để thấy rõ
// * THỨ TỰ GỌI của từng bước trong vòng đời.
// *
// * TỔNG QUAN vòng đời (theo thứ tự):
// * ┌─────────────────────────────────────────────────────────────────┐
// * │  1.  Constructor                  → Tạo instance               │
// * │  2.  Setter Injection             → Inject dependency           │
// * │  3.  BeanNameAware                → Biết tên bean               │
// * │  4.  BeanFactoryAware             → Biết BeanFactory            │
// * │  5.  ApplicationContextAware      → Biết ApplicationContext      │
// * │  6.  BeanPostProcessor::before    → Xử lý trước init            │
// * │  7.  @PostConstruct               → Init annotation             │
// * │  8.  InitializingBean             → afterPropertiesSet()        │
// * │  9.  @Bean(initMethod)            → Custom init method          │
// * │  10. BeanPostProcessor::after     → Xử lý sau init             │
// * │  ─── Bean sẵn sàng sử dụng ───────────────────────────────── │
// * │  11. @PreDestroy                  → Dọn dẹp annotation          │
// * │  12. DisposableBean               → destroy()                   │
// * │  13. @Bean(destroyMethod)         → Custom destroy method       │
// * └─────────────────────────────────────────────────────────────────┘
// */
//public class LifecycleBean
//        implements BeanNameAware, BeanFactoryAware, ApplicationContextAware,
//                   InitializingBean, DisposableBean {
//
//    private String name;          // dependency được inject qua setter
//    private String beanName;      // do Spring gán qua BeanNameAware
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 1: CONSTRUCTOR - Spring tạo instance
//    // ══════════════════════════════════════════════════════════════
//    public LifecycleBean() {
//        step("1", "CONSTRUCTOR",
//             "Spring tạo instance bằng new LifecycleBean()\n" +
//             "             → Tại đây: chưa có dependency nào được inject\n" +
//             "             → name = null, beanName = null");
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 2: SETTER INJECTION - Spring inject dependency
//    // ══════════════════════════════════════════════════════════════
//    public void setName(String name) {
//        this.name = name;
//        step("2", "SETTER INJECTION",
//             "Spring inject dependency: setName(\"" + name + "\")\n" +
//             "             → Tất cả @Autowired field/setter được xử lý ở bước này");
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 3: BeanNameAware - Spring thông báo tên bean
//    // ══════════════════════════════════════════════════════════════
//    @Override
//    public void setBeanName(String name) {
//        this.beanName = name;
//        step("3", "BeanNameAware.setBeanName()",
//             "Spring thông báo tên bean: \"" + name + "\"\n" +
//             "             → Bean biết tên của mình trong ApplicationContext");
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 4: BeanFactoryAware - Spring cung cấp BeanFactory
//    // ══════════════════════════════════════════════════════════════
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        step("4", "BeanFactoryAware.setBeanFactory()",
//             "Spring cung cấp BeanFactory\n" +
//             "             → Bean có thể tra cứu bean khác qua beanFactory.getBean()\n" +
//             "             → BeanFactory type: " + beanFactory.getClass().getSimpleName());
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 5: ApplicationContextAware - Spring cung cấp ApplicationContext
//    // ══════════════════════════════════════════════════════════════
//    @Override
//    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
//        step("5", "ApplicationContextAware.setApplicationContext()",
//             "Spring cung cấp ApplicationContext\n" +
//             "             → ApplicationContext type: " + ctx.getClass().getSimpleName() + "\n" +
//             "             → Số bean đã đăng ký: " + ctx.getBeanDefinitionCount());
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 6: BeanPostProcessor::before  ← được gọi từ LifecycleBeanPostProcessor
//    // ══════════════════════════════════════════════════════════════
//    // (xem LifecycleBeanPostProcessor.postProcessBeforeInitialization)
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 7: @PostConstruct - Spring gọi sau khi inject xong
//    // ══════════════════════════════════════════════════════════════
//    @PostConstruct
//    public void postConstruct() {
//        step("7", "@PostConstruct",
//             "Annotation @PostConstruct - init logic chạy SAU inject\n" +
//             "             → Tại đây name = \"" + name + "\", beanName = \"" + beanName + "\"\n" +
//             "             → Thường dùng để: validate, khởi tạo cache, kết nối");
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 8: InitializingBean.afterPropertiesSet()
//    // ══════════════════════════════════════════════════════════════
//    @Override
//    public void afterPropertiesSet() {
//        step("8", "InitializingBean.afterPropertiesSet()",
//             "Interface InitializingBean - gọi sau @PostConstruct\n" +
//             "             → Ít dùng hơn @PostConstruct (phụ thuộc Spring API)\n" +
//             "             → Thường thấy trong internal Spring beans");
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 9: Custom init-method - khai báo trong @Bean(initMethod=...)
//    // ══════════════════════════════════════════════════════════════
//    public void customInit() {
//        step("9", "@Bean(initMethod = \"customInit\")",
//             "Custom init method được khai báo trong @Bean config\n" +
//             "             → Gọi SAU afterPropertiesSet()\n" +
//             "             → Ưu điểm: không cần import Spring annotation trong bean");
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 10: BeanPostProcessor::after  ← được gọi từ LifecycleBeanPostProcessor
//    // ══════════════════════════════════════════════════════════════
//    // (xem LifecycleBeanPostProcessor.postProcessAfterInitialization)
//
//    // ══════════════════════════════════════════════════════════════
//    // METHOD SỬ DỤNG - Sau khi bean đã sẵn sàng
//    // ══════════════════════════════════════════════════════════════
//    public void doWork() {
//        System.out.println("\n  📌 [ĐANG SỬ DỤNG BEAN] doWork() → name = \"" + name + "\"");
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 11: @PreDestroy - Spring gọi trước khi destroy
//    // ══════════════════════════════════════════════════════════════
//    @PreDestroy
//    public void preDestroy() {
//        step("11", "@PreDestroy",
//             "Annotation @PreDestroy - dọn dẹp trước khi bean bị hủy\n" +
//             "             → Thường dùng để: đóng connection, release resource, flush cache");
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 12: DisposableBean.destroy()
//    // ══════════════════════════════════════════════════════════════
//    @Override
//    public void destroy() {
//        step("12", "DisposableBean.destroy()",
//             "Interface DisposableBean - gọi sau @PreDestroy\n" +
//             "             → Ít dùng hơn @PreDestroy");
//    }
//
//    // ══════════════════════════════════════════════════════════════
//    // BƯỚC 13: Custom destroy-method
//    // ══════════════════════════════════════════════════════════════
//    public void customDestroy() {
//        step("13", "@Bean(destroyMethod = \"customDestroy\")",
//             "Custom destroy method được khai báo trong @Bean config\n" +
//             "             → Gọi SAU DisposableBean.destroy()");
//    }
//
//    // ─── helper ───────────────────────────────────────────────────
//    static void step(String no, String name, String detail) {
//        System.out.printf("%n  [BƯỚC %2s] %-46s%n             %s%n",
//                no, "► " + name, detail);
//    }
//}
