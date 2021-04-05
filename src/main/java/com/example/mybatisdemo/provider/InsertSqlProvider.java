package com.example.mybatisdemo.provider;

import com.example.mybatisdemo.annotation.Column;
import com.example.mybatisdemo.service.Entity;
import com.example.utils.idgen.IdGenerate;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

public class InsertSqlProvider extends SQL implements SqlProvider{

    public String insert(Entity<?> entity){
        if(!StringUtils.hasLength(entity.getId())){
            entity.setId(IdGenerate.nextId());
        }
        this.scanColumn(entity).INSERT_INTO(entity.getSqlMap().getTable().name());
        String sqlString = this.toString();
        System.out.println(sqlString);
        System.out.println(entity);
        return sqlString;
    }

    protected InsertSqlProvider scanColumn(Entity<?> entity) {
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
                this.INTO_COLUMNS(column.name()).INTO_VALUES("#{entity." + column.attrName() + "}");
            }catch (IllegalArgumentException | IllegalAccessException ex){
                continue;
            }
        }
        return this;
    }

}
