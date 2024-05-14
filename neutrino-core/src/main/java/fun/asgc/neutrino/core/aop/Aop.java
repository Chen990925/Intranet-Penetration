
package fun.asgc.neutrino.core.aop;

import fun.asgc.neutrino.core.aop.proxy.Proxy;
import fun.asgc.neutrino.core.aop.proxy.ProxyFactory;
import fun.asgc.neutrino.core.aop.proxy.ProxyStrategy;
import fun.asgc.neutrino.core.util.Assert;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/14
 */
public class Aop {
	private static final ProxyStrategy proxyStrategy = ProxyStrategy.SUB_CLASS_PROXY;
	private static final ProxyFactory proxyFactory = Proxy.getProxyFactory(proxyStrategy);

	public static <T> T get(Class<T> clazz) {
		Assert.notNull(proxyFactory, "代理工厂初始化异常!");
		return proxyFactory.get(clazz);
	}
}
