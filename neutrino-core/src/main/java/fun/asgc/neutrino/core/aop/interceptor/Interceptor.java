
package fun.asgc.neutrino.core.aop.interceptor;

import fun.asgc.neutrino.core.aop.Invocation;

/**
 * @author: chenjunlin
 * @date: 2024/5/14
 */
public interface Interceptor {
    /**
     * 拦截方法
     * @param inv
     */
    void intercept(Invocation inv);
}
