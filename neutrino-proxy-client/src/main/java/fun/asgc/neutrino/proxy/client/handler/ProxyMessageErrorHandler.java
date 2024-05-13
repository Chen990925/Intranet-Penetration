 

package fun.asgc.neutrino.proxy.client.handler;

import com.alibaba.fastjson.JSONObject;
import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.annotation.Match;
import fun.asgc.neutrino.proxy.core.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@Slf4j
@Match(type = Constants.ProxyDataTypeName.ERROR)
@Component
public class ProxyMessageErrorHandler implements ProxyMessageHandler {

	@Override
	public void handle(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
		log.info("异常信息: {}", proxyMessage.getInfo());
		JSONObject data = JSONObject.parseObject(proxyMessage.getInfo());
		Integer code = data.getInteger("code");
		if (ExceptionEnum.AUTH_FAILED.getCode().equals(code)) {
			System.exit(0);
		}
	}

	@Override
	public String name() {
		return ProxyDataTypeEnum.DISCONNECT.getDesc();
	}
}
