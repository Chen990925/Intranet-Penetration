 

package fun.asgc.neutrino.proxy.server.handler;

import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.annotation.Match;
import fun.asgc.neutrino.proxy.core.Constants;
import fun.asgc.neutrino.proxy.core.ProxyDataTypeEnum;
import fun.asgc.neutrino.proxy.core.ProxyMessage;
import fun.asgc.neutrino.proxy.core.ProxyMessageHandler;
import fun.asgc.neutrino.proxy.server.util.ProxyChannelManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@Match(type = Constants.ProxyDataTypeName.DISCONNECT)
@Component
public class ProxyMessageDisconnectHandler implements ProxyMessageHandler {

	@Override
	public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
		String clientKey = ctx.channel().attr(Constants.CLIENT_KEY).get();

		// 代理连接没有连上服务器由控制连接发送用户端断开连接消息
		if (clientKey == null) {
			String userId = proxyMessage.getInfo();
			Channel userChannel = ProxyChannelManager.removeUserChannelFromCmdChannel(ctx.channel(), userId);
			if (userChannel != null) {
				// 数据发送完成后再关闭连接，解决http1.0数据传输问题
				userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
			}
			return;
		}

		Channel cmdChannel = ProxyChannelManager.getCmdChannel(clientKey);
		if (cmdChannel == null) {
			return;
		}

		Channel userChannel = ProxyChannelManager.removeUserChannelFromCmdChannel(cmdChannel, ctx.channel().attr(Constants.USER_ID).get());
		if (userChannel != null) {
			// 数据发送完成后再关闭连接，解决http1.0数据传输问题
			userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
			ctx.channel().attr(Constants.NEXT_CHANNEL).remove();
			ctx.channel().attr(Constants.CLIENT_KEY).remove();
			ctx.channel().attr(Constants.USER_ID).remove();
		}
	}

	@Override
	public String name() {
		return ProxyDataTypeEnum.DISCONNECT.getDesc();
	}

}
