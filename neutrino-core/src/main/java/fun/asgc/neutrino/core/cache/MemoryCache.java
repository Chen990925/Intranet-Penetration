
package fun.asgc.neutrino.core.cache;

import java.util.*;

/**
 * MemoryCache 是一个通用的内存缓存类，用于存储键值对形式的数据
 * @author: chenjunlin
 * @date: 2024/4/25
 */
public class MemoryCache<K,V> implements Cache<K,V> {

	/**
	* 使用 Collections.synchronizedMap 方法创建了一个线程安全的 HashMap 实例，并将其赋值给 cache 字段
	 */
	private Map<K,V> cache = Collections.synchronizedMap(new HashMap<>());

	@Override
	public void set(K k, V v) {
		cache.put(k, v);
	}

	@Override
	public V get(K k) {IdentityHashMap a = new IdentityHashMap();
		return (V)cache.get(k);
	}

	@Override
	public boolean containsKey(K k) {
		return cache.containsKey(k);
	}

	@Override
	public boolean containsValue(V v) {
		return cache.containsValue(v);
	}

	@Override
	public Set<K> keySet() {
		return cache.keySet();
	}

	@Override
	public Collection<V> values() {
		return cache.values();
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public V isOnePeek() {
		if (size() != 1) {
			return null;
		}
 		return cache.values().stream().findFirst().get();
	}
}
