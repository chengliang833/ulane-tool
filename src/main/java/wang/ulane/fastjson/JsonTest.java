package wang.ulane.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PascalNameFilter;

public class JsonTest {
	public static void main(String[] args) {
		JSONObject obj = new JSONObject();
		obj.put("sdfd", "asd");
		obj.put("sef", "gdsfg");
		System.out.println(obj.toJSONString());
		JsonBean b = JSON.parseObject(obj.toJSONString(), JsonBean.class);
		System.out.println(JSON.toJSONString(b));
		System.out.println(JSON.toJSONString(b, new PascalNameFilter()));
	}
}
