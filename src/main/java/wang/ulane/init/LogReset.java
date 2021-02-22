package wang.ulane.init;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

public class LogReset {
	
	/**
	 * 重置logback的配置路径
	 * @param configFilepathName
	 */
	public static void initLogback(String path) {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator joranConfigurator = new JoranConfigurator();
		joranConfigurator.setContext(loggerContext);
		loggerContext.reset();
		try {
			joranConfigurator.doConfigure(PropLoad.initFile(path, "logback-exists.xml"));
		} catch (Exception e) {
			System.out.println(String.format("Load logback config file error. Message: ", e.getMessage()));
		}
		//在doConfigure之前日志不打印
//		StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
	}
	
}
