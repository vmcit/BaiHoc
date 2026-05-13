package org.example.reflection.simple.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Xác định Annotation có hiệu lực ở đâu (Method, Field, Type/Class)
@Target(ElementType.METHOD)
// Xác định Annotation tồn tại đến lúc nào (RUNTIME là phổ biến nhất để xử lý bằng Reflection)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLog {
    String value() default "Default Log Message";
}