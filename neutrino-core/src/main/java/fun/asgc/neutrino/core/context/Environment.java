  

package fun.asgc.neutrino.core.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@Accessors(chain = true)
@Data
public class Environment {
	/**
	 * 启动类
	 */
	private Class<?> mainClass;
	/**
	 * 启动参数
	 */
	private String[] mainArgs;
	/**
	 * 包扫描路径
	 */
	private List<String> scanBasePackages;
	/**
	 * 横幅
	 */
	private String banner;
	/**
	 * 应用配置
	 */
	private ApplicationConfig config;
}
