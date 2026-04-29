package org.example.reflection.simple.anotation;

import java.lang.reflect.Field;

public class JsonProcessor {
    public static String toJson(Object obj) throws IllegalAccessException {
        StringBuilder jsonBuilder = new StringBuilder("{");
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            // Kiểm tra xem field có đánh dấu @JsonField không
            if (field.isAnnotationPresent(JsonField.class)) {
                field.setAccessible(true); // Cho phép truy cập biến private

                JsonField annotation = field.getAnnotation(JsonField.class);
                // Lấy tên từ annotation, nếu trống thì lấy tên biến gốc
                String key = annotation.value().isEmpty() ? field.getName() : annotation.value();
                Object value = field.get(obj);

                jsonBuilder.append("\"").append(key).append("\": \"").append(value).append("\",");
            }
        }

        // Xóa dấu phẩy cuối cùng và đóng ngoặc
        if (jsonBuilder.length() > 1) jsonBuilder.setLength(jsonBuilder.length() - 1);
        return jsonBuilder.append("}").toString();
    }

    public static void main(String[] args) throws IllegalAccessException {
        User user = new User("Gemini", "ai@google.com", "123456");

        System.out.println(toJson(user));
        // Kết quả: {"user_name": "Gemini","user_email": "ai@google.com"}
        // Biến 'password' đã bị bỏ qua vì không có Annotation.
    }
}
