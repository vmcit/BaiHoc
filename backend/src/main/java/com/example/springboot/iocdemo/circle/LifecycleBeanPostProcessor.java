//package com.example.springboot.iocdemo.circle;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.stereotype.Component;
//
///**
// * BƯỚC 6 & 10: BeanPostProcessor
// *
// * Spring tự động gọi BeanPostProcessor cho MỌI bean trong context.
// * Đây là cơ chế nền tảng của @Autowired, @Transactional, AOP proxy, v.v.
// *
// *   postProcessBeforeInitialization() → gọi TRƯỚC @PostConstruct  (Bước 6)
// *   postProcessAfterInitialization()  → gọi SAU  customInit()      (Bước 10)
// */
//@Component
//public class LifecycleBeanPostProcessor implements BeanPostProcessor {
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName)
//            throws BeansException {
//        if (bean instanceof LifecycleBean) {
//            LifecycleBean.step("6", "BeanPostProcessor.postProcessBeforeInitialization()",
//                    "Gọi TRƯỚC @PostConstruct cho bean: \"" + beanName + "\"\n" +
//                    "             → Spring dùng bước này để: tạo AOP proxy, xử lý @Autowired\n" +
//                    "             → Có thể WRAP hoặc THAY THẾ bean bằng object khác");
//        }
//        return bean; // trả về bean gốc (hoặc proxy)
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName)
//            throws BeansException {
//        if (bean instanceof LifecycleBean) {
//            LifecycleBean.step("10", "BeanPostProcessor.postProcessAfterInitialization()",
//                    "Gọi SAU customInit() cho bean: \"" + beanName + "\"\n" +
//                    "             → Bean đã init xong, sẵn sàng bị wrap thành proxy\n" +
//                    "             → @Transactional, @Async, AOP được tạo ở bước này\n" +
//                    "             ✅ Bean chính thức SẴN SÀNG để sử dụng");
//        }
//        return bean;
//    }
//}
