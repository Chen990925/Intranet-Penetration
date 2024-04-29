
package fun.asgc.neutrino.core.exception;

import fun.asgc.neutrino.core.container.Container;

/**
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public class ContainerInitException extends RuntimeException {

	public ContainerInitException(Container container) {
		super(String.format("容器:[%s] 初始化异常", container.getClass().getName()));
	}

}
