  

package fun.asgc.neutrino.core.util;

import lombok.Data;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@Data
public class Pair<K,V> {

	/**
	 * 第一个值
	 */
	private K first;

	/**
	 * 第二个值
	 */
	private V second;

	public static <K,V> Pair<K,V> of(K first, V second) {
		Pair<K,V> pair = new Pair<>();
		pair.setFirst(first);
		pair.setSecond(second);
		return pair;
	}
}
