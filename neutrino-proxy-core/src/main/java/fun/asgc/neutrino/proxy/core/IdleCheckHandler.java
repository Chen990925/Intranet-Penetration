  

package fun.asgc.neutrino.proxy.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 用于在 Netty 管道中检测空闲状态，并执行相应的操作
 * @author: chenjunlin
 * @date: 2024/5/1
 */
public class IdleCheckHandler extends IdleStateHandler {

    /**
     * 造函数接收三个参数 readerIdleTimeSeconds、writerIdleTimeSeconds 和 allIdleTimeSeconds，
     * 分别表示读空闲时间、写空闲时间和所有类型空闲时间的阈值。
     * 调用父类 IdleStateHandler 的构造函数，并传入这三个参数，初始化空闲状态处理器
     * @param readerIdleTimeSeconds
     * @param writerIdleTimeSeconds
     * @param allIdleTimeSeconds
     */
    public IdleCheckHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }

    /**
     * 重写了父类的 channelIdle() 方法，用于处理管道的空闲事件。
     * 在方法中，首先判断空闲状态事件的类型，根据不同类型执行不同的操作。
     * 如果是第一次写空闲（FIRST_WRITER_IDLE_STATE_EVENT），则向管道写入一个心跳消息（通过 ProxyMessage.buildHeartbeatMessage() 方法构建），以保持连接的活跃状态。
     * 如果是第一次读空闲（FIRST_READER_IDLE_STATE_EVENT），则关闭管道（通过 ctx.channel().close() 方法关闭），断开连接。
     * 最后调用父类的 channelIdle() 方法，继续执行其他可能的空闲处理逻辑
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        if (IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT == evt) {
            ctx.channel().writeAndFlush(ProxyMessage.buildHeartbeatMessage());
        } else if (IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT == evt) {
            ctx.channel().close();
        }
        super.channelIdle(ctx, evt);
    }
}
