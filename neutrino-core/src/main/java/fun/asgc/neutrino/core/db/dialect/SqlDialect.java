 
package fun.asgc.neutrino.core.db.dialect;

import java.util.Map;
import java.util.Set;

/**
 * sql方言，用于屏蔽各种不同的数据库的sql差异
 * @author: chenjunlin
 * @date: 2022/6/27
 */
public interface SqlDialect {
    /**
     * 获取记录数
     * @return
     */
    String getRecordCount();

    /**
     * 查询所有数据
     * @return
     */
    String findAll();

    /**
     * 根据id查询单条数据
     * @param id
     * @return
     */
    String findById(String id);

    /**
     * 查询
     * @param filterField
     * @param params
     * @return
     */
    String find(Set<String> filterField, Map<String,Object> params);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    String deleteById(String id);

    /**
     * 删除
     * @param filterField
     * @param params
     * @return
     */
    String delete(Set<String> filterField,Map<String,Object> params);

    /**
     * 删除所有数据
     * @return
     */
    String deleteAll();

    /**
     * 更新
     * @param filterField
     * @param params
     * @return
     */
    String update(Set<String> filterField,Map<String,Object> params);

    /**
     * 新增
     * @param filterField
     * @param params
     * @return
     */
    String create(Set<String> filterField,Map<String,Object> params);

    /**
     * 分页查询
     * @return
     */
    String findPage();

    /**
     * 分页查询
     * @param filterField
     * @param params
     * @return
     */
    String findPage(Set<String> filterField,Map<String,Object> params);

}
