 
package fun.asgc.neutrino.core.aop;

import fun.asgc.neutrino.core.aop.interceptor.Interceptor;
import fun.asgc.neutrino.core.aop.interceptor.InterceptorFactory;
import fun.asgc.neutrino.core.aop.proxy.ProxyCache;
import fun.asgc.neutrino.core.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
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
	private List<Interceptor> interceptors;
	private volatile int index = 0;
	private Object returnValue;

	public Invocation(Long methodId, Object proxy, Supplier callback, Object... args) {
		this.targetMethod = ProxyCache.getMethod(methodId);
		this.targetClass = this.targetMethod.getDeclaringClass();
		this.proxy = proxy;
		this.callback = callback;
		this.args = args;
		this.interceptors = InterceptorFactory.getListByTargetMethod(this.targetMethod);
	}

	public void invoke() {
		if (CollectionUtil.notEmpty(this.interceptors) && index < this.interceptors.size()) {
			this.interceptors.get(index++).intercept(this);
		} else {
			returnValue = callback.get();
		}
	}

	public <T> T getReturnValue() {
		return (T)returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
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
