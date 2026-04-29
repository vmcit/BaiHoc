package org.example.reflection;

import java.lang.reflect.Field;

public class PrivateFieldReflection {
    public static void main(String[] args) {
        try {
            // Khởi tạo một đối tượng Student bình thường
            Student student = new Student();

            // Lấy đối tượng Class
            Class<?> clazz = student.getClass();

            // 1. Truy cập vào field "name" (đang là private)
            // Lưu ý: Phải dùng getDeclaredField thay vì getField
            Field nameField = clazz.getDeclaredField("name");

            // 2. "Bẻ khóa": Cho phép truy cập vào biến private
            nameField.setAccessible(true);

            // 3. Gán giá trị mới cho biến name của đối tượng student
            nameField.set(student, "Nguyễn Văn A");

            // 4. Kiểm tra lại kết quả
            String value = (String) nameField.get(student);
            System.out.println("Giá trị biến private sau khi can thiệp: " + value);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
