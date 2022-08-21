package wang.ulane.jdbc;

import java.util.ArrayList;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;

public class DBDocExport {
	
	public static void main(String[] args) {
		documentGeneration();
	}
	
	/**
	 * 文档生成
	 */
	static void documentGeneration() {
		String fileOutputDir = "C:\\Users\\Administrator\\Desktop\\temp";
		String fileName = "数据库导出";
		
//		String driverName = "oracle.jdbc.driver.OracleDriver";
//		String host = "jdbc:oracle:thin:@***:1521/helowinXDB";
//		String username = "ulane";
//		String password = "***";
		String driverName = "oracle.jdbc.driver.OracleDriver";
		String host = "jdbc:oracle:thin:@***:1521/testdb";
		String username = "";
		String password = "";
		
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(driverName);
		hikariConfig.setJdbcUrl(host);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		// 设置可以获取tables remarks信息
		hikariConfig.addDataSourceProperty("useInformationSchema", "true");
		hikariConfig.setMinimumIdle(2);
		hikariConfig.setMaximumPoolSize(5);
		DataSource dataSource = new HikariDataSource(hikariConfig);
		
	   //生成配置
	   EngineConfig engineConfig = EngineConfig.builder()
	         //生成文件路径
	         .fileOutputDir(fileOutputDir)
	         //打开目录
	         .openOutputDir(false)
	         //文件类型
	         .fileType(EngineFileType.WORD)
	         //生成模板实现
	         .produceType(EngineTemplateType.freemarker)
	         //自定义文件名称
	         .fileName(fileName).build();
	   
	   //指定表前缀
	   ArrayList<String> designatedTableName = new ArrayList<>();
	   ArrayList<String> designatedTablePrefix = new ArrayList<>();
	   designatedTablePrefix.add("TB_APPLICANTINFORMATIO");
	   designatedTablePrefix.add("TB_APPLY_CHILD_INF");
	   designatedTablePrefix.add("IDE_DISBURSE");
	   designatedTablePrefix.add("STD_MSP_");
	   designatedTablePrefix.add("TB_E");
	   designatedTablePrefix.add("IDE_PRODUCT_");
	   designatedTablePrefix.add("IDE_MODEL");
	   designatedTablePrefix.add("IDE_QUOTA");
	   designatedTablePrefix.add("IDE_LIMIT");
	   designatedTablePrefix.add("EXC_");
	   designatedTablePrefix.add("STD_BANK");
	   designatedTablePrefix.add("SYS_");
	   designatedTablePrefix.add("DH_");
	   designatedTablePrefix.add("WARN_BUSI");
	   
	   //忽略表
	   ArrayList<String> ignoreTableName = new ArrayList<>();
	   ignoreTableName.add("test_user");
	   ignoreTableName.add("test_group");
	   //忽略表前缀
	   ArrayList<String> ignorePrefix = new ArrayList<>();
	   ignorePrefix.add("test_");
	   //忽略表后缀    
	   ArrayList<String> ignoreSuffix = new ArrayList<>();
	   ignoreSuffix.add("_test");
	   ProcessConfig processConfig = ProcessConfig.builder()
	         //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置	
			 //根据名称指定表生成
			 .designatedTableName(designatedTableName)
			 //根据表前缀生成
			 .designatedTablePrefix(designatedTablePrefix)
			 //根据表后缀生成	
			 .designatedTableSuffix(new ArrayList<>())
	         //忽略表名
	         .ignoreTableName(ignoreTableName)
	         //忽略表前缀
	         .ignoreTablePrefix(ignorePrefix)
	         //忽略表后缀
	         .ignoreTableSuffix(ignoreSuffix).build();
	   //配置
	   Configuration config = Configuration.builder()
	         //版本
	         .version("1.0.0")
	         //描述
	         .description("数据库设计文档生成")
	         //数据源
	         .dataSource(dataSource)
	         //生成配置
	         .engineConfig(engineConfig)
	         //生成配置
	         .produceConfig(processConfig)
	         .build();
	   //执行生成
	   new DocumentationExecute(config).execute();
	}
	
}
