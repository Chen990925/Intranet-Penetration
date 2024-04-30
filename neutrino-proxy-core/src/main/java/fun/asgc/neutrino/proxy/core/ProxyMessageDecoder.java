package fun.asgc.neutrino.proxy.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import static fun.asgc.neutrino.proxy.core.Constants.*;

/**
 * 一个基于 Netty 的 LengthFieldBasedFrameDecoder 的子类，用于将接收到的字节流解码为 ProxyMessage 对象也就是自定义的对象
 * 这是一个解码器，用于将字节流解码为帧（Frame），并根据长度字段的值来拆分帧
 * @author: chenjunlin
 * @date: 2024/5/1
 */
public class ProxyMessageDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 提供了两个构造函数，分别接收不同的参数，用于初始化父类 LengthFieldBasedFrameDecoder
     * maxFrameLength：帧的最大长度，超出该长度的帧将被丢弃。
     * lengthFieldOffset：长度字段的偏移量，即长度字段相对于帧起始位置的偏移量。
     * lengthFieldLength：长度字段的长度，即长度字段的字节数。
     * lengthAdjustment：长度调整值，用于调整消息长度。
     * initialBytesToStrip：初始字节忽略量，即在解码时跳过的字节数。
     * failFast：是否快速失败，如果为 true，则表示解码器遇到异常时将立即抛出异常，而不是等待更多的数据
     * @param maxFrameLength
     * @param lengthFieldOffset
     * @param lengthFieldLength
     * @param lengthAdjustment
     * @param initialBytesToStrip
     */
    public ProxyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
            int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public ProxyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
            int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    /**
     * 重写了父类的 decode() 方法，用于实现解码逻辑。
     * @param ctx
     * @param in2
     * @return
     * @throws Exception
     */
    @Override
    protected ProxyMessage decode(ChannelHandlerContext ctx, ByteBuf in2) throws Exception {
        //在 decode() 方法中，首先调用父类的 decode() 方法，将字节流解码为一个 ByteBuf 对象。
        ByteBuf in = (ByteBuf) super.decode(ctx, in2);
        //然后判断解码后的 ByteBuf 对象是否为空，如果为空则返回 null。
        if (in == null) {
            return null;
        }
        //接着判断解码后的字节流长度是否大于消息头部的长度，如果不足则返回 null。
        if (in.readableBytes() < HEADER_SIZE) {
            return null;
        }
        int frameLength = in.readInt();
        if (in.readableBytes() < frameLength) {
            return null;
        }
        //如果长度足够，则依次从字节流中读取消息的类型、流水号、信息长度、信息内容和数据内容，并设置到 ProxyMessage 对象中。
        ProxyMessage proxyMessage = new ProxyMessage();
        byte type = in.readByte();
        long sn = in.readLong();

        proxyMessage.setSerialNumber(sn);

        proxyMessage.setType(type);

        int infoLength = in.readInt();
        byte[] infoBytes = new byte[infoLength];
        in.readBytes(infoBytes);
        proxyMessage.setInfo(new String(infoBytes));

        byte[] data = new byte[frameLength - TYPE_SIZE - SERIAL_NUMBER_SIZE - INFO_LENGTH_SIZE - infoLength];
        in.readBytes(data);
        proxyMessage.setData(data);
        //最后释放 ByteBuf 对象资源，并返回解码后的 ProxyMessage 对象
        in.release();

        return proxyMessage;
    }
}
