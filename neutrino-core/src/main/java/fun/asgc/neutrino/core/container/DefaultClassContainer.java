
package fun.asgc.neutrino.core.container;

import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.context.Environment;
import fun.asgc.neutrino.core.exception.ContainerInitException;
import fun.asgc.neutrino.core.util.ClassUtil;
import fun.asgc.neutrino.core.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认的类容器实现，用于扫描和管理应用程序中的所有类
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@Slf4j
public class DefaultClassContainer implements ClassContainer {

	//environment: 应用程序的环境信息。
	private Environment environment;
	//classes: 存储所有类的集合。
	private Set<Class<?>> classes;
	//componentClasses: 存储被 @Component 注解标记的类的集合
	private Set<Class<?>> componentClasses;

	/**
	 *  构造方法接收一个 Environment 对象作为参数，用于初始化类容器。
	 *  在构造方法中初始化了 classes 和 componentClasses，然后调用 init() 方法进行初始化操作
	 * @param environment
	 */
	public DefaultClassContainer(Environment environment) {
		this.environment = environment;
		this.classes = Collections.synchronizedSet(new HashSet<>());
		this.componentClasses = Collections.synchronizedSet(new HashSet<>());
		this.init();
	}

	/**
	 * 在 init() 方法中，首先判断是否存在需要扫描的基础包路径，如果没有则直接返回。
	 * 调用 ClassUtil.scan() 方法扫描基础包路径下的所有类，并将扫描到的类存储到 classes 中。
	 * 通过流操作，过滤出被 @Component 注解标记的类，并将它们存储到 componentClasses 中
	 * @throws ContainerInitException
	 */
	@Override
	public void init() throws ContainerInitException {
		try {
			if (CollectionUtil.isEmpty(environment.getScanBasePackages())) {
				return;
			}
			this.classes = ClassUtil.scan(environment.getScanBasePackages());
			this.componentClasses = this.classes.stream().filter(item -> ClassUtil.isAnnotateWith(item, Component.class)).collect(Collectors.toSet());
			log.info("class容器初始化完成");
		} catch (Exception e) {
			log.error("class容器初始化异常!");
			throw new ContainerInitException(this);
		}
	}

	@Override
	public boolean hasClass(Class<?> clazz) {
		return classes.contains(clazz);
	}

	/**
	 * 获取所有类的集合
	 * @return
	 */
	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	/**
	 * 获取被 @Component 注解标记的类的集合
	 * @return
	 */
	@Override
	public Set<Class<?>> getComponentClasses() {
		return componentClasses;
	}

	@Override
	public void destroy() {
		log.info("class容器销毁");
	}
}
