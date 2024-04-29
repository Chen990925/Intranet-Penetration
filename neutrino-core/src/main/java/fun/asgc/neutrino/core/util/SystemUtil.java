  

package fun.asgc.neutrino.core.util;

import fun.asgc.neutrino.core.base.CodeBlock;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public class SystemUtil {

	public static void addShutdownHook(Runnable runnable) {
		Runtime.getRuntime().addShutdownHook(new Thread(runnable));
	}

	/**
	 * 等待进程销毁
	 * @return
	 */
	public static RunContext waitProcessDestroy() {
		return waitProcessDestroy(null);
	}

	/**
	 * 等待进程销毁
	 * @param destroy
	 * @return
	 */
	public static RunContext waitProcessDestroy(CodeBlock destroy) {
		RunContext context = new RunContext();
		SystemUtil.addShutdownHook(() -> {
			synchronized (context) {
				if (null != destroy) {
					destroy.execute();
				}
				context.stop();
				context.notify();
			}
		});
		return context;
	}


	public static class RunContext {
		private volatile  boolean running = true;

		public void sync() {
			synchronized (this) {
				while (running) {
					try {
						this.wait();
					} catch (Exception e) {
						// ignore
					}
				}
			}
		}

		private void stop() {
			this.running = false;
		}

	}
}
