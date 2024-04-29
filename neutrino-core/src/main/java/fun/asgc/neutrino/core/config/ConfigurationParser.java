  

package fun.asgc.neutrino.core.config;

import fun.asgc.neutrino.core.exception.ConfigurationParserException;

import java.io.InputStream;
import java.util.Map;

/**
 * @author: chenjunlin
 * @date: 2024/4/25
 */
public interface ConfigurationParser {

	/**
	 * 解析
	 * @param in
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T parse(InputStream in, Class<T> clazz) throws ConfigurationParserException;

	/**
	 * 解析
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T parse(Class<T> clazz) throws ConfigurationParserException;

	/**
	 * 解析
	 * @param config
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws ConfigurationParserException
	 */
	<T> T parse(Map<String, Object> config, Class<T> clazz) throws ConfigurationParserException;
}
