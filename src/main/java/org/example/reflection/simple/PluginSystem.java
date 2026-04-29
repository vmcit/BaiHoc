package org.example.reflection.simple;

public class PluginSystem {
    public static void main(String[] args) {
        // Giả sử tên lớp này được đọc từ một file cấu hình bên ngoài
        String pluginClassName = "org.example.reflection.simple.BassBoost";

        try {
            // 1. Nạp lớp vào bộ nhớ bằng tên chuỗi
            Class<?> clazz = Class.forName(pluginClassName);

            // 2. Kiểm tra xem lớp này có thực thi interface Effect không
            if (Effect.class.isAssignableFrom(clazz)) {

                // 3. Khởi tạo đối tượng từ lớp đã nạp
                Effect effect = (Effect) clazz.getDeclaredConstructor().newInstance();

                // 4. Thực thi phương thức
                effect.apply();
            } else {
                System.out.println("Lớp này không phải là một Effect hợp lệ!");
            }

        } catch (Exception e) {
            System.out.println("Không thể nạp Plugin: " + e.getMessage());
        }
    }
}
