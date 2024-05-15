 
package fun.asgc.neutrino.core.db.template;

/**
 * @author: chenjunlin
 * @date: 2024/5/15
 */
public interface JdbcCallback<T> {

    /**
     * 执行
     * @return
     */
    T execute();

}
