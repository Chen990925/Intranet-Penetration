 
package fun.asgc.neutrino.core.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PrimaryKey
 * 在持久化模型中标注主键（可标注一个或多个）
 * @author: chenjunlin
 * @date: 2024/5/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {

}
