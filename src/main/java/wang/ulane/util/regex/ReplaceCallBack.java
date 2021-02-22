package wang.ulane.util.regex;

import java.util.regex.Matcher;

/**
 * 字符串替换的回调接口
 *
 */
public interface ReplaceCallBack {
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
	public String replace(String matchStr, int matchI, Matcher matcher);
}
