package fun.asgc.neutrino.proxy.core;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 *
 * @author: chenjunlin
 * @date: 2024/5/1
 */
public interface Constants {

    AttributeKey<Channel> NEXT_CHANNEL = AttributeKey.newInstance("nxt_channel");

    AttributeKey<String> USER_ID = AttributeKey.newInstance("user_id");

    AttributeKey<String> CLIENT_KEY = AttributeKey.newInstance("client_key");

    int HEADER_SIZE = 4;
    int TYPE_SIZE = 1;
    int SERIAL_NUMBER_SIZE = 8;
    int INFO_LENGTH_SIZE = 4;

    interface ProxyDataTypeName {
        String HEARTBEAT = "HEARTBEAT";
        String AUTH = "AUTH";
        String CONNECT = "CONNECT";
        String DISCONNECT = "DISCONNECT";
        String TRANSFER = "TRANSFER";
        String ERROR = "ERROR";
    }
}
