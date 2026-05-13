package org.example.reflection.simple.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Annotation này chỉ được dùng trên các biến (FIELD)
@Target(ElementType.FIELD)
// Annotation này sẽ tồn tại cho đến khi chương trình chạy (RUNTIME)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonField {
    String value() default ""; // Cho phép truyền thêm tham số nếu muốn đặt tên khác
}
