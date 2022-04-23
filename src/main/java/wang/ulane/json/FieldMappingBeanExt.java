package wang.ulane.json;

import com.alibaba.fastjson.annotation.JSONField;

public class FieldMappingBeanExt extends FieldMappingBean {

	@JSONField(name="aa")
	public void setA(String a) {
		super.setA(a);
	}

	@JSONField(name="bb")
	public void setB(String b) {
		super.setB(b);
	}
	
}
