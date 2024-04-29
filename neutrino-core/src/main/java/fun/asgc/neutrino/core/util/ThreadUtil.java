  

package fun.asgc.neutrino.core.util;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public class ThreadUtil {

	/**
	 * 执行一个线程
	 * @param runnable
	 */
	public static void run(Runnable runnable) {
		new Thread(runnable).start();
	}

}
