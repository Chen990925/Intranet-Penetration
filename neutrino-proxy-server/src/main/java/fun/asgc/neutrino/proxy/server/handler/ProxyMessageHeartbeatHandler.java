 

package fun.asgc.neutrino.proxy.server.handler;

import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.annotation.Match;
import fun.asgc.neutrino.proxy.core.Constants;
import fun.asgc.neutrino.proxy.core.ProxyDataTypeEnum;
import fun.asgc.neutrino.proxy.core.ProxyMessage;
import fun.asgc.neutrino.proxy.core.ProxyMessageHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@Match(type = Constants.ProxyDataTypeName.HEARTBEAT)
@Component
public class ProxyMessageHeartbeatHandler implements ProxyMessageHandler {

	@Override
	public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
		ctx.channel().writeAndFlush(ProxyMessage.buildHeartbeatMessage());
	}

	@Override
	public String name() {
		return ProxyDataTypeEnum.HEARTBEAT.getDesc();
	}

}
