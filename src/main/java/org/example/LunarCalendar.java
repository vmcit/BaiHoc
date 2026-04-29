package org.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Annotation để đánh dấu biến thời gian lịch dương, có thể chuyển đổi sang lịch âm
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LunarCalendar {
    // Có thể thêm thuộc tính nếu cần, ví dụ format
    String format() default "dd/MM/yyyy";
}
