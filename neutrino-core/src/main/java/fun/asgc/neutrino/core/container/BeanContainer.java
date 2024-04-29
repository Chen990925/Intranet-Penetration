
package fun.asgc.neutrino.core.container;

import fun.asgc.neutrino.core.context.Bean;

import java.util.List;

/**
 *	容器模板
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public interface BeanContainer extends Container {

	/**
	 * 是否存在bean实例
	 * @param clazz
	 * @return
	 */
	boolean hasBean(Class<?> clazz);

	/**
	 * 是否存在bean实例
	 * @param name
	 * @return
	 */
	boolean hasBean(String name);

	/**
	 * 获取bean实例
	 * @param clazz
	 * @return
	 */
	Bean getBean(Class<?> clazz);

	/**
	 * 获取bean实例
	 * @param name
	 * @return
	 */
	Bean getBean(String name);

	/**
	 * 获取bean集合
	 * @return
	 */
	List<Bean> beanList();

	/**
	 * 添加bean
	 * @param bean
	 */
	void addBean(Bean bean);
}
