
package fun.asgc.neutrino.core.util;

import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

/**
 *
 * @author: chenjunlin
 * @date: 2022/6/20
 */
public class LockUtilTest {
	private static volatile A a;

	/**
	 * 没有双重校验锁，并发执行容易产生多个实例
	 */
	@Test
	public void test1() {
		for (int i = 0; i < 10; i++) {
			ThreadUtil.run(() -> {
				if (a == null) {
					a = new A();
				}
			});
		}
		SystemUtil.waitProcessDestroy().sync();
	}

	/**
	 * 使用双重校验锁，并发执行，只产生一个实例
	 */
	@Test
	public void test2() {
		for (int i = 0; i < 10; i++) {
			ThreadUtil.run(() -> {
				LockUtil.doubleCheckProcess(() -> a == null,
					LockUtilTest.class,
					() -> a = new A());
			});
		}
		SystemUtil.waitProcessDestroy().sync();
	}

	@Data
	public static class A {
		public A() {
			System.out.println("new instance.");
		}
	}
}
