package fun.asgc.neutrino.core.cache;

import java.util.Collection;
import java.util.Set;

/**
 * 缓存类模板
 * @author: chenjunlin
 * @date: 2024/4/25
 */
public interface Cache<K,V> {

	/**
	 * 设置缓存
	 * @param k
	 * @param v
	 */
	void set(K k, V v);

	/**
	 * 获取缓存
	 * @param k
	 * @return
	 */
	V get(K k);

	/**
	 * 判断缓存是否存在
	 * @param k
	 * @return
	 */
	boolean containsKey(K k);

	/**
	 * 判断缓存值是否存在
	 * @param v
	 * @return
	 */
	boolean containsValue(V v);

	/**
	 * key集合
	 * @return
	 */
	Set<K> keySet();

	/**
	 * 值集合
	 * @return
	 */
	Collection<V> values();

	/**
	 * 是否为空
	 * @return
	 */
	boolean isEmpty();

	/**
	 * 清空缓存
	 */
	void clear();

	/**
	 * 缓存大小
	 * @return
	 */
	int size();

	/**
	 * 如果有且只有一个缓存，则返回该缓存值，否则返回空
	 * @return
	 */
	V isOnePeek();
}
