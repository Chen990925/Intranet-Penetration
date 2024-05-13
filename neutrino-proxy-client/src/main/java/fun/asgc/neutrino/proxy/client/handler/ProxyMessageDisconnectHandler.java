 

package fun.asgc.neutrino.proxy.client.handler;

import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.annotation.Match;
import fun.asgc.neutrino.proxy.client.util.ClientChannelMannager;
import fun.asgc.neutrino.proxy.core.Constants;
import fun.asgc.neutrino.proxy.core.ProxyDataTypeEnum;
import fun.asgc.neutrino.proxy.core.ProxyMessage;
import fun.asgc.neutrino.proxy.core.ProxyMessageHandler;
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
		Channel realServerChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
		if (realServerChannel != null) {
			ctx.channel().attr(Constants.NEXT_CHANNEL).remove();
			ClientChannelMannager.returnProxyChanel(ctx.channel());
			realServerChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public String name() {
		return ProxyDataTypeEnum.DISCONNECT.getDesc();
	}

}
