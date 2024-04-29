  
package fun.asgc.neutrino.core.type;

/**
 * 类型转换器
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@FunctionalInterface
public interface TypeConverter {
	/**
	 * 类型转换
	 * @param value
	 * @param targetType
	 * @return
	 */
	Object convert(Object value, Class<?> targetType);
}
