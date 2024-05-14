 
package fun.asgc.neutrino.core.aop.proxy;

/**
 * 代理策略
 * @author: chenjunlin
 * @date: 2024/5/14
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProxyStrategy {
    JDK_DYNAMIC_PROXY(1, "JDK动态代理"),
    SUB_CLASS_PROXY(2, "子类代理");

    private Integer strategy;
    private String desc;
}
