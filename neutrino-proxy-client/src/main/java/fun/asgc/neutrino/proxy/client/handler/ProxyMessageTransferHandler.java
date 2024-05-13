 

package fun.asgc.neutrino.proxy.client.handler;

import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.annotation.Match;
import fun.asgc.neutrino.proxy.core.Constants;
import fun.asgc.neutrino.proxy.core.ProxyDataTypeEnum;
import fun.asgc.neutrino.proxy.core.ProxyMessage;
import fun.asgc.neutrino.proxy.core.ProxyMessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@Match(type = Constants.ProxyDataTypeName.TRANSFER)
@Component
public class ProxyMessageTransferHandler implements ProxyMessageHandler {

	@Override
	public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
		Channel realServerChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
		if (realServerChannel != null) {
			ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
			buf.writeBytes(proxyMessage.getData());
			realServerChannel.writeAndFlush(buf);
		}
	}

	@Override
	public String name() {
		return ProxyDataTypeEnum.TRANSFER.getDesc();
	}

}
