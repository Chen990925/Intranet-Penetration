package fun.asgc.neutrino.core.cache;

/**
 * Cache缓存组
 * @author: chenjunlin
 * @date: 2024/4/25
 */
public interface CacheGroup<K,V> {

	/**
	 * 获取缓存
	 * @param group
	 * @return
	 */
	Cache<K,V> getCache(String group);

	/**
	 * 设置缓存
	 * @param group
	 * @param cache
	 */
	void setCache(String group, Cache<K, V> cache);

	/**
	 * 判断缓存是否存在
	 * @param group
	 * @return
	 */
	boolean containsCache(String group);

	/**
	 * 设置缓存值
	 * @param group
	 * @param k
	 * @param v
	 */
	void set(String group, K k, V v);

	/**
	 * 获取缓存值
	 * @param group
	 * @param k
	 * @return
	 */
	V get(String group, K k);

	/**
	 * 判断缓存值是否存在
	 * @param group
	 * @param k
	 * @return
	 */
	boolean containsKey(String group, K k);

	/**
	 * 是否为空
	 * @param group
	 * @return
	 */
	boolean isEmpty(String group);

}
