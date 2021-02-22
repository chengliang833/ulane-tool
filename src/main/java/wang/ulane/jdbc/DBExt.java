package wang.ulane.jdbc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DBExt {

	public static Param param(String k, Object v){
		return new Param(k, v);
	}

	//custom
	public static Object readMapOneObject(String field, String sql, Object... params){
		Map<String,Object> map = readMapOne(sql, params);
		return map.get(field);
	}

	public static Object readMapOneValue(String sql, Object... params){
		Map<String,Object> map = readMapOne(sql, params);
		return map.get("VAL");
	}

	public static String readMapString(String field, String sql, Object... params){
		Map<String,Object> map = readMapOne(sql, params);
		return map.get(field)==null?null:map.get(field).toString();
	}

	public String readMapStringValue(String sql, Object... params){
		return readMapString("VAL", sql, params);
	}

	//DButil
	public static List<Map<String, Object>> readList(String sql, Object... params){
		return DBUtil.readList(sql, params);
	}

	public static int readMapCount(String sql, Object... params){
		Map<String,Object> map = DBUtil.readMap(sql, params);
		return ((BigDecimal)map.get("VAL")).intValue();
	}

	public static Map<String,Object> readMapOne(String sql, Object... params){
		Map<String,Object> map = DBUtil.readMap(sql, params);
		if(map == null){
			throw new DBException("没有找到对应值");
		}
		return map;
	}

}
