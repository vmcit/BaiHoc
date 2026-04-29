package org.example.reflection.simple;

import org.example.reflection.Student;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SimpleFrameworkDemo {
    public static void main(String[] args) {
        // 1. Giả sử đây là dữ liệu nhận được từ giao diện (Web/Mobile)
        Map<String, Object> formData = new HashMap<>();
        formData.put("name", "Nguyễn Văn Học");
        formData.put("age", 20);
        // formData.put("className", "IT");

        // 2. Thay vì dùng Student s = new Student(); s.setName("...");
        // Ta dùng một hàm dùng Reflection để làm tự động
        Student student = (Student) fillData(new Student(), formData);

        // Kiểm tra kết quả
        System.out.println("Tên sau khi map: " + getPrivateFieldValue(student, "name"));
        System.out.println("Tuổi sau khi map: " + student.age);
        //System.out.println("Class sau khi map: " + getPrivateFieldValue(student, "className"));
    }

    // Hàm "Framework" dùng Reflection để gán dữ liệu tự động
    public static Object fillData(Object obj, Map<String, Object> data) {
        Class<?> clazz = obj.getClass();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            try {
                // Tìm biến dựa trên key của Map
                Field field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true); // Bẻ khóa private

                // Gán giá trị từ Map vào biến của Object
                field.set(obj, entry.getValue());
            } catch (Exception e) {
                System.out.println("Không tìm thấy biến: " + entry.getKey());
            }
        }
        return obj;
    }

    // Hàm phụ trợ để đọc biến private phục vụ việc kiểm tra
    private static Object getPrivateFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) { return null; }
    }
}