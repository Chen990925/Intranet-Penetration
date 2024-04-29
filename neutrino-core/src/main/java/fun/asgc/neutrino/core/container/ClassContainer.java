  

package fun.asgc.neutrino.core.container;

import java.util.Set;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public interface ClassContainer extends Container {

	/**
	 * 判断指定类是否在容器中
	 * @param clazz
	 * @return
	 */
	boolean hasClass(Class<?> clazz);

	/**
	 * 获取容器中所有的类
	 * @return
	 */
	Set<Class<?>> getClasses();

	/**
	 * 获取容器中所有的组件
	 * @return
	 */
	Set<Class<?>> getComponentClasses();

}
