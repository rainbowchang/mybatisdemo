package com.example.mybatisdemo.provider;

import com.example.mybatisdemo.service.Entity;

import java.lang.reflect.Field;

public abstract  interface SqlProvider {

     default Field getField(Entity<?> t, String fieldName) {
        for (Field field : t.getSqlMap().getFields()) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }
}
