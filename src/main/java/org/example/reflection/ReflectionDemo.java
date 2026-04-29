package org.example.reflection;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ReflectionDemo {
    public static void main(String[] args) {
        try {
            // 1. Lấy đối tượng Class từ đường dẫn đầy đủ
            Class<?> clazz = Class.forName("org.example.reflection.Student");

            // 2. Lấy tên lớp
            System.out.println("Full Name: " + clazz.getName());
            System.out.println("Simple Name: " + clazz.getSimpleName());

            // 3. Kiểm tra Modifiers (public, final, static,...)
            int modifiers = clazz.getModifiers();
            System.out.println("Is Final? " + Modifier.isFinal(modifiers));
            System.out.println("Is Public? " + Modifier.isPublic(modifiers));

            // 4. Kiểm tra Interfaces mà lớp này thực thi (implements)
            Class<?>[] interfaces = clazz.getInterfaces();
            System.out.println("Interfaces: " + Arrays.toString(interfaces));

            // 5. Lấy danh sách các phương thức public (bao gồm cả từ lớp cha)
            System.out.println("--- Public Methods ---");
            Arrays.stream(clazz.getMethods()).forEach(m -> System.out.println(m.getName()));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
