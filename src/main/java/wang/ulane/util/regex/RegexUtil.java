package wang.ulane.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	
	public static void main(String[] args) {
		System.out.println(fieldToUpper());
	}
	public static String fieldToUpper(){
		String line = "team_id, org_code, team_name, team_status, team_lead_id, members_cnt, tm_smp";
		String[] lines = line.split(",");
		String rline = "";
		for(String linesub:lines){
			if(linesub.indexOf("_") >= 0){
				linesub = linesub + " " + RegexUtil.replaceAll(linesub.replaceAll("\\s*", ""), "_\\w", new CallBack() {
						public String replace(String matchStr, int matchI, Matcher matcher) {
							return matchStr.substring(1).toUpperCase();
						}
				});
			}
			rline = rline + linesub + ",";
		}
		return rline;
	}
	public static String test(){
		String line = "dfsfs23d4s5d46657f77";
//		line = RegexUtil.replaceAll(line, "\\d", new ReplaceCallBack() {
//			public String replace(String matchStr, int matchI, Matcher matcher) {
//				return "["+matchStr+"]";
//			}
//		});
		line = RegexUtil.replaceAll(line, "\\d", new CallBack() {
					public String replace(String matchStr, int matchI, Matcher matcher) {
						return "["+matchStr+"]";
					}
		});
		return line;	
	}
	
	/**
	 * 将String中的所有regex匹配的字串全部替换掉
	 * 
	 * @param string 待替换的字符串
	 * @param regex 替换查找的正则表达式
	 * @param replaceBack 替换函数
	 * @return
	 */
	public static String replaceAll(String string, String regex, RegexUtil.CallBack replaceBack) {
		return replaceAll(string, Pattern.compile(regex), replaceBack);
	}

	/**
	 * 将String中的所有pattern匹配的字串替换掉
	 * 
	 * @param string 待替换的字符串
	 * @param pattern 替换查找的正则表达式对象
	 * @param replaceBack 替换函数
	 * @return
	 */
	public static String replaceAll(String string, Pattern pattern, RegexUtil.CallBack replaceBack) {
		if (string == null) {
			return null;
		}
		Matcher m = pattern.matcher(string);
		if (m.find()) {
			StringBuffer sb = new StringBuffer();
			int index = 0;
			while (true) {
				m.appendReplacement(sb, replaceBack.replace(m.group(0), index++, m));
				if (!m.find()) {
					break;
				}
			}
			m.appendTail(sb);
			return sb.toString();
		}
		return string;
	}

	/**
	 * 将String中的regex第一次匹配的字串替换掉
	 * 
	 * @param string 待替换的字符串
	 * @param regex 替换查找的正则表达式
	 * @param replaceBack 替换函数
	 * @return
	 */
	public static String replaceFirst(String string, String regex, RegexUtil.CallBack replaceBack) {
		return replaceFirst(string, Pattern.compile(regex), replaceBack);
	}

	/**
	 * 将String中的pattern第一次匹配的字串替换掉
	 * 
	 * @param string 待替换的字符串
	 * @param pattern 替换查找的正则表达式对象
	 * @param replaceBack 替换函数
	 * @return
	 */
	public static String replaceFirst(String string, Pattern pattern, RegexUtil.CallBack replaceBack) {
		if (string == null) {
			return null;
		}
		Matcher m = pattern.matcher(string);
		StringBuffer sb = new StringBuffer();
		if (m.find()) {
			m.appendReplacement(sb, replaceBack.replace(m.group(0), 0, m));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 将text转化为特定的字串返回
	 * 
	 * @param matchStr 指定的匹配到的字符串
	 * @param matchI 替换的匹配到的序号,0开始
	 * @param matcher Matcher对象
	 * 			matcher.group(i), 即 $i,$0=matchStr,$1=第一个()
	 * 	ep.	line = RegexUtil.replaceAll(line, "INSERT INTO `(.*)`", new ReplaceCallBack() {
	 * 			public String replace(String matchStr, int matchI, Matcher matcher) {
	 * 				return "INSERT INTO `"+matcher.group(1).toUpperCase()+"`";
	 * 			}
	 * 		});
	 * @return
	 */
	public interface CallBack{
		public String replace(String matchStr, int matchI, Matcher matcher);
	}
}
