package org.example.reflection.simple.anotation.dependency.mini;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface Id {
    boolean autoIncrement() default false; // Thêm thuộc tính này
}
