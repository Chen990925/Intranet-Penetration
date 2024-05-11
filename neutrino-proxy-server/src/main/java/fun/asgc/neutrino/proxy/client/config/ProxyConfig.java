package fun.asgc.neutrino.proxy.client.config;

import fun.asgc.neutrino.core.annotation.Configuration;
import fun.asgc.neutrino.core.annotation.Init;
import fun.asgc.neutrino.core.annotation.Value;
import fun.asgc.neutrino.proxy.core.ProxyClientConfig;
import lombok.Data;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/9
 */
@Data
@Configuration(prefix = "proxy")
public class ProxyConfig {
	public static ProxyConfig instance;
	private Protocol protocol;
	private Client client;
	private ProxyClientConfig clientConfig;

	@Data
	public static class Protocol {
		@Value("max-frame-length")
		private Integer maxFrameLength;
		@Value("length-field-offset")
		private Integer lengthFieldOffset;
		@Value("length-field-length")
		private Integer lengthFieldLength;
		@Value("initial-bytes-to-strip")
		private Integer initialBytesToStrip;
		@Value("length-adjustment")
		private Integer lengthAdjustment;
		@Value("read-idle-time")
		private Integer readIdleTime;
		@Value("write-idle-time")
		private Integer writeIdleTime;
		@Value("all-idle-time-seconds")
		private Integer allIdleTimeSeconds;
	}

	@Data
	public static class Client {
		@Value("key-store-password")
		private String keyStorePassword;
		@Value("jks-path")
		private String jksPath;
		@Value("server-ip")
		private String serverIp;
		@Value("server-port")
		private Integer serverPort;
		@Value("ssl-enable")
		private Boolean sslEnable;
	}

	@Init
	public void init() {
		instance = this;
	}
}
