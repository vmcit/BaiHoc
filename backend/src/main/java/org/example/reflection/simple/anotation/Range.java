package org.example.reflection.simple.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Range {
    int min() default 0;
    int max() default 100;
    String message() default "Giá trị nằm ngoài phạm vi cho phép";
}
