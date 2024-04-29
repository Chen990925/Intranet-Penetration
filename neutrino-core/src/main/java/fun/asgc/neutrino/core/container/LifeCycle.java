  

package fun.asgc.neutrino.core.container;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public interface LifeCycle {

	/**
	 * 初始化
	 */
	void init();

	/**
	 * 销毁
	 */
	void destroy();
}
