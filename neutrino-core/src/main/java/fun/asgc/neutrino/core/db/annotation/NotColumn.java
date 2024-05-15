 
package fun.asgc.neutrino.core.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 在持久化模型中标注不需要持久化的字段
 * @author: chenjunlin
 * @date: 2024/5/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotColumn {

}
