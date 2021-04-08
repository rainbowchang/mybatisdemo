package com.example.mybatisdemo.service;

import com.example.mybatisdemo.mapper.CrudDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class CrudService<Dao extends CrudDao<T>, T extends Entity<?>> {

    @Autowired
    protected Dao dao;

    public CrudService(){
    }


    public long insert(T entity) {
        return dao.insert(entity);
    }

    public long update(T entity){
        return dao.update(entity);
    }

    public long save(T entity){
        if(entity.isNewRecord){
            return this.insert(entity);
        } else {
            return this.update(entity);
        }
    }

    /**
     * 根据ID字段获取一条记录
     * @param id
     * @return
     */
    public T get(String id){

        if(StringUtils.isBlank(id)){
            return null;
        }
        Class<T> clazz = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        try {
            T t = clazz.newInstance();
            t.setId(id);
            return dao.get(t);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据实体，通过其ID获取实体的全部。
     * 如果id为空，返回null。
     * @param t
     * @return
     */
    public T get(T t){ //TODO 改用SelectProvider
        if(t == null){
            return null;
        }
        if(StringUtils.isBlank(t.id)){
            return null;
        }
        return dao.get(t);
    }

    /**
     * 根据实体的部分字段，查找到满足条件的所有记录。
     * @param t
     * @return
     */
    public List<T> findList(T t) {
        return dao.findList(t);
    }

    public long delete(T entity){
        return dao.delete(entity);
    }

}
