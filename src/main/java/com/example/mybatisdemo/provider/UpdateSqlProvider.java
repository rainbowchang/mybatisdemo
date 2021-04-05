package com.example.mybatisdemo.provider;

import com.example.mybatisdemo.annotation.Column;
import com.example.mybatisdemo.service.Entity;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

public class UpdateSqlProvider extends SQL implements SqlProvider {

    public String phyDelete(Entity<?> entity) {
        String id = entity.getId();
        if (!StringUtils.hasLength(id)) {
            return null;
        }
        String sql = DELETE_FROM(entity.getSqlMap().getTable().name())
                .WHERE("id=#{entity.id}")
                .toString();
        System.out.println(sql);
        return sql;
    }

    public String delete(Entity<?> entity) {
        String id = entity.getId();
        if (!StringUtils.hasLength(id)) {
            return null;
        }
        String sql = UPDATE(entity.getSqlMap().getTable().name())
                .SET("status=#{entity.status}")
                .WHERE("id=#{entity.id}")
                .toString();
        System.out.println(sql);
        return sql;
    }

    public String update(Entity<?> entity) {
        String id = entity.getId();
        if (!StringUtils.hasLength(id)) {
            return null;
        }

        UPDATE(entity.getSqlMap().getTable().name())
                .WHERE("id=#{entity.id}");
        List<Column> columnList = entity.getSqlMap().getColumns();
        for (Column column : columnList) {
            try {
                String columnName = column.attrName();
                Field field = getField(entity, columnName);
                if (field == null)
                    continue;
                field.setAccessible(true);
                Object columnValue = field.get(entity);
                if (columnValue == null || "".equals(columnValue))
                    continue;
                StringBuilder setBuilder = new StringBuilder();

                SET(setBuilder.append(column.name())
                        .append("=#{entity.").append(column.attrName())
                        .append("}").toString());
                setBuilder.append(", ").append(column.name()).append("='").append(columnValue).append("'");
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                continue;
            }
        }
        String sql = this.toString();
        System.out.println(sql);
        return sql;
    }


}
