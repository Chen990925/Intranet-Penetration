 
package fun.asgc.neutrino.core.db.dao;

import fun.asgc.neutrino.core.db.template.SqlAndParams;

import java.util.LinkedHashMap;

/**
 * sql方言，用于屏蔽各种不同的数据库的sql差异
 * @author:  chenjunlin
 * @date: 2024/5/15
 */
public interface SqlDialect {
    /**
     * 新增单条记录
     * @param obj
     * @return
     */
    SqlAndParams add(Object obj);

    /**
     * 查询单条记录
     * @return
     */
    SqlAndParams find(Class<?> clazz, LinkedHashMap<String, Object> params);

    /**
     * 查询数据条数
     * @param clazz
     * @param params
     * @return
     */
    SqlAndParams count(Class<?> clazz, LinkedHashMap<String, Object> params);
}
