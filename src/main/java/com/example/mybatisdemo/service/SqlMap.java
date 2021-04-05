package com.example.mybatisdemo.service;

import com.example.mybatisdemo.annotation.Column;
import com.example.mybatisdemo.annotation.Table;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class SqlMap<T extends Entity<T>> extends LinkedHashMap<String, Object> {

    private Class<T> entityClazz;
    private List<Column> columns;
    private Table table;
    private List<Field> fields;

    private Entity<T> entity;
    public SqlMap(Entity<T> entity){
        entityClazz = (Class<T>) ((ParameterizedType)entity.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        Field[] fields = entityClazz.getDeclaredFields();//通过反射获取类的域（全局变量）
        if(!entityClazz.isAnnotationPresent(Table.class)){
            return ;
        }
        table = entityClazz.getAnnotation(Table.class);
        columns = new ArrayList<>();
        fields = new ArrayList<>();
        this.entity = entity;
        setColumnArray(entityClazz);
        setFieldsArray(entityClazz);
    }

    private void setColumnArray(Class<?> clazz){
        Table table = clazz.getAnnotation(Table.class);
        List<Column> columnList = Arrays.asList(table.columns());
        columns.addAll(columnList);
        Class<?> parentClazz = clazz.getSuperclass();
        if(parentClazz.isAnnotationPresent(Table.class)){
            setColumnArray(parentClazz);
        }
    }

    private void setFieldsArray(Class<?> clazz){
        Field[] fieldArray = clazz.getDeclaredFields();
        fields.addAll(Arrays.asList(fieldArray));
        Class<?> parentClazz = clazz.getSuperclass();
        if (parentClazz == null){
            return;
        }
        setFieldsArray(parentClazz);
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Table getTable() {
        return table;
    }

    public List<Field> getFields() {
        return fields;
    }
}
