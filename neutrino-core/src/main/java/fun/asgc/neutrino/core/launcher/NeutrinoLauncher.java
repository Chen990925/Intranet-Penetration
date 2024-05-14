
package fun.asgc.neutrino.core.launcher;

import com.google.common.collect.Lists;
import fun.asgc.neutrino.core.annotation.NeutrinoApplication;
import fun.asgc.neutrino.core.constant.MetaDataConstant;
import fun.asgc.neutrino.core.container.ApplicationContainer;
import fun.asgc.neutrino.core.container.DefaultApplicationContainer;
import fun.asgc.neutrino.core.context.ApplicationConfig;
import fun.asgc.neutrino.core.context.Environment;
import fun.asgc.neutrino.core.util.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;

/**
 *	用于启动应用程序。它负责初始化环境、加载配置、创建应用容器，并启动应用
 * @author: chenjunlin
 * @date: 2024/4/29
 */
@Slf4j
public class NeutrinoLauncher {
	private Environment environment;
	private ApplicationContainer applicationContainer;

	public static SystemUtil.RunContext run(final Class<?> clazz, final String[] args) {
		Assert.notNull(clazz, "启动类不能为空!");
		return new NeutrinoLauncher(clazz, args).launch();
	}

	public static void runSync(final Class<?> clazz, final String[] args) {
		run(clazz, args).sync();
		log.info("Application already stop.");
	}

	/**
	 * 用于初始化 NeutrinoLauncher 实例，接收应用程序的主类和命令行参数作为参数
	 * @param clazz
	 * @param args
	 */
	private NeutrinoLauncher(Class<?> clazz, String[]  args) {
		this.environment = new Environment()
			.setMainClass(clazz)
			.setMainArgs(args);
	}

	private SystemUtil.RunContext launch() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		environmentInit();
		this.applicationContainer = new DefaultApplicationContainer(environment);
		SystemUtil.RunContext runContext = SystemUtil.waitProcessDestroy(() -> {
			this.applicationContainer.destroy();
			log.info("Application already stop.");
		});

		stopWatch.stop();
		printLog(environment, stopWatch);

		return runContext;
	}

	/**
	 * 环境初始化
	 */
	private void environmentInit() {
		environment.setScanBasePackages(Lists.newArrayList(TypeUtil.getPackageName(environment.getMainClass())));

		bannerProcess(environment);

		NeutrinoApplication neutrinoApplication = environment.getMainClass().getAnnotation(NeutrinoApplication.class);
		if (null != neutrinoApplication) {
			String[] scanBasePackages = neutrinoApplication.scanBasePackages();
			if (ArrayUtil.notEmpty(scanBasePackages)) {
				environment.setScanBasePackages(Lists.newArrayList(scanBasePackages));
			}
		}
		log.info("scanBasePackages: {}", environment.getScanBasePackages());

		// 加载应用配置
		environment.setConfig(ConfigUtil.getYmlConfig(ApplicationConfig.class));
		log.info("load ApplicationConfig finished.");
	}

	/**
	 * 打印启动日志
	 * @param environment
	 * @param stopWatch
	 */
	private void printLog(Environment environment, StopWatch stopWatch) {
		StringBuffer sb = new StringBuffer();
		sb.append("Started ");
		sb.append(environment.getMainClass().getName());
		sb.append(" in ");
		sb.append(stopWatch.getTotalTimeSeconds());

		try {
			double uptime = (double) ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0D;
			sb.append(" seconds (JVM running for " + uptime + ")");
		} catch (Throwable var5) {
		}
		log.info(sb.toString());
	}

	/**
	 * banner处理
	 * @param environment
	 */
	private void bannerProcess(Environment environment) {
		environment.setBanner(MetaDataConstant.DEFAULT_BANNER);
		String banner = FileUtil.readContentAsString(MetaDataConstant.APP_BANNER_FILE_PATH);
		if (StringUtil.notEmpty(banner)) {
			environment.setBanner(banner);
		}
		log.info(environment.getBanner());
	}
}
