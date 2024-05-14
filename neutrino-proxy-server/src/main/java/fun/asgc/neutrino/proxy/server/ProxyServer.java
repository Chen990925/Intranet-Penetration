 

package fun.asgc.neutrino.proxy.server;

import fun.asgc.neutrino.core.annotation.NeutrinoApplication;
import fun.asgc.neutrino.core.launcher.NeutrinoLauncher;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@NeutrinoApplication
public class ProxyServer {

	public static void main(String[] args) {
		NeutrinoLauncher.run(ProxyServer.class, args);
	}

}
