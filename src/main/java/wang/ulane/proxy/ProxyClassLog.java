package wang.ulane.proxy;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import wang.ulane.log.LogAspect;

public class ProxyClassLog extends ProxyClass{
	
	static {
		ProxyClass.addRelateClassPath(LogAspect.class);
		ProxyClass.addRelateClassPath(JSON.class);
	}
	
	@SuppressWarnings("deprecation")
	public static void proxyMethodLog(String classFullName, String methodName, @SuppressWarnings("rawtypes") Class[] params){
		proxyMethod(classFullName, methodName, params, 
				generateBeforeStr(classFullName, methodName, params), 
				generateAfterStr(classFullName, methodName, params));
	}

	public static void proxyMethodLog(Map<String, List<MethodParam>> map){
		for(String key:map.keySet()){
			for(MethodParam mp:map.get(key)){
				if(mp.getCustomFullMethodName() == null){
					mp.setBeforeBody(generateBeforeStr(key, mp.getMethodName(), mp.getParams()));
					mp.setAfterBody(generateAfterStr(key, mp.getMethodName(), mp.getParams()));
				}
			}
			proxyMethod(key, map.get(key).toArray(new MethodParam[]{}));
		}
	}
	
	public static void proxyMethodLog(String classFullName, MethodParam... mps){
		for(MethodParam mp:mps){
			mp.setBeforeBody(generateBeforeStr(classFullName, mp.getMethodName(), mp.getParams()));
			mp.setAfterBody(generateAfterStr(classFullName, mp.getMethodName(), mp.getParams()));
		}
		proxyMethod(classFullName, mps);
	}
	
	public static String generateBeforeStr(String classFullName, String methodName, @SuppressWarnings("rawtypes") Class[] params){
		StringBuilder beforeSb = new StringBuilder("java.util.List arr = new java.util.ArrayList();");
		if(params != null && params.length > 0){
			for(int i=0,length=params.length; i<length; i++){
				if(PrimitiveEnum.checkPrimitive(params[i].getName())){
					beforeSb.append("arr.add(");
					beforeSb.append(PrimitiveEnum.valueOf("_"+params[i].getName()).getWrapper());
					beforeSb.append(".valueOf(arg").append(i).append("));");
				}else{
					beforeSb.append("arr.add(arg").append(i).append(");");
				}
			}
		}
//		beforeSb.append("StringBuilder targetMethod = new StringBuilder(\"").append(classFullName).append("\").append(\":\").append(\"").append(methodName).append("\");");
		beforeSb.append("String targetMethod = new StringBuilder(\"").append(classFullName).append("\").append(\":\").append(\"").append(methodName).append("\").toString();");
//		//字符串全常量直接用+，编译时自动合并
//		beforeSb.append("long start = System.currentTimeMillis();"
//					+	"StringBuilder startStr = new StringBuilder(\"开始\").append(Thread.currentThread().getName()).append(\"调用--> \").append(targetMethod).append(\" 参数:\").append(com.alibaba.fastjson.JSON.toJSONString(arr));"
//					+	"System.out.println(startStr);");
		beforeSb.append("long start = System.currentTimeMillis();");
		beforeSb.append(LogAspect.class.getCanonicalName()).append(".printLogBeforeProceed(targetMethod,com.alibaba.fastjson.JSON.toJSONString(arr));");
		return beforeSb.toString();
	}
	
	public static String generateAfterStr(String classFullName, String methodName, @SuppressWarnings("rawtypes") Class[] params){
//		String afterSb = "StringBuilder endStr = new StringBuilder(\"结束\").append(Thread.currentThread().getName()).append(\"调用<-- \").append(targetMethod).append(\" 返回值:\").append(String.valueOf(result)).append(\" 耗时:\").append(System.currentTimeMillis() - start).append(\"ms\");"
//						+"System.out.println(endStr);";
		StringBuilder afterSb = new StringBuilder(LogAspect.class.getCanonicalName());
		afterSb.append(".printObjectLogAfterProceed(targetMethod, System.currentTimeMillis() - start, ");
		//内部会有正则替换
		afterSb.append("${returnwrapper-result}");
		afterSb.append(");");
		return afterSb.toString();
	}
}
