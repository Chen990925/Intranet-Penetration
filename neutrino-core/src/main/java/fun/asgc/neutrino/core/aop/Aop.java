
package fun.asgc.neutrino.core.aop;

import fun.asgc.neutrino.core.aop.proxy.Proxy;
import fun.asgc.neutrino.core.aop.proxy.ProxyFactory;
import fun.asgc.neutrino.core.aop.proxy.ProxyStrategy;
import fun.asgc.neutrino.core.cache.Cache;
import fun.asgc.neutrino.core.cache.MemoryCache;
import fun.asgc.neutrino.core.util.Assert;
import fun.asgc.neutrino.core.util.LockUtil;

/**
 *
 * &#064;author:  chenjunlin
 * &#064;date:  2024/5/14
 */
public class Aop {
	private static final ProxyStrategy proxyStrategy = ProxyStrategy.SUB_CLASS_PROXY;
	private static final ProxyFactory proxyFactory = Proxy.getProxyFactory(proxyStrategy);
	private static final Cache<Class<?>, Object> proxyBeanCache = new MemoryCache<>();

	public static <T> T get(Class<T> clazz) {
		Assert.notNull(proxyFactory, "代理工厂初始化异常!");
		return (T)LockUtil.doubleCheckProcess(() -> !proxyBeanCache.containsKey(clazz),
				clazz,
				() -> proxyBeanCache.set(clazz, proxyFactory.get(clazz)),
				() -> proxyBeanCache.get(clazz)
		);
	}
}
