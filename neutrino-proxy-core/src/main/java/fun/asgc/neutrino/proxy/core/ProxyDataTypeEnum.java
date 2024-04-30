  

package fun.asgc.neutrino.proxy.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/1
 */
@Getter
@AllArgsConstructor
public enum ProxyDataTypeEnum {
	HEARTBEAT(0x01, Constants.ProxyDataTypeName.HEARTBEAT, "心跳"),
	AUTH(0x02, Constants.ProxyDataTypeName.AUTH,"认证"),
	CONNECT(0x03, Constants.ProxyDataTypeName.CONNECT,"连接"),
	DISCONNECT(0x04, Constants.ProxyDataTypeName.DISCONNECT,"断开连接"),
	TRANSFER(0x05, Constants.ProxyDataTypeName.TRANSFER,"数据传输"),
	ERROR(0x06, Constants.ProxyDataTypeName.ERROR,"异常");
	/**
	 * 将枚举类型 ProxyDataTypeEnum 的所有枚举值映射为一个整数到枚举值的映射，并存储在静态的 cache 字段中
	 */
	private static Map<Integer,ProxyDataTypeEnum> cache = Stream.of(values()).collect(Collectors.toMap(ProxyDataTypeEnum::getType, Function.identity()));

	private int type;
	private String name;
	private String desc;

	public static ProxyDataTypeEnum of(Integer type) {
		return cache.get(type);
	}
}
