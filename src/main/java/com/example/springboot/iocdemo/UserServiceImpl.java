package com.example.springboot.iocdemo;

/**
 * Implementation của UserService
 * Đây là một Bean - một object được quản lý bởi IoC Container
 */
public class UserServiceImpl implements UserService {
    
    private String serviceName = "UserService";
    
    // Constructor
    public UserServiceImpl() {
        System.out.println("✓ UserServiceImpl Bean đã được khởi tạo");
    }
    
    @Override
    public void saveUser(String name) {
        System.out.println("[" + serviceName + "] Đang lưu user: " + name);
    }
    
    @Override
    public String getUserInfo(String name) {
        System.out.println("[" + serviceName + "] Lấy thông tin user: " + name);
        return "User Info: " + name + " (ID: 123)";
    }
    
    // Getter và Setter (dùng cho Dependency Injection)
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}

