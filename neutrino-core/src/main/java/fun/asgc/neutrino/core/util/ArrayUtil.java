
package fun.asgc.neutrino.core.util;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public class ArrayUtil {

	/**
	 * 判断数组是否为空
	 * @param arr
	 * @return
	 */
	public static boolean isEmpty(Object[] arr) {
		return null == arr || arr.length == 0;
	}

	/**
	 * 判断数组是否非空
	 * @param arr
	 * @return
	 */
	public static boolean notEmpty(Object[] arr) {
		return !isEmpty(arr);
	}
}
