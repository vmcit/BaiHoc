package org.example.reflection.simple.anotation.dependency;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MyDIContainer {

    // Nơi lưu trữ các đối tượng đã được khởi tạo (Singleton)
    private static final Map<Class<?>, Object> beans = new HashMap<>();

    // Hàm lấy hoặc tạo mới một Instance (Bean)
    public static <T> T getBean(Class<T> clazz) throws Exception {
        // Nếu đã có trong map thì trả về luôn
        if (beans.containsKey(clazz)) {
            return clazz.cast(beans.get(clazz));
        }

        // 1. Khởi tạo đối tượng bằng Reflection
        T instance = clazz.getDeclaredConstructor().newInstance();
        beans.put(clazz, instance);

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                // Phá vỡ tính đóng gói để gán giá trị cho private field
                field.setAccessible(true);

                // Đệ quy để lấy đối tượng cho Field này (Dependency)
                Object dependency = getBean(field.getType());

                // Gán dependency vào instance hiện tại
                field.set(instance, dependency);
            }
        }

        return instance;
    }
}
