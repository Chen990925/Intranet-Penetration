 
package fun.asgc.neutrino.core.aop.proxy;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 代理缓存
 * @author: chenjunlin
 * @date: 2024/5/14
 */
public class ProxyCache {
    private static final AtomicLong methodId = new AtomicLong();
    private static final Map<Long, Method> methodCache = Collections.synchronizedMap(new HashMap<>());

    public static Long generateMethodId() {
        return methodId.incrementAndGet();
    }

    public static Long setMethod(Method method) {
        Long id = generateMethodId();
        methodCache.put(id, method);
        return id;
    }

    public static Method getMethod(Long id) {
        return methodCache.get(id);
    }
}
