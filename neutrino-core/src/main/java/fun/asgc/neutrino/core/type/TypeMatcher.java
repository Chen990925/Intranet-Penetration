  

package fun.asgc.neutrino.core.type;

/**
 * 值类型匹配器
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@FunctionalInterface
public interface TypeMatcher {

	/**
	 * 匹配
	 * @param clazz
	 * @param targetClass
	 * @return
	 */
	TypeMatchInfo match(Class<?> clazz, Class<?> targetClass);

}
