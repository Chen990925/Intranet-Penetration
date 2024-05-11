package fun.asgc.neutrino.proxy.client;

import fun.asgc.neutrino.core.annotation.NeutrinoApplication;
import fun.asgc.neutrino.core.launcher.NeutrinoLauncher;

/**
 * 启动类
 * @author: chenjunlin
 * @date: 2024/5/9
 */
@NeutrinoApplication
public class ProxyClient {

	public static void main(String[] args) {
		NeutrinoLauncher.run(ProxyClient.class, args);
	}

}
