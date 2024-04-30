  

package fun.asgc.neutrino.proxy.core;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;

/**
 * 一个消息对象的定义，用于在网络通信中传递消息
 * @author: chenjunlin
 * @date: 2024/5/1
 */
@Accessors(chain = true)
@Data
public class ProxyMessage {

    /**
     * 心跳消息
     */
    public static final byte TYPE_HEARTBEAT = 0x01;

    /**
     * 认证消息，检测clientKey是否正确
     */
    public static final byte TYPE_AUTH = 0x02;

    /**
     * 代理后端服务器建立连接消息
     */
    public static final byte TYPE_CONNECT = 0x03;

    /**
     * 代理后端服务器断开连接消息
     */
    public static final byte TYPE_DISCONNECT = 0x04;

    /**
     * 代理数据传输
     */
    public static final byte TYPE_TRANSFER = 0x05;

    /**
     * 通用异常信息
     */
    public static final byte TYPE_ERROR = 0x06;

    /**
     * 消息类型
     */
    private byte type;

    /**
     * 消息流水号
     */
    private long serialNumber;

    /**
     * 消息命令请求信息
     */
    private String info;

    /**
     * 消息传输数据
     */
    private byte[] data;

    @Override
    public String toString() {
        return "ProxyMessage [type=" + type + ", serialNumber=" + serialNumber + ", info=" + info + ", data=" + Arrays.toString(data) + "]";
    }

    public static ProxyMessage create() {
        return new ProxyMessage();
    }

    public static ProxyMessage buildHeartbeatMessage() {
        return create().setType(TYPE_HEARTBEAT);
    }

    /**
     * 创建认证消息
     * @param info
     * @return
     */
    public static ProxyMessage buildAuthMessage(String info) {
        return create().setType(TYPE_AUTH)
            .setInfo(info);
    }

    public static ProxyMessage buildConnectMessage(String info) {
        return create().setType(TYPE_CONNECT)
            .setInfo(info);
    }

    public static ProxyMessage buildDisconnectMessage(String info) {
        return create().setType(TYPE_DISCONNECT)
            .setInfo(info);
    }

    public static ProxyMessage buildTransferMessage(String info, byte[] data) {
        return create().setType(TYPE_TRANSFER)
            .setInfo(info)
            .setData(data);
    }

    public static ProxyMessage buildErrMessage(ExceptionEnum exceptionEnum, String info) {
        JSONObject data = new JSONObject();
        data.put("code", exceptionEnum.getCode());
        data.put("msg", exceptionEnum.getMsg());
        data.put("info", info);
        return create().setType(TYPE_ERROR)
            .setInfo(data.toJSONString());
    }

    /**
     * 提供了一个构建错误消息的方法，可以指定异常信息和其他相关信息，将其封装成一个错误消息对象返回
     * @param exceptionEnum
     * @return
     */
    public static ProxyMessage buildErrMessage(ExceptionEnum exceptionEnum) {
       return buildErrMessage(exceptionEnum, null);
    }
}
