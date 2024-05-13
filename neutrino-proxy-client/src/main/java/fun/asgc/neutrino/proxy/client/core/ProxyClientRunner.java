 

package fun.asgc.neutrino.proxy.client.core;

import com.alibaba.fastjson.JSONObject;
import fun.asgc.neutrino.core.annotation.Autowired;
import fun.asgc.neutrino.core.annotation.Bean;
import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.runner.ApplicationRunner;
import fun.asgc.neutrino.core.util.CollectionUtil;
import fun.asgc.neutrino.core.util.FileUtil;
import fun.asgc.neutrino.core.util.StringUtil;
import fun.asgc.neutrino.proxy.client.config.ProxyConfig;
import fun.asgc.neutrino.proxy.client.util.ClientChannelMannager;
import fun.asgc.neutrino.proxy.core.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@Slf4j
@Component
public class ProxyClientRunner implements ApplicationRunner {
	@Autowired
	private ProxyConfig proxyConfig;
	@Autowired("bootstrap")
	private static Bootstrap bootstrap;
	@Autowired("realServerBootstrap")
	private static Bootstrap realServerBootstrap;
	private static NioEventLoopGroup workerGroup;

	@Override
	public void run(String[] args) {
		ProxyClientConfig clientConfig = getClientConfig(args.length >= 1 ? args[args.length - 1] : null);
		proxyConfig.setClientConfig(clientConfig);
		connectProxyServer();
	}

	/**
	 * 连接代理服务器
	 */
	private void connectProxyServer() {
		workerGroup = new NioEventLoopGroup();
		realServerBootstrap.group(workerGroup);
		realServerBootstrap.channel(NioSocketChannel.class);
		realServerBootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new RealServerChannelHandler());
			}
		});

		bootstrap.group(workerGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				if (proxyConfig.getClient().getSslEnable()) {
					ch.pipeline().addLast(createSslHandler());
				}

				ch.pipeline().addLast(new ProxyMessageDecoder(proxyConfig.getProtocol().getMaxFrameLength(),
					proxyConfig.getProtocol().getLengthFieldOffset(), proxyConfig.getProtocol().getLengthFieldLength(),
					proxyConfig.getProtocol().getLengthAdjustment(), proxyConfig.getProtocol().getInitialBytesToStrip()));
				ch.pipeline().addLast(new ProxyMessageEncoder());
				ch.pipeline().addLast(new IdleCheckHandler(proxyConfig.getProtocol().getReadIdleTime(), proxyConfig.getProtocol().getWriteIdleTime(), proxyConfig.getProtocol().getAllIdleTimeSeconds()));
				ch.pipeline().addLast(new ClientChannelHandler());
			}
		});
		bootstrap.connect(proxyConfig.getClient().getServerIp(), proxyConfig.getClient().getServerPort())
			.addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {

						// 连接成功，向服务器发送客户端认证信息（clientKey）
						ClientChannelMannager.setCmdChannel(future.channel());
						future.channel().writeAndFlush(ProxyMessage.buildAuthMessage(JSONObject.toJSONString(proxyConfig.getClientConfig())));
						log.info("连接代理服务成功.");
					} else {
						log.info("连接代理服务失败!");
					}
				}
			});
	}

	private ChannelHandler createSslHandler() {
		try {
			InputStream jksInputStream = FileUtil.getInputStream(proxyConfig.getClient().getJksPath());

			SSLContext clientContext = SSLContext.getInstance("TLS");
			final KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(jksInputStream, proxyConfig.getClient().getKeyStorePassword().toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(ks);
			TrustManager[] trustManagers = tmf.getTrustManagers();
			clientContext.init(null, trustManagers, null);

			SSLEngine sslEngine = clientContext.createSSLEngine();
			sslEngine.setUseClientMode(true);

			return new SslHandler(sslEngine);
		} catch (Exception e) {
			log.error("创建SSL处理器失败", e);
			e.printStackTrace();
		}
		return null;
	}

	@Bean
	public Bootstrap bootstrap() {
		return new Bootstrap();
	}

	@Bean
	public Bootstrap realServerBootstrap() {
		return new Bootstrap();
	}

	private ProxyClientConfig getClientConfig(String path) {
		if (StringUtil.isEmpty(path)) {
			path = "./config.json";
		}
		String content = FileUtil.readContentAsString(path);
		if (StringUtil.isEmpty(content)) {
			log.error("配置文件: {} 不存在或格式异常!", path);
			System.exit(0);
			return null;
		}
		try {
			ProxyClientConfig clientConfig = JSONObject.parseObject(content, ProxyClientConfig.class);
			if (StringUtil.isEmpty(clientConfig.getClientKey()) || CollectionUtil.isEmpty(clientConfig.getProxy())) {
				log.error("配置异常!");
				System.exit(0);
				return null;
			}
			return clientConfig;
		} catch (Exception e) {
			log.error("解析配置文件异常!", e);
		}
		return null;
	}
}
