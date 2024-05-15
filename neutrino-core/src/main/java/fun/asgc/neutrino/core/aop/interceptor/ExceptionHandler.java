
package fun.asgc.neutrino.core.aop.interceptor;

/**
 * 异常处理器
 * @author: chenjunlin
 * @date: 2024/5/15
 */
public interface ExceptionHandler {

    /**
     * 是否支持处理该异常
     * @param e
     * @return
     */
    boolean support(Exception e);

    /**
     * 异常处理
     * @param e
     * @return
     */
    Object handle(Exception e);
}
