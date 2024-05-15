 
package fun.asgc.neutrino.core.aop;

import fun.asgc.neutrino.core.aop.interceptor.Interceptor;

import java.lang.annotation.*;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/14
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Intercept {
	Class<? extends Interceptor>[] value() default {};
	Class<? extends Interceptor>[] exclude() default {};
}
