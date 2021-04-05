package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.provider.InsertSqlProvider;
import com.example.mybatisdemo.provider.SelectSqlProvider;
import com.example.mybatisdemo.provider.UpdateSqlProvider;
import com.example.mybatisdemo.service.Entity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CrudDao<T extends Entity<?>> {

    @SelectProvider(type= SelectSqlProvider.class, method = "get")
    T get(@Param("entity") T entity);

    @SelectProvider(type= SelectSqlProvider.class, method = "findList")
    List<T> findList(@Param("entity") T entity);

    @InsertProvider(type= InsertSqlProvider.class, method = "insert")
    long insert(@Param("entity") T entity);

    @UpdateProvider(type= UpdateSqlProvider.class, method = "delete")
    long delete(@Param("entity") T entity);

    @UpdateProvider(type= UpdateSqlProvider.class, method = "update")
    long update(@Param("entity") T entity);

}
