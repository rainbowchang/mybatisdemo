package com.example.mybatisdemo.provider;

import com.example.mybatisdemo.annotation.Column;
import com.example.mybatisdemo.constants.ConstantUtils;
import com.example.mybatisdemo.service.Entity;
import com.example.utils.idgen.IdGenerate;
import com.example.utils.lang.DateUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public class InsertSqlProvider extends SQL implements SqlProvider{

    public String insert(Entity<?> entity){

        if(StringUtils.isBlank(entity.getId())){
            entity.setId(IdGenerate.nextId());
        }
        if(StringUtils.isBlank(entity.getCorpCode())){

        }
        if(StringUtils.isBlank(entity.getCorpName())){

        }
        Date now = new Date();
        if(entity.getCreateDate() == null ){
            entity.setCreateDate(now);
        }
        if(entity.getUpdateBy() == null){
            entity.setUpdateDate(now);
        }
        if(StringUtils.isBlank(entity.getCreateBy())){

        }
        if(StringUtils.isBlank(entity.getUpdateBy())){

        }
        if(StringUtils.isBlank(entity.getStatus())){
            entity.setStatus(ConstantUtils.STATUS_VALID);
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
                    column_value = "STR_TO_DATE('" + column_value + "', '" + sql_date_format + "')";
                    this.INTO_COLUMNS(column.name()).INTO_VALUES(column_value);
                } else {
                    this.INTO_COLUMNS(column.name()).INTO_VALUES("#{entity." + column.attrName() + "}");
                }
            }catch (IllegalArgumentException | IllegalAccessException ex){
                continue;
            }
        }
        return this;
    }

}
