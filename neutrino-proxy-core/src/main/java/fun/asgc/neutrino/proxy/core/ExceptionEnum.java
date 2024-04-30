package fun.asgc.neutrino.proxy.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/1
 */
@Getter
@AllArgsConstructor
public enum ExceptionEnum {

	AUTH_FAILED(1, "认证失败");

	private Integer code;
	private String msg;
}
