package fun.asgc.neutrino.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ Target(ElementType.FIELD)  注解表示该注解可以应用于字段（Field）上。
 * @ Retention(RetentionPolicy.RUNTIME) 注解表示该注解在运行时（Runtime）仍然可用，可以通过反射来获取。
 * /

 /**
 *
 * @author: chenjunlin
 * @date: 2024/4/25
 */


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    String value() default "";
}
