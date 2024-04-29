
package fun.asgc.neutrino.core.context;

import fun.asgc.neutrino.core.container.BeanContainer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@Accessors(chain = true)
@Data
public class ApplicationContext {

	/**
	 * 应用环境
	 */
	private Environment environment;
	/**
	 * 应用配置
	 */
	private ApplicationConfig applicationConfig;
	/**
	 * bean容器
	 */
	private BeanContainer beanContainer;
}
