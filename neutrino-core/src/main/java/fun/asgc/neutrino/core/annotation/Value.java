package fun.asgc.neutrino.core.annotation;

import java.lang.annotation.*;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
	String value() default "";

}
