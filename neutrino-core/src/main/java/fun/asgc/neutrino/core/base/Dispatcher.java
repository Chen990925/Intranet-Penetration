  

package fun.asgc.neutrino.core.base;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/25
 */
public interface Dispatcher<Context, Data> {

	/**
	 * 调度
	 * @param context
	 * @param data
	 */
	void dispatch(Context context, Data data);
}
