 
package fun.asgc.neutrino.core.aop.proxy;

/**
 * @author: chenjunlin
 * @date: 2024/5/14
 */
public interface ProxyFactory {

    /**
     * 获取一个类的代理实例
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T get(Class<T> clazz);

    /**
     * 是否能被代理
     * @param clazz
     * @return
     */
    boolean canProxy(Class<?> clazz);
}
