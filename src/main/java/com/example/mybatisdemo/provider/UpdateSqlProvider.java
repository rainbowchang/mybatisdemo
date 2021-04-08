package com.example.mybatisdemo.provider;

import com.example.mybatisdemo.annotation.Column;
import com.example.mybatisdemo.constants.ConstantUtils;
import com.example.mybatisdemo.service.Entity;
import com.example.utils.lang.DateUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public class UpdateSqlProvider extends SQL implements SqlProvider {

    public String phyDelete(Entity<?> entity) {
        String id = entity.getId();
        if (StringUtils.isBlank(id)) {
            return null;
        }
        String sql = DELETE_FROM(entity.getSqlMap().getTable().name())
                .WHERE("id=#{entity.id}")
                .toString();
        System.out.println(sql);
        System.out.println(entity);
        return sql;
    }

    public String delete(Entity<?> entity) {
        String id = entity.getId();
        if (StringUtils.isBlank(id)) {
            return null;
        }
        entity.setStatus(ConstantUtils.STATUS_DELETE);
        entity.setUpdateDate(new Date());
//        entity.setUpdateBy();  //TODO
        {
            String column_value = DateUtils.formatDate(entity.getUpdateDate(), ConstantUtils.DATE_TIME_PATTERN);
            column_value = "update_date = STR_TO_DATE('" + column_value + "', '%Y-%m-%d %H:%i:%s')";
            SET(column_value);
        }


        String sql = UPDATE(entity.getSqlMap().getTable().name())
                .SET("status=#{entity.status}")
                .WHERE("id=#{entity.id}")
                .toString();
        System.out.println(sql);
        System.out.println(entity);
        return sql;
    }

    public String update(Entity<?> entity) {
        String id = entity.getId();
        if (StringUtils.isBlank(id)) {
            return null;
        }
        entity.setUpdateDate(new Date());
//        entity.setUpdateBy();  //TODO
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
                if(StringUtils.isNotBlank(column.dateFormat())){
                    String sql_date_format;
                    switch (column.dateFormat()){
                        case ConstantUtils.DATE_PATTERN:
                            sql_date_format=ConstantUtils.SQL_DATE_PATTERN;
                            break;
                        case ConstantUtils.DATE_TIME_PATTERN:
                        default:
                            sql_date_format = ConstantUtils.SQL_DATE_TIME_PATTERN;
                            break;
                    }
                    String column_value = DateUtils.formatDate((Date)columnValue, column.dateFormat());
                    column_value = column.name() + " = STR_TO_DATE('" + column_value + "', '" + sql_date_format + "')";
                    SET(column_value);
                } else {
                    SET(setBuilder.append(column.name())
                            .append("=#{entity.").append(column.attrName())
                            .append("}").toString());
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                continue;
            }
        }
        String sql = this.toString();
        System.out.println(sql);
        System.out.println(entity);
        return sql;
    }


}
