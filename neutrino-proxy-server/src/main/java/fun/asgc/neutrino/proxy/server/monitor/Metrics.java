 

package fun.asgc.neutrino.proxy.server.monitor;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@Data
public class Metrics implements Serializable {
    private static final long serialVersionUID = 1L;
    private int port;
    private long readBytes;
    private long wroteBytes;
    private long readMsgs;
    private long wroteMsgs;
    private int channels;
    private long timestamp;
}
