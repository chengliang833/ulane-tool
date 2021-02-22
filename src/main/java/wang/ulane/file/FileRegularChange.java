package wang.ulane.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.regex.Matcher;

import com.alibaba.fastjson.JSONObject;

import wang.ulane.util.regex.RegexUtil;
import wang.ulane.util.regex.RegexUtil.CallBack;

public class FileRegularChange {
	public static void main(String[] args) throws Exception {
		mainTest();
//		adapter();
	}
	
	private static String currentTable = null; 
	private static int currentRow = 0;
	private static String primKeyString = null;
	public static void mainTest() throws Exception {
		System.out.println("start...");
		String inputpath = "D:\\Develop\\Install\\eclipse_pristine\\workspace\\fortest\\src\\main\\resources\\abc.txt";
		String outputpath = "D:\\Develop\\Install\\eclipse_pristine\\workspace\\fortest\\src\\main\\resources\\abc2.txt";
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputpath), "UTF-8"));
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputpath), "UTF-8"));
		String line = null;
		while((line = br.readLine()) != null){
			line = replaceLineMysqlMigrate(line);
			if(line != null){
				pw.println(line);
			}
		}
		br.close();
		pw.close();
		System.out.println("outputpath:"+outputpath);
		System.out.println("finish...");
	}
	
	public static String replaceLineSourceTableGenerate(String line){
		if(line.indexOf("Optional.ofNullable") < 0){
//		if(line.matches(".*CREATE TABLE `(.*)`.*")){
//			line = RegexUtil.replaceAll(line, "CREATE TABLE `(.*)`.*", (matchStr, matchI, matcher) -> {
//				String r2 = matcher.group(1).substring(0,1); 
//				String matchg2 = RegexUtil.replaceAll(matcher.group(1).substring(1), "_(\\w)", (matchStr2, matchI2, matcher2) -> {
//					return matcher2.group(1).toUpperCase();
//				});
//				return "table_"+matcher.group(1)+"="+r2.toUpperCase()+matchg2+",Dao";
////				return "\t\r\t@Autowired\r\tprivate " + r2.toUpperCase() + matchg2 + "Service " + r2 + matchg2 + "Service;";
////				return "services.put(\""+r2.toUpperCase()+matchg2+"\", "+r2 + matchg2+"Service);";
//			});
			return null;
		}
		return line;
	}
	
	private static int allL = 0;
	public static String replaceCreateSql(String line){
		int length = 50;
		if(line.matches("(?=.*1\\s(未见异常|正常|是|无))(?=.*2\\s(异常|否|有))^.*$")){
			//正常,异常项,是否 有无项
			length = 4;
		}else if(line.matches("(?=.*1)(?=.*2)(?=.*3)(?=.*8)^.*$")){
			//包含1 2 3 8分类的
			length = 50;
		}else if(line.matches("(?=.*1)(?=.*2)(?=.*3)^.*$")){
			//包含1 2 3分类的(不含8){
			length = 15;
		}else if(line.matches("(?=.*(fzjcOther|otherDisHave|riskFactorsOther))^.*$")){
//			System.out.println(line);
			length = 200;
		}
		allL += length*3;
		line = line.replaceAll("\"(.*)\": \"(.*)\",{0,1}", "  `$1` varchar("+length+") DEFAULT NULL COMMENT '$2',");
		System.out.println(allL);
		return line;
	}
	
	public static String replaceLine(String line){
		if(line.matches("	private \\w* (\\w*);")){
			line = line.replaceAll("	private \\w* (\\w*);", "$1");
//			line = RegexUtil.replaceAll(line, "	private \\w* (\\w*);", (matchStr, matchI, matcher) -> {
//				if(Integer.parseInt(matcher.group(1)) > 1000){
//					return "text,";
//				}else{
//					return matchStr;
//				}
//			});
		}else{
			line = null;
		}
//		if(line.matches(".*float\\((\\d*)\\),.*")){
//			line = line.replaceAll("float\\((\\d*)\\),", "float\\($1,0\\),");
//		}
		
//		line = RegexUtil.replaceAll(line, "_([a-z])", (matchStr, matchI, matcher) -> {
//			return matcher.group(1).toUpperCase();
//		});
		
//		line = RegexUtil.replaceAll(line, "VALUES \\((\\d*), (\\d*),", (matchStr, matchI, matcher) -> {
//			return "VALUES \\("+matcher.group(1)+", "+(Integer.parseInt(matcher.group(2))+1)+",";
//		});
		
//		line = RegexUtil.replaceAll(line, ".*", (matchStr, matchI, matcher) -> {
//			if(StringUtils.isBlank(matchStr)){
//				return "";
//			}
////			return "\r@Autowired\rprivate "+matcher.group()+"Service "+matcher.group().toLowerCase()+"Service;\r";
////			return "services.put(\""+matcher.group()+"\", "+matcher.group().toLowerCase()+"Service);\r";
//			return "classes.put(\""+matcher.group()+"\", "+matcher.group()+".class);\r";
//		});
		return line;
	}

	public static String replaceLineMysqlMigrate(String line){
		if(line.matches(".*CREATE TABLE `(.*)`  \\(.*")){
			line = line.replaceAll(".*CREATE TABLE `(.*)`  \\(.*",
						  "start=`date +%s`\r"
						+ "echo \"-----------------------------------------------------------------------------------------------$1 start... `date +'%Y-%m-%d %H:%M:%S'`\"\r"
//						+ "bin/mysqldump -uroot -pGL123#@! dataload $1 | mysql -h 121.37.195.229 -P27894 -C -uroot -pGL123#@! dataload\r"
//						+ "bin/mysqldump -uroot -proot@123#@! HDO $1 | mysql -h 211.159.185.18 -P3306 -C -uroot -p0123456 HDO\r"
						+ "mysqldump -uroot -proot@123#@! HDO $1 | mysql -h 211.159.185.18 -P3306 -C -uroot -p0123456 HDO\r"
						+ "end=`date +%s`\r"
						+ "echo \"-----------------------------------------------------------------------------------------------$1 finish... `date +'%Y-%m-%d %H:%M:%S'` 用时:\\$((\\$end-\\$start))\"");
		}else{
			return null;
		}
		return line;
	}
	public static String replaceLineMysql(String line){
		CallBack cb = new CallBack() {
			@Override
			public String replace(String matchStr, int matchI, Matcher matcher) {
				if(currentRow == 0){
					return matcher.group(1);
				}else{
					return ","+matcher.group(1);
				}
			}
		};
		if(line.matches(".*CREATE TABLE `(.*)`  \\(.*")){
//			line = RegexUtil.replaceAll(line, ".*CREATE TABLE `(.*)`  \\(.*", (matchStr, matchI, matcher) -> {
//				currentTable = matcher.group(1);
//				currentRow = 0;
//				return "select ";
//			});
		}else if(line.startsWith("  `")){
			line = RegexUtil.replaceAll(line, "  `(.*)` .*", cb);
			currentRow++;
		}else if(line.startsWith("  PRIMARY KEY")){
			primKeyString = line.replaceAll("  PRIMARY KEY \\((.*)\\).*", "$1");
			return null;
		}else if(line.startsWith(") ENGINE")){
			return "from "+currentTable+"\rorder by "+ primKeyString + "\r";
		}else{
			return null;
		}
		return line;
	}
	
	public static String replaceLineOracle(String line){
		CallBack cb = new CallBack() {
			@Override
			public String replace(String matchStr, int matchI, Matcher matcher) {
				if(currentRow == 0){
					if(matcher.group(2).startsWith("NVARCHAR2")){
						return "Translate("+matcher.group(1)+" USING CHAR_CS) AS "+matcher.group(1);
					}
					return matcher.group(1);
				}else{
					if(matcher.group(2).startsWith("NVARCHAR2")){
						return ",Translate("+matcher.group(1)+" USING CHAR_CS) AS "+matcher.group(1);
					}
					return ","+matcher.group(1);
				}
			}
		};
		if(line.matches(".*CREATE TABLE \"ULANE\".\"(.*)\".*")){
//			line = RegexUtil.replaceAll(line, ".*CREATE TABLE \"ULANE\".\"(.*)\".*", (matchStr, matchI, matcher) -> {
//				currentTable = matcher.group(1);
//				currentRow = 0;
//				return "select ";
//			});
		}else if(line.startsWith("	\"")){
			line = RegexUtil.replaceAll(line, "	\"(.*)\" (.*)", cb);
			currentRow++;
		}else if(line.startsWith("   (	\"")){
			line = RegexUtil.replaceAll(line, "   \\(	\"(.*)\" (.*)", cb);
			currentRow++;
		}else if(line.startsWith("   ) SEGMENT CREATION")){
			return "from "+currentTable+"\r";
		}else{
			return null;
		}
		return line;
	}
	
	/**
	 * 适配器标准推送数据整理
	 * @param args
	 * @throws Exception
	 */
	public static void adapter() throws Exception {
		System.out.println("start...");
		String splitStr = ",";//	
		String inputpath = "D:\\Develop\\Install\\eclipse_pristine\\workspace\\fortest\\src\\main\\resources\\abc.csv";
		String outputpath = "D:\\Develop\\work\\Z_wy\\prg\\0_common\\01_公卫3.0数据转换\\A003-新田\\20200414_数据整理\\标准推送数据_20200414.sql";
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputpath), "UTF-8"));
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputpath), "UTF-8"));
		String line = null;
		while((line = br.readLine()) != null){
			String[] values = line.split(splitStr);
			if(!values[values.length-2].endsWith("}")){
				line = line + br.readLine();
				values = line.split(splitStr);
//				System.out.println(JSON.toJSONString(values));
			}
			line = "INSERT INTO `message_consume_exception`(`id`, `gmt_created`, `gmt_modified`, `is_deleted`, `consumer_group`, `topic`, `message_pkid`, `exception_type`, `remark`, `data_id`, `data_doctor_id`, `data_idcard`, `data_sign_province_code`, `data_sign_city_code`, `data_sign_dstrict_code`, `data_sign_township_code`, `data_sign_village_code`, `unique_key`, `message`, `state`) VALUES ("
					+ values[0] + ","
					+ "'" + values[1] + "',"
					+ "'" + values[2] + "',"
					+ values[3] + ","
					+ "'" + values[4] + "',"
					+ "'" + values[5] + "',"
					+ values[6] + ","
					+ values[7] + ","
					+ "'" + values[8];
			int i = 9;
			while(!values[i].startsWith("{")){
				line = line + "," + values[i];
				i++;
			}
			line += "',";
			String value9 = "";//json格式
			for(int length=values.length; i<length-1; i++){
				if(!values[i].startsWith("\\")
						&& !values[i].startsWith("\"")
						&& !values[i].startsWith("{")){
//						System.out.println(values[i]);
					value9 = value9 + values[i].replaceFirst("(\\w*):", "\"$1\":");
//						System.out.println("to:----------------------"+values[i].replaceFirst("(\\w*):", "\"$1\":") + ",");
				}else{
					value9 += values[i];
				}
				//倒数二个不加逗号
				if(i < length - 2){
					value9 += ",";
				}
			}
			
			//测试json格式是否正确
			JSONObject obj = JSONObject.parseObject(value9.replaceAll("(\\\\)+\"", "\\\\\\\""));
//			System.out.println(obj.toJSONString());
			JSONObject objd = JSONObject.parseObject(obj.getString("data"));
//			System.out.println(objd.toJSONString());
			JSONObject objd2 = objd.getJSONObject("data");
//			System.out.println(objd2.toJSONString());

			line = line + "'" + obj.getString("dataId") + "',"
					+ "'" + obj.getString("dataDoctorId") + "',"
					+ "'" + obj.getString("dataIdcard") + "',"
					+ "'" + obj.getString("dataSignProvinceCode") + "',"
					+ "'" + obj.getString("dataSignCityCode") + "',"
					+ "'" + obj.getString("dataSignDistrictCode") + "',"
					+ "'" + obj.getString("dataSignTownshipCode") + "',"
					+ "'" + obj.getString("dataSignVillageCode") + "',"
					+ "'" + obj.getString("uniqueKey") + "',";
			
//			line = (line + "'" + value9 + "'," + values[values.length-1] + ")").replace("\\\\\"", "\\\\\\\"");
			line = (line + "'" + value9 + "'," + values[values.length-1] + ")").replaceAll("(\\\\)+\"", "\\\\\\\\\\\\\\\"");//java字符串消耗一个\, 正则消耗一个\, 所以\\\\表示一个\
			pw.println(line+";");
//			break;
		}
		br.close();
		pw.close();
		System.out.println("finish...");
	}
	
	
	
	
	
}
