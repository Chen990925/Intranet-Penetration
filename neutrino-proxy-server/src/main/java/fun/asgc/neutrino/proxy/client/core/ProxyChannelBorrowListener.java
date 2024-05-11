 

package fun.asgc.neutrino.proxy.client.core;

import io.netty.channel.Channel;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/10
 */
public interface ProxyChannelBorrowListener {

    void success(Channel channel);

    void error(Throwable cause);

}
