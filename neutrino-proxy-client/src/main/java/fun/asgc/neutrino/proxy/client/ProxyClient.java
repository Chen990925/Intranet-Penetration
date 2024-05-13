 

package fun.asgc.neutrino.proxy.client;

import fun.asgc.neutrino.core.annotation.NeutrinoApplication;
import fun.asgc.neutrino.core.launcher.NeutrinoLauncher;

/**
 *
 * @author:  chenjunlin
 * @date: 2024/5/13
 */
@NeutrinoApplication
public class ProxyClient {

	public static void main(String[] args) {
		NeutrinoLauncher.run(ProxyClient.class, args);
	}

}
