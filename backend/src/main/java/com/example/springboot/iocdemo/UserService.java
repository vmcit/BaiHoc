package com.example.springboot.iocdemo;

/**
 * Interface cho Service Bean
 */
public interface UserService {
    void saveUser(String name);
    String getUserInfo(String name);
}

