package wang.ulane.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;

import wang.ulane.proxy.MethodParam;
import wang.ulane.proxy.ProxyClass;
import wang.ulane.proxy.ProxyPoint;

public class FastJsonChangeDefine {
	
	private static SerializeConfig sc = null;
	
	public static void init(){
		ProxyClass.addRelateClassPath(JSON.class);
		ProxyClass.proxyMethod("com.alibaba.fastjson.serializer.JSONSerializer", 
				new MethodParam("getObjectWriter", "wang.ulane.json.FastJsonChangeDefine.execute", 
						new Class[]{Class.class}));
		sc = new SerializeConfig(true);
	}
	
	public static Object execute(ProxyPoint point) throws Exception{
		Class<?> beanType = (Class<?>) point.getArgs()[0];
        FieldBaseAnnotation fba = beanType.getAnnotation(FieldBaseAnnotation.class);
        //有传入的SerializeConfig时，以传入的为准。fieldBase默认false，若注解手动指定false
        if(((JSONSerializer)point.getBean()).getMapping() == SerializeConfig.globalInstance
        		&& fba != null && fba.value()){
//        	point.getArgs()[3] = fba.value();
        	return sc.getObjectWriter(beanType);
        }
		return point.proceed();
	}
	
}
