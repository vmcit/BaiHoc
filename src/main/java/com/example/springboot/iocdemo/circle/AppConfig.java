//package com.example.springboot.iocdemo.circle;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Spring Configuration - khai báo bean và lifecycle methods
// */
//@Configuration
//@ComponentScan(basePackageClasses = AppConfig.class)  // quét package này để tìm @Component
//public class AppConfig {
//
//    /**
//     * Khai báo LifecycleBean với:
//     *   - initMethod   → Bước 9  (gọi sau afterPropertiesSet)
//     *   - destroyMethod → Bước 13 (gọi sau DisposableBean.destroy)
//     */
//    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
//    public LifecycleBean lifecycleBean() {
//        LifecycleBean bean = new LifecycleBean();
//        // Bước 2: Spring sẽ inject qua setter sau khi gọi constructor
//        bean.setName("SpringManagedBean");
//        return bean;
//    }
//}
