package org.example.reflection.simple.anotation.aoplog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//Chúng ta sẽ tạo một Annotation để tự động đo và in ra
// thời gian chạy của bất kỳ hàm nào mà nó được gắn vào. Đây là kỹ thuật thuộc về AOP (Aspect-Oriented Programming).
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
    String unit() default "ms"; // Cho phép chọn đơn vị: ms hoặc ns
}
