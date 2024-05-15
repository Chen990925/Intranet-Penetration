 
package fun.asgc.neutrino.core.aop.interceptor;

import fun.asgc.neutrino.core.aop.Intercept;
import fun.asgc.neutrino.core.cache.Cache;
import fun.asgc.neutrino.core.cache.MemoryCache;
import fun.asgc.neutrino.core.util.ArrayUtil;
import fun.asgc.neutrino.core.util.Assert;
import fun.asgc.neutrino.core.util.LockUtil;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 拦截器工厂
 * @author:  chenjunlin
 * @date: 2022/6/24
 */
public class InterceptorFactory {
    private static final Cache<Class<? extends Interceptor>, Interceptor> interceptorCache = new MemoryCache<>();
    private static final List<Interceptor> globalInterceptorList = Collections.synchronizedList(new ArrayList<>());
    private static final Map<Method, List<Interceptor>> methodInterceptorListMap = new HashMap<>();

    static {
        registerGlobalInterceptor(InnerGlobalInterceptor.class);
    }

    public static <T extends Interceptor> T get(Class<T> clazz) {
        return (T)LockUtil.doubleCheckProcess(() -> !interceptorCache.containsKey(clazz),
                clazz,
                () -> {
                    try {
                        interceptorCache.set(clazz, clazz.newInstance());
                    } catch (InstantiationException|IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> interceptorCache.get(clazz)
        );
    }

    public static List<Interceptor> getListByTargetMethod(Method targetMethod) {
        Assert.notNull(targetMethod, "目标方法不能为空！");

        return LockUtil.doubleCheckProcess(() -> !methodInterceptorListMap.containsKey(targetMethod),
                targetMethod,
                () -> {
                    List<Interceptor> interceptors = new ArrayList<>();
                    interceptors.addAll(globalInterceptorList);
                    addInterceptorByAnnotation(interceptors, targetMethod.getDeclaringClass().getAnnotation(Intercept.class));
                    addInterceptorByAnnotation(interceptors, targetMethod.getAnnotation(Intercept.class));
                    methodInterceptorListMap.put(targetMethod, interceptors);
                },
                () -> methodInterceptorListMap.get(targetMethod));
    }

    private static void addInterceptorByAnnotation(List<Interceptor> interceptors, Intercept intercept) {
        if (null == interceptors || null == intercept) {
            return;
        }
        if (ArrayUtil.notEmpty(intercept.value())) {
            for (Class<? extends Interceptor> clazz : intercept.value()) {
                Interceptor interceptor = get(clazz);
                if (null != interceptor && !interceptors.contains(interceptor)) {
                    interceptors.add(interceptor);
                }
            }
        }
        if (ArrayUtil.notEmpty(intercept.exclude())) {
            for (Class<? extends Interceptor> clazz : intercept.exclude()) {
                Interceptor interceptor = get(clazz);
                if (null != interceptor && interceptors.contains(interceptor)) {
                    interceptors.remove(interceptor);
                }
            }
        }
    }

    /**
     * 注册拦截器 - 全局
     * @param clazz
     */
    public static synchronized void registerGlobalInterceptor(Class<? extends  Interceptor> clazz) {
        Assert.notNull(clazz, "class不能为空!");
        Interceptor interceptor = get(clazz);
        if (!globalInterceptorList.contains(interceptor)) {
            globalInterceptorList.add(interceptor);
        }
    }
}
