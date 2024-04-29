
package fun.asgc.neutrino.core.context;

import fun.asgc.neutrino.core.annotation.*;
import fun.asgc.neutrino.core.container.LifeCycle;
import fun.asgc.neutrino.core.container.BeanContainer;
import fun.asgc.neutrino.core.util.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 管理 Bean 的生命周期，包括实例化、初始化、依赖注入和销毁等操作
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@Slf4j
@Accessors(chain = true)
@Data
public class Bean implements LifeCycle {
	/**
	 * name: Bean 的名称。
	 * clazz: Bean 的类类型。
	 * instance: Bean 的实例。
	 * component: Bean 类上的 @Component 注解。
	 * order: Bean 的排序顺序。
	 * isBoot: 标志是否为启动时必须初始化的 Bean。
	 * isInit: 标志 Bean 是否已经初始化。
	 */
	private String name;
	private Class<?> clazz;
	private Object instance;
	private Component component;
	private int order;
	private boolean isBoot;
	private volatile boolean isInit;

	public boolean hasInstance() {
		return null != instance;
	}

	/**
	 * 实现 LifeCycle 接口中的初始化方法，用于初始化 Bean。
	 * 首先判断是否已经初始化和是否存在实例，然后执行标记了 @Init 注解的无参方法
	 */
	@Override
	public void init() {
		if (isInit) {
			return;
		}
		if (!hasInstance()) {
			return;
		}
		isInit = true;

		Set<Method> methods = ReflectUtil.getMethods(clazz);
		if (CollectionUtil.notEmpty(methods)) {
			for (Method method : methods) {
				if (method.isAnnotationPresent(Init.class) && method.getParameters().length == 0) {
					try {
						method.invoke(instance);
					} catch (Exception e) {
						log.error(String.format("初始化方法执行异常 class:%s method:%s", clazz.getName(), method.getName()), e);
					}
				}
			}
		}
	}

	@Override
	public void destroy() {
		if (!hasInstance()) {
			return;
		}
		Set<Method> methods = ReflectUtil.getMethods(clazz);
		if (CollectionUtil.notEmpty(methods)) {
			for (Method method : methods) {
				if (method.isAnnotationPresent(Destroy.class) && method.getParameters().length == 0) {
					try {
						method.invoke(instance);
					} catch (Exception e) {
						log.error(String.format("销毁方法执行异常 class:%s method:%s", clazz.getName(), method.getName()), e);
					}
				}
			}
		}
	}

	/**
	 * 创建 Bean 的实例，并进行注入和初始化。首先调用 newInstance0() 方法创建实例，然后进行依赖注入并调用 init() 方法进行初始化
	 * @param context
	 * @return
	 */
	public boolean newInstance(ApplicationContext context) {
		boolean result = newInstance0();
		if (result) {
			inject(context);
			init();
		}
		return result;
	}

	/**
	 * 实际创建 Bean 实例的方法。首先检查是否已经存在实例，然后使用双重检查锁定（Double-Check Locking）模式创建实例。
	 * 如果 Bean 类上标记了 @Configuration 注解，则通过 ConfigUtil.getYmlConfig(clazz) 方法从 YAML 配置文件中获取实例
	 * @return
	 */
	private boolean newInstance0() {
		return LockUtil.doubleCheckProcess(() -> !hasInstance(),
			this,
			() -> {
				try {
					// 暂时先只支持yml配置
					Configuration configuration = clazz.getAnnotation(Configuration.class);
					if (null != configuration) {
						instance = ConfigUtil.getYmlConfig(clazz);
					} else {
						// 由编码规避没有无参构造器的问题
						instance = clazz.newInstance();
					}
				} catch (Exception e) {
					// ignore
				}
			},
			() -> hasInstance()
		);
	}

	/**
	 * 对 Bean 进行依赖注入。首先获取类中标记了 @Bean 注解的方法，并执行这些方法获取额外的 Bean，并将其加入容器中。
	 * 然后遍历类中的字段，对标记了 @Autowired 注解的字段进行自动注入
	 * @param context
	 */
	private void inject(ApplicationContext context) {
		Set<Method> methods = ReflectUtil.getMethods(clazz);
		if (CollectionUtil.notEmpty(methods)) {
			methods.stream().forEach(method -> {
				fun.asgc.neutrino.core.annotation.Bean bean = method.getAnnotation(fun.asgc.neutrino.core.annotation.Bean.class);
				if (null == bean) {
					return;
				}
				String name = bean.value();
				if (StringUtil.isEmpty(name)) {
					name = method.getName();
				}
				// TODO 方法带参数的情况、class重复的问题，暂时由编码规避
				if (method.getParameters().length == 0) {
					try {
						Object obj = method.invoke(this.instance);
						if (null == obj) {
							log.error("bean 实例不能为空!");
							return;
						}
						context.getBeanContainer().addBean(new Bean()
							.setClazz(obj.getClass())
							.setBoot(false)
							.setComponent(null)
							.setInstance(obj)
							.setName(name)
							.setOrder(Integer.MAX_VALUE)
						);
					} catch (Exception e) {
						log.error(String.format("bean [%s] 实例化失败!", name), e);
					}
				}
			});
		}
		Set<Field> fieldSet = ReflectUtil.getInheritChainDeclaredFieldSet(clazz);
		if (CollectionUtil.notEmpty(fieldSet)) {
			fieldSet.forEach(field -> {
				Autowired autowired = field.getAnnotation(Autowired.class);
				if (null == autowired) {
					return;
				}
				if (field.getType() == Environment.class) {
					ReflectUtil.setFieldValue(field, instance, context.getEnvironment());
				} else if(field.getType() == ApplicationConfig.class) {
					ReflectUtil.setFieldValue(field, instance, context.getApplicationConfig());
				} else if(field.getType() == BeanContainer.class) {
					ReflectUtil.setFieldValue(field, instance, context.getBeanContainer());
				} else if (field.getType() == ApplicationContext.class) {
					ReflectUtil.setFieldValue(field, instance, context);
				} else {
					Class<?> autowiredType = field.getType();
					Bean autowiredBean = null;
					if (StringUtil.isEmpty(autowired.value())) {
						autowiredBean = context.getBeanContainer().getBean(autowiredType);
					} else {
						autowiredBean = context.getBeanContainer().getBean(autowired.value());
					}
					if (null == autowiredBean) {
						throw new RuntimeException(String.format("类 %s 自动装配字段:%s 依赖bean不存在!", clazz.getName(), field.getName()));
					}
					if (autowiredBean.isDependOn(clazz)) {
						throw new RuntimeException(String.format("类[%s]与类[%s]存在循环依赖!", clazz.getName(), field.getType().getName()));
					}
					autowiredBean.newInstance(context);
					ReflectUtil.setFieldValue(field, instance, autowiredBean.getInstance());
				}
			});
		}
	}

	/**
	 * 判断 Bean 是否为懒加载，通过判断类上是否标记了 @Lazy 注解实现
	 * @return
	 */
	public boolean isLazy() {
		Lazy lazy = ClassUtil.getAnnotation(clazz, Lazy.class);
		return null != lazy;
	}

	/**
	 * 判断 Bean 是否依赖于指定的目标类。通过遍历类的字段，判断是否存在依赖关系
	 * @param target
	 * @return
	 */
	private boolean isDependOn(Class<?> target) {
		if (null == target) {
			return false;
		}
		Set<Field> fieldSet = ReflectUtil.getInheritChainDeclaredFieldSet(clazz);
		if (CollectionUtil.isEmpty(fieldSet)) {
			return false;
		}
		for (Field field : fieldSet) {
			Autowired autowired = field.getAnnotation(Autowired.class);
			if (null == autowired) {
				continue;
			}
			if (field.getType() == target) {
				return true;
			}
		}
		return false;
	}
}
