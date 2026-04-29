package org.example;

import java.lang.reflect.Method;

public class AnnotationExample {

    // Sử dụng annotation tùy chỉnh
    @MyAnnotation(value = "Đây là một phương thức được annotate", priority = 5)
    public void annotatedMethod() {
        System.out.println("Phương thức annotatedMethod được gọi!");
    }

    // Phương thức không có annotation
    public void normalMethod() {
        System.out.println("Phương thức normalMethod được gọi!");
    }

    public static void main(String[] args) {
        try {
            // Lấy class
            Class<?> clazz = AnnotationExample.class;

            // Duyệt qua tất cả các phương thức
            for (Method method : clazz.getDeclaredMethods()) {
                // Kiểm tra xem phương thức có annotation MyAnnotation không
                if (method.isAnnotationPresent(MyAnnotation.class)) {
                    // Lấy annotation
                    MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
                    System.out.println("Phương thức: " + method.getName());
                    System.out.println("Giá trị annotation: " + annotation.value());
                    System.out.println("Độ ưu tiên: " + annotation.priority());
                    System.out.println("---");
                }
            }

            // Tạo instance và gọi phương thức
            AnnotationExample example = new AnnotationExample();
            example.annotatedMethod();
            example.normalMethod();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
