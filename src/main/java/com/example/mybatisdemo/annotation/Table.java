package com.example.mybatisdemo.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
public @interface Table {

    String orderBy() default "";

    String extColumnKeys() default "";

    String name() default "";

    String alias() default "a";

    String extWhereKeys() default "";

    Column[] columns();

    String extFromKeys() default "";

    String comment() default "";
}
