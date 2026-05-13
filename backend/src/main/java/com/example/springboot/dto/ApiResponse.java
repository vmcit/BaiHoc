package com.example.springboot.dto;

/**
 * DTO cho API Response chung
 */
public class ApiResponse {
    private int code;
    private String message;
    private Object data;
    private Long timestamp;
    
    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public ApiResponse(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public ApiResponse(int code, String message, Object data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    // Static methods for quick creation
    public static ApiResponse success(Object data) {
        return new ApiResponse(200, "Success", data);
    }
    
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(200, message, data);
    }
    
    public static ApiResponse error(String message) {
        return new ApiResponse(400, message);
    }
    
    public static ApiResponse error(int code, String message) {
        return new ApiResponse(code, message);
    }
    
    public static ApiResponse unauthorized() {
        return new ApiResponse(401, "Unauthorized");
    }
    
    public static ApiResponse forbidden() {
        return new ApiResponse(403, "Forbidden");
    }
    
    public static ApiResponse notFound() {
        return new ApiResponse(404, "Not found");
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // Getters & Setters
    // ═══════════════════════════════════════════════════════════════════════════
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

