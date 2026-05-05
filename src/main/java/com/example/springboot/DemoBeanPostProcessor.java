package com.example.springboot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class DemoBeanPostProcessor  {

    public DemoBeanPostProcessor() {
        System.out.println("[BƯỚC 1] Constructor chạy — Spring vừa tạo DemoBeanPostProcessor");
    }

//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if (bean instanceof FDemoBean) {
//            System.out.println("[BƯỚC  6] BeanPostProcessor.postProcessBeforeInitialization()  — TRƯỚC @PostConstruct");
//        }
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (bean instanceof FDemoBean) {
//            System.out.println("[BƯỚC 10] BeanPostProcessor.postProcessAfterInitialization()   — SAU init, bean SẴN SÀNG");
//        }
//        return bean;
//    }
}
