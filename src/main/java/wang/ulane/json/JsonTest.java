package wang.ulane.json;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {
	private static ObjectMapper om = new ObjectMapper();
	
	static {
		FastJsonChangeDefine.init();
//		JacksonChangeDefine.init();
		om.enable(MapperFeature.USE_STD_BEAN_NAMING);
	}
	
	public static void main(String[] args) throws JsonProcessingException {
		JSONObject obj = new JSONObject();
		obj.put("sdfd", "asd");
		obj.put("sef", "gdsfg");
		JsonBean b = JSON.parseObject(obj.toJSONString(), JsonBean.class);
		b.setSdfd("sdfds");
		System.out.println(obj.toJSONString());
		System.out.println(JSON.toJSONString(b));
		//字段首字母大写
		System.out.println(JSON.toJSONString(b, new PascalNameFilter()));
		//使用完全原字段
		System.out.println(JSON.toJSONString(b, new SerializeConfig(true)));
		
		System.out.println(om.writeValueAsString(b));
		
		Map<String, Object> map = new HashMap<>();
		map.put("obj", JSON.parseObject(JSON.toJSONString(b, new PascalNameFilter())));
		map.put("obj2", b);
		System.out.println(JSON.toJSONString(map));
		
		
	}
}
