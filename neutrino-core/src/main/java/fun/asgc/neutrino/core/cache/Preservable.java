package fun.asgc.neutrino.core.cache;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/25
 */
public interface Preservable {

	/**
	 * 保存到磁盘
	 */
	void save();

	/**
	 * 从磁盘加载
	 */
	void load();

}
