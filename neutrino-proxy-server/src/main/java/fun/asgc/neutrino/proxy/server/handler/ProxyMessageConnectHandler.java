 

package fun.asgc.neutrino.proxy.server.handler;

import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.annotation.Match;
import fun.asgc.neutrino.proxy.core.Constants;
import fun.asgc.neutrino.proxy.core.ProxyDataTypeEnum;
import fun.asgc.neutrino.proxy.core.ProxyMessage;
import fun.asgc.neutrino.proxy.core.ProxyMessageHandler;
import fun.asgc.neutrino.proxy.server.util.ProxyChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@Match(type = Constants.ProxyDataTypeName.CONNECT)
@Component
public class ProxyMessageConnectHandler implements ProxyMessageHandler {

	@Override
	public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
		String info = proxyMessage.getInfo();
		if (info == null) {
			ctx.channel().close();
			return;
		}

		String[] tokens = info.split("@");
		if (tokens.length != 2) {
			ctx.channel().close();
			return;
		}

		Channel cmdChannel = ProxyChannelManager.getCmdChannel(tokens[1]);
		if (cmdChannel == null) {
			ctx.channel().close();
			return;
		}

		Channel userChannel = ProxyChannelManager.getUserChannel(cmdChannel, tokens[0]);
		if (userChannel != null) {
			ctx.channel().attr(Constants.USER_ID).set(tokens[0]);
			ctx.channel().attr(Constants.CLIENT_KEY).set(tokens[1]);
			ctx.channel().attr(Constants.NEXT_CHANNEL).set(userChannel);
			userChannel.attr(Constants.NEXT_CHANNEL).set(ctx.channel());
			// 代理客户端与后端服务器连接成功，修改用户连接为可读状态
			userChannel.config().setOption(ChannelOption.AUTO_READ, true);
		}
	}

	@Override
	public String name() {
		return ProxyDataTypeEnum.CONNECT.getDesc();
	}
}
