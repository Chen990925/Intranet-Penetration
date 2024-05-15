 
package fun.asgc.neutrino.core.aop.interceptor;

import java.lang.reflect.Method;

/**
 * 结果处理
 * @author:  chenjunlin
 * @date: 2024/5/15
 */
public interface ResultAdvice {

    /**
     * 结果处理
     * @param targetClass
     * @param targetMethod
     * @param result
     * @return
     */
    default Object advice(Class<?> targetClass, Method targetMethod, Object result) {
        return result;
    }

}
