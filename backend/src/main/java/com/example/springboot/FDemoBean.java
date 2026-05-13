package com.example.springboot;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FDemoBean {


    // BƯỚC 1: Constructor — Spring tạo object
    public FDemoBean() {
        System.out.println("[BƯỚC 1] Constructor chạy — Spring vừa tạo DemoBean");
    }

    // BƯỚC 2: Setter Injection — Spring inject giá trị từ application.properties
    @Value("${spring.application.name:MyApp}")
    public void setAppName(String appName) {
        System.out.println("[BƯỚC 2] Setter Injection chạy — inject appName = \"" + appName + "\"");
    }

    // BƯỚC 7: @PostConstruct — sau khi inject xong, trước khi dùng
    @PostConstruct
    public void init() {
        ///  khoi tạo table database
        System.out.println("[BƯỚC 7] @PostConstruct chạy — bean sẵn sàng sử dụng");
    }

    // BƯỚC 11: @PreDestroy — trước khi Spring hủy bean (khi tắt app)
    @PreDestroy
    public void cleanup() {
        System.out.println("[BƯỚC 11] @PreDestroy chạy — Spring chuẩn bị hủy bean");
    }
}


