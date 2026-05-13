package org.example.reflection.simple.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) // Chỉ dùng cho Class/Interface
public @interface MyPlugin {
    String name(); // Tên của plugin
    int priority() default 1; // Độ ưu tiên
}