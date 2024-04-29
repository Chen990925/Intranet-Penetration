  

package fun.asgc.neutrino.core.base;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/25
 */
public interface Handler<Context, Data> {

	/**
	 * 处理
	 * @param context
	 * @param data
	 */
	void handle(Context context, Data data);

	/**
	 * 名称
	 * @return
	 */
	default String name() {
		return "";
	};
}
