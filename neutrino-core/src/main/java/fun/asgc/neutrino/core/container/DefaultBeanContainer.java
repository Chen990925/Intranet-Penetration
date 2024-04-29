
package fun.asgc.neutrino.core.container;

import com.google.common.collect.Lists;
import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.annotation.Order;
import fun.asgc.neutrino.core.context.Bean;
import fun.asgc.neutrino.core.cache.Cache;
import fun.asgc.neutrino.core.cache.MemoryCache;
import fun.asgc.neutrino.core.context.Environment;
import fun.asgc.neutrino.core.exception.ContainerInitException;
import fun.asgc.neutrino.core.runner.ApplicationRunner;
import fun.asgc.neutrino.core.util.ClassUtil;
import fun.asgc.neutrino.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 默认的 Bean 容器实现，用于管理应用程序中的所有 Bean
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@Slf4j
public class DefaultBeanContainer implements BeanContainer {
	private Environment environment;
	//classContainer: 类容器，用于获取应用程序中的所有类。
	private ClassContainer classContainer;
	private Cache<String, Bean> nameBeanCache;
	private Cache<Class<?>, Bean> classBeanCache;
	private List<Bean> beans;

	/**
	 * 构造方法接收一个 Environment 对象作为参数，用于初始化 Bean 容器。在构造方法中初始化了
	 * nameBeanCache、classBeanCache 和 beans，然后调用 init() 方法进行初始化操作
	 * @throws ContainerInitException
	 */
	public DefaultBeanContainer(Environment environment) {
		//environment: 应用程序的环境信息。
		this.environment = environment;
		//nameBeanCache: 名称到 Bean 的缓存，用于快速通过名称查找 Bean。
		this.nameBeanCache = new MemoryCache<>();
		//classBeanCache: 类到 Bean 的缓存，用于快速通过类查找 Bean。
		this.classBeanCache = new MemoryCache<>();
		//beans: 存储所有 Bean 的列表
		this.beans = new ArrayList<>();
		this.init();
	}

	@Override
	public boolean hasBean(Class<?> clazz) {
		return classBeanCache.containsKey(clazz);
	}

	@Override
	public boolean hasBean(String name) {
		return nameBeanCache.containsKey(name);
	}

	@Override
	public Bean getBean(Class<?> clazz) {
		return classBeanCache.get(clazz);
	}

	@Override
	public Bean getBean(String name) {
		return nameBeanCache.get(name);
	}


	/**
	 * 在 init() 方法中，首先创建了 classContainer 对象，然后通过 classContainer 获取应用程序中的所有组件类。
	 * 遍历组件类，对每个组件类创建一个对应的 Bean，并根据 Component 注解中的值设置 Bean 的名称，根据 Order 注解中的值设置 Bean 的排序。
	 * 将创建的 Bean 添加到 classBeanCache 和 nameBeanCache 中，并将所有 Bean 添加到 beans 列表中。
	 * 最后输出日志，表示 Bean 容器初始化完成
	 * @throws ContainerInitException
	 */
	@Override
	public void init() throws ContainerInitException {
		this.classContainer = new DefaultClassContainer(environment);
		this.classContainer.getComponentClasses().forEach(clazz -> {
			String name = clazz.getName();
			Component component = ClassUtil.getAnnotation(clazz, Component.class);
			if (null != component && StringUtil.notEmpty(component.value())) {
				name = component.value();
			}
			Order order = ClassUtil.getAnnotation(clazz, Order.class);
			int orderValue = Integer.MAX_VALUE;
			if (null != order) {
				orderValue = order.value();
			}
			addBean(new Bean()
				.setClazz(clazz)
				.setName(name)
				.setComponent(component)
				.setOrder(orderValue)
			);

		});
		if (!classBeanCache.isEmpty()) {
			beans = Lists.newArrayList(classBeanCache.values());
			Collections.sort(beans, Comparator.comparingInt(Bean::getOrder));
		}
		log.info("bean容器初始化完成");
	}

	/**
	 * destroy() 方法销毁了 classContainer，并输出日志表示 Bean 容器销毁完成
	 */
	@Override
	public void destroy() {
		this.classContainer.destroy();
		log.info("bean容器销毁");
	}

	/**
	 * 添加 Bean 到容器中
	 * @param bean
	 */
	@Override
	public void addBean(Bean bean) {
		if (null == bean) {
			return;
		}
		// TODO 暂时由编码时规避名字冲突
		if (ApplicationRunner.class.isAssignableFrom(bean.getClazz())) {
			bean.setOrder(Integer.MIN_VALUE);
			bean.setBoot(true);
		}
		classBeanCache.set(bean.getClazz(), bean);
		nameBeanCache.set(bean.getName(), bean);
	}

	@Override
	public List<Bean> beanList() {
		return beans;
	}
}
