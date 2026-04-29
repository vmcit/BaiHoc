package org.example.reflection.simple.anotation;

import org.example.reflection.simple.Effect;

public class AnnotationReflectionDemo {
    public static void main(String[] args) {
        try {
            Class<?> clazz = Class.forName("org.example.reflection.simple.anotation.BassBoostAno");

            // 1. Kiểm tra xem lớp này có được đánh dấu @MyPlugin không
            if (clazz.isAnnotationPresent(MyPlugin.class)) {

                // 2. Lấy đối tượng Annotation ra
                MyPlugin info = clazz.getAnnotation(MyPlugin.class);

                // 3. Đọc dữ liệu từ Annotation
                System.out.println("Phát hiện Plugin: " + info.name());
                System.out.println("Độ ưu tiên: " + info.priority());

                // 4. Khởi tạo và chạy như bình thường
                Effect effect = (Effect) clazz.getDeclaredConstructor().newInstance();
                effect.apply();
            } else {
                System.out.println("Lớp này không có nhãn @MyPlugin!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
