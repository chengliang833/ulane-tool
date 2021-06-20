package wang.ulane.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;

import wang.ulane.proxy.MethodParam;
import wang.ulane.proxy.ProxyClass;
import wang.ulane.proxy.ProxyPoint;

public class JacksonChangeDefine {

	public static void init(){
		ProxyClass.addRelateClassPath(ObjectMapper.class);
		ProxyClass.proxyMethod("com.fasterxml.jackson.databind.introspect.BasicClassIntrospector", 
				new MethodParam("constructPropertyCollector", "wang.ulane.json.JacksonChangeDefine.execute", 
						new Class[]{MapperConfig.class, AnnotatedClass.class, JavaType.class, boolean.class, String.class}));
	}
	
	public static Object execute(ProxyPoint point) throws Exception{
		MapperConfig<?> config = (MapperConfig<?>) point.getArgs()[0];
		JavaType type = (JavaType) point.getArgs()[2];
		StdNameAnnotation sna = type.getRawClass().getAnnotation(StdNameAnnotation.class);
        if(sna != null){
        	if(sna.value()){
        		config = config.with(MapperFeature.USE_STD_BEAN_NAMING);
        	}else{
        		config = config.without(MapperFeature.USE_STD_BEAN_NAMING);
        	}
        	point.getArgs()[0] = config;
        }
		return point.proceed();
	}
	
}
