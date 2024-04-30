package fun.asgc.neutrino.proxy.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import static fun.asgc.neutrino.proxy.core.Constants.*;

/**
 * 用于将 ProxyMessage 对象编码为字节流
 * MessageToByteEncoder这是一个编码器，用于将消息对象编码为字节流
 * @author: chenjunlin
 * @date: 2024/5/1
 */
public class ProxyMessageEncoder extends MessageToByteEncoder<ProxyMessage> {

    public ProxyMessageEncoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ProxyMessage msg, ByteBuf out) throws Exception {
        //根据 ProxyMessage 对象的内容，计算出消息体的长度
        int bodyLength = TYPE_SIZE + SERIAL_NUMBER_SIZE + INFO_LENGTH_SIZE;
        byte[] infoBytes = null;
        if (msg.getInfo() != null) {
            infoBytes = msg.getInfo().getBytes();
            bodyLength += infoBytes.length;
        }

        //首先计算出消息体的基本长度，包括消息类型、流水号和信息长度的长度
        if (msg.getData() != null) {
            bodyLength += msg.getData().length;
        }

        //然后根据消息对象中的信息内容和数据内容，计算出实际的消息体长度，并将其写入到输出字节流 ByteBuf 中
        out.writeInt(bodyLength);

        out.writeByte(msg.getType());
        out.writeLong(msg.getSerialNumber());

        //最后，按照消息格式，依次将消息类型、流水号、信息长度、信息内容和数据内容写入到输出字节流中
        if (infoBytes != null) {
            out.writeInt(infoBytes.length);
            out.writeBytes(infoBytes);
        } else {
            out.writeInt(0x00);
        }

        if (msg.getData() != null) {
            out.writeBytes(msg.getData());
        }
    }
}
