package com.example.mybatisdemo.provider;

import com.example.mybatisdemo.annotation.Column;
import com.example.mybatisdemo.service.Entity;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.List;

public class SelectSqlProvider extends SQL implements SqlProvider {

    /**
     * 根据实体的ID获取实体的全部
     *
     * @param entity
     * @return
     */
    public String get(Entity<?> entity) {
        String id = entity.getId();
        String sqlString = createSelectColumn(entity.getSqlMap().getColumns())
                .FROM(entity.getSqlMap().getTable().name())
                .WHERE("id=#{entity.id}")
                .toString();
        System.out.println(sqlString);
        System.out.println(entity);
        return sqlString;
    }

    public String findList(Entity<?> entity) {
        createSelectColumn(entity.getSqlMap().getColumns())
                .FROM(entity.getSqlMap().getTable().name())
                .createWhereSeg(entity);
        String sqlString = this.toString();
        System.out.println(sqlString);
        System.out.println(entity);
        return sqlString;
    }

    @Override
    public SelectSqlProvider FROM(String table){
        super.FROM(table);
        return this;
    }

    private SelectSqlProvider createSelectColumn(List<Column> columnList) {
        for (Column column : columnList) {
            this.SELECT(column.name() + " as " + column.attrName());
        }
        return this;
    }

    private SelectSqlProvider createWhereSeg(Entity<?> t) {
        this.WHERE("1=1");
        for (Column column : t.getSqlMap().getColumns()) {
            try {
                String columnName = column.attrName();
                Field field = getField(t, columnName);
                if (field == null)
                    continue;
                field.setAccessible(true);
                Object columnValue = field.get(t);
                if (columnValue == null || "".equals(columnValue))
                    continue;
                this.WHERE(column.name() + "=#{entity." + column.attrName() + "}");
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                continue;
            }
        }
        return this;
    }

}
