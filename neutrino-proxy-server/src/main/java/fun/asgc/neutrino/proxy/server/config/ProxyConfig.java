 

package fun.asgc.neutrino.proxy.server.config;

import fun.asgc.neutrino.core.annotation.Configuration;
import fun.asgc.neutrino.core.annotation.Value;
import lombok.Data;

import java.util.Map;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@Data
@Configuration(prefix = "proxy")
public class ProxyConfig {
	private Protocol protocol;
	private Server server;
	@Value("license")
	private Map<String, Integer> licenseMap;

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
	public static class Server {
		@Value("port")
		private Integer port;
		@Value("ssl-port")
		private Integer sslPort;
		@Value("key-store-password")
		private String keyStorePassword;
		@Value("key-manager-password")
		private String keyManagerPassword;
		@Value("jks-path")
		private String jksPath;
	}

}
