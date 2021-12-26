package wang.ulane.util;

public class StringUtils {
	
	public static boolean isNull(String str){
		return str == null || str.equals("") || str.equalsIgnoreCase("null");
	}
	
}
