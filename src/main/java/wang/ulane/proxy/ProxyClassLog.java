package wang.ulane.proxy;

public class ProxyClassLog extends ProxyClass{
	
	public static void proxyMethodLog(String classFullName, String methodName, @SuppressWarnings("rawtypes") Class[] params){
		proxyMethod(classFullName, methodName, params, 
				generateBeforeStr(classFullName, methodName, params), 
				generateAfterStr(classFullName, methodName));
	}
	
	public static void proxyMethodLog(String classFullName, MethodParam... mps){
		for(MethodParam mp:mps){
			mp.setBeforeBody(generateBeforeStr(classFullName, mp.getMethodName(), mp.getParams()));
			mp.setAfterBody(generateAfterStr(classFullName, mp.getMethodName()));
		}
		proxyMethod(classFullName, mps);
	}
	
	public static String generateBeforeStr(String classFullName, String methodName, @SuppressWarnings("rawtypes") Class[] params){
		StringBuilder beforeSb = new StringBuilder("com.alibaba.fastjson.JSONArray arr = new com.alibaba.fastjson.JSONArray();");
		if(params != null && params.length > 0){
			for(int i=0,length=params.length; i<length; i++){
				beforeSb.append("arr.add(arg").append(i).append(");");
			}
		}
		beforeSb.append("StringBuilder targetMethod = new StringBuilder(\"").append(classFullName).append("\").append(\":\").append(\"").append(methodName).append("\");");
		//字符串全常量直接用+，编译时自动合并
		beforeSb.append("long start = System.currentTimeMillis();"
					+	"StringBuilder startStr = new StringBuilder(\"开始\").append(Thread.currentThread().getName()).append(\"调用--> \").append(targetMethod).append(\" 参数:\").append(arr.toJSONString());"
					+	"System.out.println(startStr);");
		return beforeSb.toString();
	}
	
	public static String generateAfterStr(String classFullName, String methodName){
		String afterSb = "StringBuilder endStr = new StringBuilder(\"结束\").append(Thread.currentThread().getName()).append(\"调用<-- \").append(targetMethod).append(\" 返回值:\").append(String.valueOf(result)).append(\" 耗时:\").append(System.currentTimeMillis() - start).append(\"ms\");"
						+"System.out.println(endStr);";
		return afterSb;
	}
}
