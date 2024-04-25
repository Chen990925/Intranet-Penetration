package fun.asgc.neutrino.core.annotation;

import java.lang.annotation.*;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface Configuration {

	/**
	 * 文件
	 * @return
	 */
	String file() default "";

	/**
	 * 前缀
	 * @return
	 */
	String prefix() default "";

}
