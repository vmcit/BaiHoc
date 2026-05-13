package com.example.springboot.iocdemo;

/**
 * Simple IoC Container - Quản lý các Bean (Object)
 * Đây là demo đơn giản của khái niệm Inversion of Control
 */
public class SimpleIoCContainer {
    
    private UserService userService;
    
    /**
     * Constructor: Container khởi tạo Bean
     * Đây gọi là "Constructor Injection" - đưa dependency vào thông qua constructor
     */
    public SimpleIoCContainer() {
        // Container tự tạo và quản lý Bean
        this.userService = new UserServiceImpl();
        System.out.println("✓ SimpleIoCContainer khởi tạo xong, Bean đã được inject\n");
    }
    
    /**
     * Phương thức này demo Setter Injection
     * Đưa Bean vào sau khi khởi tạo thông qua Setter
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
        System.out.println("✓ UserService Bean đã được inject via Setter");
    }
    
    /**
     * Lấy Bean từ Container
     */
    public UserService getUserService() {
        return userService;
    }
    
    /**
     * Sử dụng Bean thông qua Container
     */
    public void executeUserOperation(String userName) {
        System.out.println("\n--- Thực thi Bean operation ---");
        userService.saveUser(userName);
        String info = userService.getUserInfo(userName);
        System.out.println("Result: " + info);
    }

}

