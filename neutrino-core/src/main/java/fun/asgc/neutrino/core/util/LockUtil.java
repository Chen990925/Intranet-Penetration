  

package fun.asgc.neutrino.core.util;

import fun.asgc.neutrino.core.base.CodeBlock;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public class LockUtil {

	/**
	 * 双重校验处理
	 * @param isLock
	 * @param lock
	 * @param lockProcess
	 */
	public static void doubleCheckProcess(BooleanSupplier isLock, Object lock, CodeBlock lockProcess) {
		if (isLock.getAsBoolean()) {
			synchronized (lock) {
				if (isLock.getAsBoolean()) {
					lockProcess.execute();
				}
			}
		}
	}

	/**
	 * 双重校验处理
	 * @param isLock
	 * @param lock
	 * @param lockProcess
	 * @param nonLockProcess
	 */
	public static <T> T doubleCheckProcess(BooleanSupplier isLock, Object lock, CodeBlock lockProcess, Supplier<T> nonLockProcess) {
		doubleCheckProcess(isLock, lock, lockProcess);
		return nonLockProcess.get();
	}

}
