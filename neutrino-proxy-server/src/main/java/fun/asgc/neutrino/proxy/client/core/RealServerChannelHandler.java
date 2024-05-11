 

package fun.asgc.neutrino.proxy.client.core;

import fun.asgc.neutrino.proxy.client.util.ClientChannelMannager;
import fun.asgc.neutrino.proxy.core.Constants;
import fun.asgc.neutrino.proxy.core.ProxyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 处理与被代理客户端的数据传输
 * @author:  chenjunlin
 * @date: 2024/5/10
 */
public class RealServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        Channel realServerChannel = ctx.channel();
        Channel channel = realServerChannel.attr(Constants.NEXT_CHANNEL).get();
        if (channel == null) {
            // 代理客户端连接断开
            ctx.channel().close();
        } else {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String userId = ClientChannelMannager.getRealServerChannelUserId(realServerChannel);
            channel.writeAndFlush(ProxyMessage.buildTransferMessage(userId, bytes));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel realServerChannel = ctx.channel();
        String userId = ClientChannelMannager.getRealServerChannelUserId(realServerChannel);
        ClientChannelMannager.removeRealServerChannel(userId);
        Channel channel = realServerChannel.attr(Constants.NEXT_CHANNEL).get();
        if (channel != null) {
            channel.writeAndFlush(ProxyMessage.buildDisconnectMessage(userId));
        }

        super.channelInactive(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel realServerChannel = ctx.channel();
        Channel proxyChannel = realServerChannel.attr(Constants.NEXT_CHANNEL).get();
        if (proxyChannel != null) {
            proxyChannel.config().setOption(ChannelOption.AUTO_READ, realServerChannel.isWritable());
        }

        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
