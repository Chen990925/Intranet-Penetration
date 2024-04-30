package fun.asgc.neutrino.proxy.core;

import fun.asgc.neutrino.core.base.Handler;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/1
 */
public interface ProxyMessageHandler extends Handler<ChannelHandlerContext, ProxyMessage> {

}
