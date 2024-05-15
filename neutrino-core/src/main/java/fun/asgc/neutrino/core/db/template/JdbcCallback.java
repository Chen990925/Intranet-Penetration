 
package fun.asgc.neutrino.core.db.template;

/**
 * @author: chenjunlin
 * @date: 2022/6/27
 */
public interface JdbcCallback<T> {

    /**
     * 执行
     * @return
     */
    T execute();

}
