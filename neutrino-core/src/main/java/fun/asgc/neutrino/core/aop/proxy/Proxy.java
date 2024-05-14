 
package fun.asgc.neutrino.core.aop.proxy;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/14
 */
public class Proxy {
    private static final ProxyFactory subClassProxyFactory = new SubClassProxyFactory();

    public static ProxyFactory getProxyFactory(ProxyStrategy strategy) {
        switch (strategy) {
            case SUB_CLASS_PROXY: return subClassProxyFactory;
            default: return null;
        }
    }

}
