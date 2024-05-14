package fun.asgc.neutrino.core.util;

import org.junit.Test;

/**
 *
 * @author: chenjunlin
 * @date: 2022/6/20
 */
public class SystemUtilTest {

	@Test
	public void run() {
		ThreadUtil.run(() -> {
			for (int i = 0; i < 10; i ++) {
				try {
					System.out.println(i);
					Thread.sleep(1000);
				} catch (Exception e) {

				}
			}
		});

		SystemUtil.waitProcessDestroy(() -> System.out.println("进程销毁")).sync();
	}

}
