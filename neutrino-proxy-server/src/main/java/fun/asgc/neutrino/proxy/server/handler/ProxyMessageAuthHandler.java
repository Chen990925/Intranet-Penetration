 

package fun.asgc.neutrino.proxy.server.handler;

import com.alibaba.fastjson.JSONObject;
import fun.asgc.neutrino.core.annotation.Autowired;
import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.annotation.Match;
import fun.asgc.neutrino.core.util.BeanManager;
import fun.asgc.neutrino.proxy.core.*;
import fun.asgc.neutrino.proxy.server.config.ProxyConfig;
import fun.asgc.neutrino.proxy.server.config.ProxyServerConfig;
import fun.asgc.neutrino.proxy.server.core.BytesMetricsHandler;
import fun.asgc.neutrino.proxy.server.core.UserChannelHandler;
import fun.asgc.neutrino.proxy.server.util.ProxyChannelManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.BindException;
import java.util.List;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@Slf4j
@Match(type = Constants.ProxyDataTypeName.AUTH)
@Component
public class ProxyMessageAuthHandler implements ProxyMessageHandler {
	@Autowired("serverBossGroup")
	private NioEventLoopGroup serverBossGroup;
	@Autowired("serverWorkerGroup")
	private NioEventLoopGroup serverWorkerGroup;
	@Autowired
	private ProxyConfig proxyConfig;

	@Override
	public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
		ProxyClientConfig clientConfig = JSONObject.parseObject(proxyMessage.getInfo(), ProxyClientConfig.class);
		String clientKey = clientConfig.getClientKey();
		if (!proxyConfig.getLicenseMap().containsKey(clientKey)) {
			ctx.channel().writeAndFlush(ProxyMessage.buildErrMessage(ExceptionEnum.AUTH_FAILED, "无效的clientKey"));
			ctx.channel().close();
			return;
		}

		if (proxyConfig.getLicenseMap().get(clientKey) != -1 && clientConfig.getProxy().size() > proxyConfig.getLicenseMap().get(clientKey)) {
			ctx.channel().writeAndFlush(ProxyMessage.buildErrMessage(ExceptionEnum.AUTH_FAILED, "代理端口数超过license限制"));
			ctx.channel().close();
			return;
		}

		ProxyServerConfig.getInstance().addClientConfig(clientConfig);
		List<Integer> ports = ProxyServerConfig.getInstance().getClientInetPorts(clientKey);
		if (ports == null) {
			ctx.channel().close();
			return;
		}

		Channel channel = ProxyChannelManager.getCmdChannel(clientKey);
		if (channel != null) {
			ctx.channel().close();
			return;
		}

		ProxyChannelManager.addCmdChannel(ports, clientKey, ctx.channel());

		startUserPortServer(ports);
	}

	@Override
	public String name() {
		return ProxyDataTypeEnum.AUTH.getDesc();
	}

	private void startUserPortServer(List<Integer> ports) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(serverBossGroup, serverWorkerGroup)
			.channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addFirst(new BytesMetricsHandler());
				ch.pipeline().addLast(new UserChannelHandler());
			}
		});

		for (int port : ports) {
			try {
				bootstrap.bind(port).get();
				log.info("绑定用户端口： {}", port);
			} catch (Exception ex) {
				// BindException表示该端口已经绑定过
				if (!(ex.getCause() instanceof BindException)) {
					throw new RuntimeException(ex);
				}
			}
		}
	}
}
