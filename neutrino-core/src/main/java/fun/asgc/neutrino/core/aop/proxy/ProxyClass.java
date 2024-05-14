 
package fun.asgc.neutrino.core.aop.proxy;

import lombok.Data;

import java.util.Map;

/**
 *
 * @author: 代理类
 * @date: 2024/5/14
 */
@Data
public class ProxyClass {
    /**
     * 被代理的目标
     */
    private Class<?> target;
    /**
     * 包名
     */
    private String pkg;
    /**
     * 类名
     */
    private String name;
    /**
     * 源代码
     */
    private String sourceCode;
    /**
     * 字节码
     */
    private Map<String, byte[]> byteCode;
    /**
     * 字节码被加载后的代理类
     */
    private Class<?> clazz;

    public ProxyClass(Class<?> target) {
        this.target = target;
        this.pkg = target.getPackage().getName();
    }
}
