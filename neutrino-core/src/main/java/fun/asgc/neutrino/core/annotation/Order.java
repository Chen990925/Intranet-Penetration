package fun.asgc.neutrino.core.annotation;

import java.lang.annotation.*;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Order {
	int value() default Integer.MAX_VALUE;
}
