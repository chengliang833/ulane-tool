package wang.ulane.util.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
	public static void main(String[] args) {
		Pattern pat = Pattern.compile("#\\{(.*?)\\}");
		Matcher m = pat.matcher("select * from a where b = #{p1} and b = #{p2} and c = #{p1}");
//		System.out.println(m.groupCount());
		while(m.find()){
			for(int i=0,length=m.groupCount()+1; i<length; i++){
				//group(0),匹配到的整体字符串，group(1)-$1
				System.out.println(m.group(i));
			}
		}
	}
	
}
