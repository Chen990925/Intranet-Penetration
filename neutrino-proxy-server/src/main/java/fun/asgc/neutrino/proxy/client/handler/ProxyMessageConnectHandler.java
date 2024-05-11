 

package fun.asgc.neutrino.proxy.client.handler;

import fun.asgc.neutrino.core.annotation.Autowired;
import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.annotation.Match;
import fun.asgc.neutrino.proxy.client.config.ProxyConfig;
import fun.asgc.neutrino.proxy.client.core.ProxyChannelBorrowListener;
import fun.asgc.neutrino.proxy.client.util.ClientChannelMannager;
import fun.asgc.neutrino.proxy.core.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/10
 */
@Match(type = Constants.ProxyDataTypeName.CONNECT)
@Component
public class ProxyMessageConnectHandler implements ProxyMessageHandler {
	@Autowired("bootstrap")
	private Bootstrap bootstrap;
	@Autowired("realServerBootstrap")
	private Bootstrap realServerBootstrap;

	@Override
	public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
		final Channel cmdChannel = ctx.channel();
		final String userId = proxyMessage.getInfo();
		String[] serverInfo = new String(proxyMessage.getData()).split(":");
		String ip = serverInfo[0];
		int port = Integer.parseInt(serverInfo[1]);
		realServerBootstrap.connect(ip, port).addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future) throws Exception {

				// 连接后端服务器成功
				if (future.isSuccess()) {
					final Channel realServerChannel = future.channel();

					realServerChannel.config().setOption(ChannelOption.AUTO_READ, false);

					// 获取连接
					ClientChannelMannager.borrowProxyChanel(bootstrap, new ProxyChannelBorrowListener() {

						@Override
						public void success(Channel channel) {
							// 连接绑定
							channel.attr(Constants.NEXT_CHANNEL).set(realServerChannel);
							realServerChannel.attr(Constants.NEXT_CHANNEL).set(channel);

							// 远程绑定
							channel.writeAndFlush(ProxyMessage.buildConnectMessage(userId + "@" + ProxyConfig.instance.getClientConfig().getClientKey()));

							realServerChannel.config().setOption(ChannelOption.AUTO_READ, true);
							ClientChannelMannager.addRealServerChannel(userId, realServerChannel);
							ClientChannelMannager.setRealServerChannelUserId(realServerChannel, userId);
						}

						@Override
						public void error(Throwable cause) {
							ProxyMessage proxyMessage = new ProxyMessage();
							proxyMessage.setType(ProxyMessage.TYPE_DISCONNECT);
							proxyMessage.setInfo(userId);
							cmdChannel.writeAndFlush(proxyMessage);
						}
					});

				} else {
					cmdChannel.writeAndFlush(ProxyMessage.buildDisconnectMessage(userId));
				}
			}
		});
	}

	@Override
	public String name() {
		return ProxyDataTypeEnum.CONNECT.getDesc();
	}
}
