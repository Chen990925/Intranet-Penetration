
package fun.asgc.neutrino.core.db.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author:  chenjunlin
 * @date: 2022/6/28
 */
public interface Dao<T> {

    //==============================================新增=====================

    /**
     * 新增单条记录
     * @param po
     * @return
     */
    T add(T po);

    //==============================================修改=====================

    /**
     * 更新单条记录
     * @param po
     * @return
     */
    int updateById(T po);

    /**
     * 更新单条记录
     * @param po
     * @param field
     * @return
     */
    int updateById(T po, String ...field);

    //==============================================删除=====================

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Serializable id);

    int delete(T po, String ...field);

    int delete();

    //==============================================查询=====================
    Long count();
    Long count(T po, String ...field);

    T findOneById(Serializable id);
    T findOne(T po, String ...field);
    List<T> find();
    List<T> find(T po, String ...field);

    List<T> findPage(int beginNo, int pageSize);
    List<T> findPage(T po,int beginNo, int pageSize, String ...field);
}

