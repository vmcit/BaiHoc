package org.example.reflection.simple.anotation;

import java.lang.reflect.Field;

public class Validator {
    public static void validate(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            // Kiểm tra field có gắn @Range không
            if (field.isAnnotationPresent(Range.class)) {
                field.setAccessible(true); // Mở khóa để đọc biến private

                Range range = field.getAnnotation(Range.class);
                int value = (int) field.get(obj); // Lấy giá trị thực tế của đối tượng

                // Logic kiểm tra
                if (value < range.min() || value > range.max()) {
                    throw new IllegalArgumentException(
                            "Lỗi tại trường '" + field.getName() + "': " + range.message() +
                                    " (Giá trị thực tế: " + value + ")"
                    );
                }
            }
        }
        System.out.println("Dữ liệu hợp lệ!");
    }
    public static String  validateandJson(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        String result = null;

        for (Field field : clazz.getDeclaredFields()) {
            // Kiểm tra field có gắn @Range không
            if (field.isAnnotationPresent(Range.class)) {
                field.setAccessible(true); // Mở khóa để đọc biến private

                Range range = field.getAnnotation(Range.class);
                int value = (int) field.get(obj); // Lấy giá trị thực tế của đối tượng

                // Logic kiểm tra
                if (value < range.min() || value > range.max()) {
                    throw new IllegalArgumentException(
                            "Lỗi tại trường '" + field.getName() + "': " + range.message() +
                                    " (Giá trị thực tế: " + value + ")"
                    );
                } else {
                     result = JsonProcessor.toJson(obj);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Employee e1 = new Employee("Nguyen Van A", 25, 5);
        Employee e2 = new Employee("Tran Thi B", 70, 5); // Tuổi sai

        try {
            System.out.print("Kiểm tra E1: ");
            System.out.println(validateandJson(e1));

            System.out.print("Kiểm tra E2: ");
            System.out.println(validateandJson(e2));// Sẽ bắn ra Exception ở đây
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
