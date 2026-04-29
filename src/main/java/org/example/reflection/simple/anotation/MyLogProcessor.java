package org.example.reflection.simple.anotation;

import java.lang.reflect.Method;

public class MyLogProcessor {
    public static void process(Object obj) throws Exception {
        Class<?> clazz = obj.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyLog.class)) {
                MyLog annotation = method.getAnnotation(MyLog.class);
                System.out.println("Thực thi log: " + annotation.value());
                method.invoke(obj);
            }
        }
    }
}
