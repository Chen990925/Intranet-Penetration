 
package fun.asgc.neutrino.core.aop;

import fun.asgc.neutrino.core.aop.proxy.ProxyCache;
import fun.asgc.neutrino.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/14
 */
@Slf4j
public class Invocation {
	private Class<?> targetClass;
	private Method targetMethod;
	private Object proxy;
	private Supplier callback;
	private Object[] args;
	private Interceptor[] interceptors;
	private volatile int index = 0;
	private Object returnValue;

	public Invocation(Long methodId, Object proxy, Supplier callback, Object... args) {
		this.targetMethod = ProxyCache.getMethod(methodId);
		this.targetClass = this.targetMethod.getDeclaringClass();
		this.proxy = proxy;
		this.callback = callback;
		this.args = args;
		this.interceptors = getInterceptors(this.targetMethod);
	}

	/**
	 * TODO 先简单实现为 newInstance
	 * @param targetMethod
	 * @return
	 */
	public static Interceptor[] getInterceptors(Method targetMethod) {
		if (null == targetMethod) {
			return null;
		}
		Intercept intercept = targetMethod.getAnnotation(Intercept.class);
		if (null == intercept) {
			intercept = targetMethod.getDeclaringClass().getAnnotation(Intercept.class);
		}
		if (null == intercept) {
			return null;
		}
		Class<? extends Interceptor>[] classes = intercept.value();
		if (ArrayUtil.isEmpty(classes)) {
			return null;
		}
		Interceptor[] interceptors = new Interceptor[classes.length];
		for (int i = 0; i < classes.length; i++) {
			try {
				interceptors[i] = classes[i].newInstance();
			} catch (Exception e) {
				// ignore
			}
		}
		return interceptors;
	}

	public void invoke() {
		if (ArrayUtil.notEmpty(this.interceptors) && index < this.interceptors.length) {
			this.interceptors[index++].intercept(this);
		} else {
			returnValue = callback.get();
		}
	}

	public <T> T getReturnValue() {
		return (T)returnValue;
	}


	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}

	public Object[] getArgs() {
		return args;
	}
}
