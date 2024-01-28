package wang.ulane.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

	public static boolean isNull(String str){
		return isEmpty(str) || str.equalsIgnoreCase("null");
	}
	public static boolean isEmpty(String str){
		return str == null || str.equals("");
	}

	public static String samePreStr(List<String> strs){
		if(strs == null){
			throw new IllegalArgumentException("未找到字符串列表");
		}
		strs = strs.stream().filter(o->!StringUtils.isEmpty(o)).collect(Collectors.toList());
		if(strs.size() == 0){
			throw new IllegalArgumentException("未找到字符串列表");
		}

		int i = 0;
		out:while(true){
			Character onceChar = null;
			for(String str:strs){
				if(onceChar == null){
					onceChar = str.charAt(i);
				}else{
					if(onceChar != str.charAt(i)){
						break out;
					}
				}
			}
			i++;
		}
		return strs.get(0).substring(0, i);
	}

}
