package fun.asgc.neutrino.core.cache;

import fun.asgc.neutrino.core.util.LockUtil;

/**
 * cacheGroup：使用 MemoryCache 对象作为缓存容器，存储缓存组的名称和对应的缓存对象。
 * MemoryCacheGroup 是一个缓存分组管理器，用于管理多个 MemoryCache 实例
 * 实现了 CacheGroup<K,V> 接口，允许在不同的缓存分组中存储数据
 * @author: chenjunlin
 * @date: 2024/4/25
 */
public class MemoryCacheGroup<K,V> implements CacheGroup<K,V> {

	/**
	 * 缓存
	 */
	private final Cache<String,Cache<K,V>> cacheGroup = new MemoryCache<>();

	@Override
	public Cache<K, V> getCache(String group) {
		return cacheGroup.get(group);
	}

	@Override
	public void setCache(String group, Cache<K, V> cache) {
		cacheGroup.set(group, cache);
	}

	@Override
	public boolean containsCache(String group) {
		return cacheGroup.containsKey(group);
	}

	@Override
	public void set(String group, K k, V v) {
		LockUtil.doubleCheckProcess(() -> !cacheGroup.containsKey(group),
			group,
			() -> cacheGroup.set(group, new MemoryCache<>())
		);
		cacheGroup.get(group).set(k, v);
	}

	@Override
	public V get(String group, K k) {
		return (cacheGroup.containsKey(group) && cacheGroup.get(group).containsKey(k)) ? cacheGroup.get(group).get(k) : null;
	}

	@Override
	public boolean containsKey(String group, K k) {
		return cacheGroup.containsKey(group) && cacheGroup.get(group).containsKey(k);
	}

	@Override
	public boolean isEmpty(String group) {
		return !cacheGroup.containsKey(group) || cacheGroup.get(group).isEmpty();
	}
}
