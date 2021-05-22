package wang.ulane.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;

public class DataUtil {
	
	protected static Logger log = LoggerFactory.getLogger(DataUtil.class);

	/**
	 * 数字转换为千分位
	 * @param num
	 * @param cent
	 * @return
	 */
	public static String toThousands(Object num, int cent) {  
		if(num == null){
			return "";
		}
		String numStr = String.valueOf(num);
    	String[] numArr = numStr.split("\\.");
		if(cent == 0){
			return numArr[0].replaceAll("(\\d)(?=(?:\\d{3})+$)", "$1,");
		}else{
			if(StringUtils.isBlank(numArr[1])){
				numArr[1] = "";
			}
			if(numArr[1].length() < cent){
				for(int i=0,length=cent-numArr[1].length(); i<length; i++){
	    			numArr[1] += "0";
	    		}
			}
			return numArr[0].replaceAll("(\\d)(?=(?:\\d{3})+$)", "$1,") + "." + numArr[1].substring(0, cent);
		}
	}
	

	public static JSONArray listToTree(JSONArray arr, String id, String pid, String child) {
		JSONArray r = new JSONArray();
		JSONObject hash = new JSONObject();
		// 将数组转为Object的形式，key为数组中的id
		for (int i = 0; i < arr.size(); i++) {
			JSONObject json = (JSONObject) arr.get(i);
			hash.put(json.getString(id), json);
		}
		// 遍历结果集
		for (int j = 0; j < arr.size(); j++) {
			// 单条记录
			JSONObject aVal = (JSONObject) arr.get(j);
			// 在hash中取出key为单条记录中pid的值
			JSONObject hashVP = aVal.get(pid) == null ? null :(JSONObject) hash.get(aVal.get(pid));
			// 如果记录的pid存在，则说明它有父节点，将她添加到孩子节点的集合中
			if (hashVP != null) {
				// 检查是否有child属性
				if (hashVP.get(child) != null) {
					JSONArray ch = (JSONArray) hashVP.get(child);
					ch.add(aVal);
					hashVP.put(child, ch);
				} else {
					JSONArray ch = new JSONArray();
					ch.add(aVal);
					hashVP.put(child, ch);
				}
			} else {
				r.add(aVal);
			}
		}
		return r;
	}
	
	public JSONObject copyObj(Object obj){
		return JSONObject.parseObject(JSON.toJSONString(obj));
	}

	public String allToJsonString(Object obj){
		return JSON.toJSONString(obj);
	}

	public String listToJsonString(List list){
		return JSON.toJSONString(list, new SerializeFilter[] {formDateFilter()});
	}

	public String objectToJsonString(Object obj){
		return JSON.toJSONString(obj, new SerializeFilter[] {formDateFilter()});
	}

	public JSONArray listToJson(List list){
		return (JSONArray)JSON.parse(JSON.toJSONString(list, new SerializeFilter[] {formDateFilter()} ));
	}
	public JSONObject objectToJson(Object obj){
		return (JSONObject)JSON.parse(JSON.toJSONString(obj, new SerializeFilter[] {formDateFilter()} ));
	}

	public ValueFilter nullStrFilter(){
		return new ValueFilter() {
			@Override
			public Object process(Object object, String name, Object value) {
				if(value == null){
					return "";
				}
                return value;
			}
		};
	}
	public ValueFilter formDateFilter(){
		return new ValueFilter() {
			@Override
			public Object process(Object object, String name, Object value) {
				if(value == null){
					return null;
				}
				if (value instanceof Date) {
					try {
						return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value);
					} catch (Exception e) {
						log.debug(e.getMessage(), e);
					}
				}else if(value instanceof Timestamp){
					try {
						return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Timestamp) value);
					} catch (Exception e) {
						log.debug(e.getMessage(), e);
					}
				}
                return value;
			}
		};
	}
	
//	public static void main(String[] args) {
////		TUserPic tup = new TUserPic();
////		tup.setCreateDate(new Date());
//////		System.out.println(new DataUtil().objectToJson(tup));
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("dsfd", new Date());
//		
//		Map<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("map2date", new Date());
//		map.put("map2", map2);
//		System.out.println(new DataUtil().objectToJson(map));
//		
//		System.out.println(new DataUtil().browseProperties(map2));
////		List<Object> list = new ArrayList<Object>();
////		list.add(tup);
////		list.add(map);
////		System.out.println(new DataUtil().listToJson(list));
//	}
	
	//json配置
	/*yu public ParserConfig getJsonConfig(){
	    JsonConfig jsonConfig = new JsonConfig();  
	    //去除双向关联
//		jsonConfig.setExcludes(new String[]{"handler","hibernateLazyInitializer"});
		//将日期格式化
	    jsonConfig.registerJsonValueProcessor(Timestamp.class,  
	            new JsonValueProcessor() {  
	                public Object processObjectValue(String paramString, Object paramObject, JsonConfig paramJsonConfig) {  
	                    if (paramObject == null) {  
	                        return null;  
	                    }  
	                    String ret = null;  
	                    try {  
	                        ret = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Timestamp) paramObject);  
	                    } catch (Exception e) {  
	                    	log.debug(e.getMessage(), e);
	                        ret = new SimpleDateFormat("yyyy-MM-dd").format((Timestamp) paramObject);  
	                    }  
	                    return ret;  
	                }  
	                
	                public Object processArrayValue(Object paramObject, JsonConfig paramJsonConfig) {  
	                    return null;  
	                }  
	            });
	    //json字符串格式化成json对象。
//	    jsonConfig.registerJsonValueProcessor(String.class,  
//	            new JsonValueProcessor() {  
//	                public Object processObjectValue(String paramString, Object paramObject, JsonConfig paramJsonConfig) {  
//	                    if (paramObject == null) {  
//	                        return null;  
//	                    }  
//	                    String ret = (String) paramObject;  
//	                    JSON json = null;
//	                    if(ret.startsWith("{") && ret.endsWith("}")){
//	                    	json = JSONObject.fromObject(ret);
//	                    	System.out.println("obj:"+json.toString());
//	                    }else if(ret.startsWith("[") && ret.endsWith("]")){
//	                    	json = JSONArray.fromObject(ret);
//	                    	System.out.println("arr:"+json.toString());
//	                    }else{
//	                    	return ret;
//	                    }
//	                    return json;
//	                }  
//	                
//	                public Object processArrayValue(Object paramObject, JsonConfig paramJsonConfig) {  
//	                    return null;  
//	                }  
//	            });
	    return jsonConfig;
	}*/
}
