  

package fun.asgc.neutrino.proxy.core;

import lombok.Data;

import java.util.List;

/**
 * 配置信息
 * @author: chenjunlin
 * @date: 2024/5/1
 */
@Data
public class ProxyClientConfig {
	private String environment;
	private String clientKey;
	private List<Proxy> proxy;

	@Data
	public static class Proxy {
		private Integer serverPort;
		private String clientInfo;
	}
}
