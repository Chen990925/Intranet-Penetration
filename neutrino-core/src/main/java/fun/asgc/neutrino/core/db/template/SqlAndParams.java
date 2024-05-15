 
package fun.asgc.neutrino.core.db.template;

import fun.asgc.neutrino.core.base.Orderly;
import fun.asgc.neutrino.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * sql语句+sql参数的封装，用于支持以下3种用法
 * 1、jdbcTemplate.query(User.class,"select * from user where id = ?",1);
 * 2、dbcTemplate.query(User.class,"select * from user where id = :id", new HashMap<String,Object>(){
 *        {
 * 				this.put("id","1");
 *         }
 *    });
 * 3、dbcTemplate.query(User.class,"select * from user where id = :id", new User().setId("1"));
 * @author: chenjunlin
 * @date: 2022/6/27
 */
public class SqlAndParams {
    private String sql;
    private Object[] paramArray;
    private Map<String,Object> paramMap;
    private Object paramObject;

    public SqlAndParams(String sql, Object[] paramArray) {
        this.sql = sql;
        this.paramArray = paramArray;
    }

    public SqlAndParams(String sql,Map<String,Object> paramMap) {
        this.sql = sql;
        this.paramMap = paramMap;
        this.initParams();
    }

    public SqlAndParams(String sql,Object paramObject) {
        this.sql = sql;
        this.paramObject = paramObject;

        this.initParamMap();
        this.initParams();
    }

    private void initParamMap(){
        if (null == paramObject) {
            return;
        }
        if (null == paramMap) {
            paramMap = new HashMap<>();
        }
        for(Field field : ReflectUtil.getDeclaredFields(paramObject.getClass())){
            paramMap.put(field.getName(), ReflectUtil.getFieldValue(field, paramObject));
        }
    }

    private void initParams() {
        List<Orderly> orderlyList = new ArrayList<>();
        for(String key : paramMap.keySet()){
            int index = sql.indexOf(":" + key);
            if(-1 != index){
                orderlyList.add(new Orderly(paramMap.get(key), index));
                sql = sql.replaceFirst(":" + key, "?");
            }
        }

        this.paramArray = orderlyList.stream().sorted().map(Orderly::getData).collect(Collectors.toList()).toArray();
    }

    public Object[] getParamArray(){
        return paramArray;
    }

    public String getSql(){
        return sql;
    }
}
