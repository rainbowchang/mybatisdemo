package com.example.mybatisdemo.annotation;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Column {
    JdbcType jdbcType() default JdbcType.UNDEFINED;

    Class<?> includeEntity() default Class.class;

    /**
     * 属性名称，也是字段对应的对象里的变量名称
     * @return
     */
    String attrName() default "";

    Class<?> javaType() default void.class;

//    QueryType queryType() default QueryType.EQ;

    boolean isInsert() default true;

    String comment() default "";

    boolean isUpdateForce() default false;

    /**
     * 界面展示的标签名称
     * @return
     */
    String label() default "";

    boolean isTreeName() default false;

    boolean isPK() default false;

    boolean isUpdate() default true;

    boolean isQuery() default true;

    /**
     * 字段的名称
     * @return
     */
    String name() default "";

    Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;

    /**
     * 日期的格式， 推荐两种yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd
     * @return
     */
    String dateFormat() default "";
}
