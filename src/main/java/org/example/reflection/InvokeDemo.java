package org.example.reflection;

import java.lang.reflect.Method;

public class InvokeDemo {
    public static void main(String[] args) {
        try {
            Calculator cal = new Calculator();
            Class<?> clazz = cal.getClass();

            Class<?> clazz1 = Class.forName("org.example.reflection.simple.BassBoost");
            ///   Method addMethod = clazz1.getDeclaredMethod("aply", int.class, int.class);
            // addMethod.invoke(clazz1, null);
            Class<?> clazz2 = Class.forName("org.example.reflection.simple.NextSong");

            // --- TRƯỜNG HỢP 1: Gọi hàm public có tham số ---
            // Bước 1: Tìm method tên "add" với 2 tham số kiểu int
            Method addMethod = clazz.getDeclaredMethod("add", int.class, int.class);

            // Bước 2: Thực thi (Invoke)
            // Cú pháp: method.invoke(đối_tượng, các_tham_số...)
            System.out.print("Gọi hàm add qua Reflection: ");
            addMethod.invoke(cal, 10, 5);


            // --- TRƯỜNG HỢP 2: Gọi hàm private ---
            // Bước 1: Tìm method tên "secretCalc"
            Method secretMethod = clazz.getDeclaredMethod("secretCalc", int.class);

            // Bước 2: "Bẻ khóa" vì nó là private
            secretMethod.setAccessible(true);

            // Bước 3: Thực thi
            System.out.print("Gọi hàm private qua Reflection: ");
            secretMethod.invoke(cal, 99);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
