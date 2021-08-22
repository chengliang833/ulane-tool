package wang.ulane.util.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
	public static void main(String[] args) {
		String sql = "select * from a where b = #{p1} and b = ${p2} and c = #{p1} and c = ${p3} and 1=1";
		StringBuffer sb = new StringBuffer();
		Pattern pat = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher m = pat.matcher(sql);
//		System.out.println(m.matches());
//		System.out.println(m.lookingAt());
		while(m.find()){
			for(int i=0,length=m.groupCount()+1; i<length; i++){
				//group(0),匹配到的整体字符串，group(1)-$1
				System.out.println("group("+i+"):"+m.group(i));
			}
			m.appendReplacement(sb, "'a'");
			System.out.println(sb.toString());
		}
		if(sb.length() > 0){
			m.appendTail(sb);
			sql = sb.toString();
		}
		
		System.out.println(sql);
		pat = Pattern.compile("#\\{(.*?)\\}");
		m = pat.matcher(sql);
		while(m.find()){
			for(int i=0,length=m.groupCount()+1; i<length; i++){
				//group(0),匹配到的整体字符串，group(1)-$1
				System.out.println("group("+i+"):"+m.group(i));
			}
		}
	}
	
}
