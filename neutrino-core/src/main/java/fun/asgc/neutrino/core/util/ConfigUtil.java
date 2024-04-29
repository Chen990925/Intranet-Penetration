  

package fun.asgc.neutrino.core.util;

import fun.asgc.neutrino.core.config.ConfigurationParser;
import fun.asgc.neutrino.core.config.impl.YmlConfigurationParser;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
public class ConfigUtil {

	private static final ConfigurationParser ymlParser = new YmlConfigurationParser();

	public static <T> T getYmlConfig(Class<T> clazz) {
		return ymlParser.parse(clazz);
	}

}
