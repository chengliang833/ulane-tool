package wang.ulane.json;

import com.alibaba.fastjson.serializer.NameFilter;

public class JSONUpperNameFilter implements NameFilter {
	
	@Override
	public String process(Object object, String name, Object value) {
		if (name == null || name.length() == 0) {
			return name;
		}
		return name.toUpperCase();
	}
}
