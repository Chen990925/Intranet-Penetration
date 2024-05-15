
package fun.asgc.neutrino.core.aop.interceptor;

import java.lang.reflect.Method;

/**
 * 过滤器
 * @author:  chenjunlin
 * @date: 2022/6/27
 */
public interface Filter {

    /**
     * 是否过滤
     * @param targetClass
     * @param targetMethod
     * @param args
     * @return
     */
    default boolean filtration(Class<?> targetClass, Method targetMethod, Object[] args) {
        return false;
    }

}
