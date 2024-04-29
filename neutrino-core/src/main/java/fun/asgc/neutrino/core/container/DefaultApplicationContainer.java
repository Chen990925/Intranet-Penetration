package fun.asgc.neutrino.core.container;

import fun.asgc.neutrino.core.context.ApplicationContext;
import fun.asgc.neutrino.core.context.Environment;
import fun.asgc.neutrino.core.exception.ContainerInitException;
import fun.asgc.neutrino.core.runner.ApplicationRunner;
import fun.asgc.neutrino.core.util.BeanManager;
import lombok.extern.slf4j.Slf4j;

/**
 *	默认的应用容器实现，用于初始化和管理应用程序的组件和资源
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@Slf4j
public class DefaultApplicationContainer implements ApplicationContainer {
	//environment: 应用程序的环境信息，包括配置等。
	private Environment environment;
	//beanContainer: Bean 容器，用于管理应用程序中的所有 Bean。
	private BeanContainer beanContainer;
	//context: 应用程序的上下文信息，包括环境、Bean 容器和应用配置等
	private ApplicationContext context;

	/**
	 * 构造方法接收一个 Environment 对象作为参数，用于初始化应用容器。在构造方法中调用了 init() 方法进行初始化操作
	 * @param environment
	 */
	public DefaultApplicationContainer(Environment environment) {
		this.environment = environment;
		this.init();
	}

	/**
	 * 在 init() 方法中，首先创建了 beanContainer 对象，然后创建了 context 对象，并设置了环境、Bean 容器和应用配置等信息。
	 * 使用 BeanManager.setContext(this.context) 方法将上下文信息设置到 BeanManager 中。
	 * 遍历了 beanContainer 中的所有 Bean，并根据其是否是启动 Bean 或者是否是懒加载的，进行实例化操作。
	 * 最后输出日志，表示应用容器初始化完成
	 * @throws ContainerInitException
	 */
	@Override
	public void init() throws ContainerInitException {
		this.beanContainer = new DefaultBeanContainer(environment);
		this.context = new ApplicationContext()
			.setEnvironment(environment)
			.setBeanContainer(beanContainer)
			.setApplicationConfig(environment.getConfig());
		BeanManager.setContext(this.context);
		this.beanContainer.beanList().forEach(bean -> {
			if (bean.isBoot() || !bean.isLazy()) {
				bean.newInstance(context);
			}
		});
		log.info("应用容器初始化完成");

		this.run();
	}

	/**
	 * run() 方法遍历了 beanContainer 中的所有 Bean，如果某个 Bean 是启动 Bean，则检查该 Bean 是否已经实例化，并且是否是 ApplicationRunner 的子类。
	 * 如果是 ApplicationRunner 的子类，则调用其 run() 方法，并传入应用程序的主参数
	 */
	private void run() {
		this.beanContainer.beanList().forEach(bean -> {
			if (bean.isBoot()) {
				if (bean.hasInstance()) {
					if (ApplicationRunner.class.isAssignableFrom(bean.getClazz())) {
						ApplicationRunner runner = (ApplicationRunner)bean.getInstance();
						runner.run(environment.getMainArgs());
					}
				}
			}
		});
	}

	/**
	 * destroy() 方法销毁了 beanContainer 中的所有 Bean，并依次调用每个 Bean 的 destroy() 方法。
	 * 最后输出日志，表示应用容器销毁完成
	 */
	@Override
	public void destroy() {
		this.beanContainer.destroy();
		this.beanContainer.beanList().forEach(bean -> bean.destroy());
		log.info("应用容器销毁");
	}
}
