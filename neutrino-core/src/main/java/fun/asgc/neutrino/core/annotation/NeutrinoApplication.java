package fun.asgc.neutrino.core.annotation;

import java.lang.annotation.*;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface NeutrinoApplication {
	String[] scanBasePackages() default {};
}
