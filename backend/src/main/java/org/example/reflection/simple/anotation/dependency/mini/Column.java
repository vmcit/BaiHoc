package org.example.reflection.simple.anotation.dependency.mini;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface Column {
    String name();
    String type() default "VARCHAR(255)"; // Thêm kiểu dữ liệu mặc định

}
