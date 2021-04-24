package wang.ulane.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;

public class ProxyClass {
	
	/**
	 * 仅编译需要，要加载的classPath，通过main、@Test方法启动（包括springboot）不需要指定（加了也没关系）。
	 * 通过tomcat等启动，需要指定默认的classpath（classes路径）（*.class.getResource("/")）
	 */
	private static Set<String> relateClassPaths = new HashSet<String>();
	
	public static void proxyMethod(String classFullName, String methodName, @SuppressWarnings("rawtypes") Class[] params, String beforeBody, String afterBody){
		proxyMethod(classFullName, new MethodParam(methodName, params, beforeBody, afterBody));
	}
	
	public static void proxyMethod(String classFullName, MethodParam... mps){
		try {
			ClassPool pool = new ClassPool(true);
			for(String path:relateClassPaths){
				pool.appendClassPath(path);
			}
//			System.out.println("------"+PropLoad.getExcutePath());
//			pool.appendClassPath(JSONArray.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//			pool.appendClassPath(PropLoad.getExcutePath());
			CtClass ct = pool.getCtClass(classFullName);// 加载这个类
			
			for(MethodParam mp:mps){
				proxyMethod(ct, pool, mp.getMethodName(), mp.getParams(), mp.getBeforeBody(), mp.getAfterBody());
			}
			
			// 转为class
			ct.toClass();
			// 释放对象
			ct.detach();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void proxyMethod(CtClass ct, ClassPool pool, String methodName, @SuppressWarnings("rawtypes") Class[] params, String beforeBody, String afterBody) throws Exception{
		CtClass[] ctParams = null;
		if(params != null && params.length > 0){
			ctParams = new CtClass[params.length];
		}
		CtMethod m = getSignMethod(pool, methodName, params, ctParams, ct);
		//无返回值直接前后加就行，放弃，兼容ProxyClassLog前后变量关联
//		if(m.getReturnType().getName().equals("void")){
//			m.insertBefore(beforeBody);
//			m.insertAfter("String result = null;"+afterBody);
//		}else{
			createProxyMethod(m, methodName, ctParams, ct);
			changeOriginalMethod(m, methodName, ctParams, ct, beforeBody, afterBody);
//		}
	}
	
	protected static CtMethod getSignMethod(ClassPool pool, String methodName, @SuppressWarnings("rawtypes") Class[] params, CtClass[] ctParams, CtClass ct) throws NotFoundException{
		CtMethod m = null;
		if(params != null && params.length > 0){
			for(int i=0,length=params.length; i<length; i++){
				ctParams[i] = pool.getCtClass(params[i].getCanonicalName());
			}
			// 获取被修改的方法
			m = ct.getDeclaredMethod(methodName, ctParams);
		}else{
			m = ct.getDeclaredMethod(methodName);
		}
		return m;
	}
	
	/**
	 * 添加代理方法名[methodName]Proxy，其他方法体，本地变量表（形参名）等不变
	 * @param m
	 * @param methodName
	 * @param ctParams
	 * @param ct
	 * @throws Exception
	 */
	private static void createProxyMethod(CtMethod m, String methodName, CtClass[] ctParams, CtClass ct) throws Exception{
		CtMethod m2 = new CtMethod(m.getReturnType(), methodName+"Proxy___", ctParams, ct);
		
//		System.out.println(m.getReturnType().getName());
		
//		m2.getMethodInfo().setAccessFlags(m.getMethodInfo().getAccessFlags() & ~AccessFlag.ABSTRACT | AccessFlag.STATIC);
		m2.getMethodInfo().setAccessFlags(m.getMethodInfo().getAccessFlags());
		m2.getMethodInfo().setCodeAttribute(m.getMethodInfo().getCodeAttribute());
//		m2.setModifiers(m.getModifiers() | Modifier.VARARGS);
		
//		//测试查询形参名
//		for(AttributeInfo attr:m.getMethodInfo().getCodeAttribute().getAttributes()){
//			System.out.println(attr.toString());;
//			if(attr instanceof LocalVariableAttribute){
//				LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute) attr;
//				for(int i=1,length=localVariableAttribute.tableLength(); i<length; i++){
//					System.out.println(localVariableAttribute.variableName(i));
//				}
//			}else{
//				System.out.println(attr.getName());
//			}
//		}
		
		//添加到原class中
		ct.addMethod(m2);
	}

	/**
	 * 创建新的[methodName]替换原方法，设置before和after代码，并调用[methodName]Proxy
	 * @param m
	 * @param methodName
	 * @param ctParams
	 * @param ct
	 * @param beforeBody
	 * @param afterBody
	 * @throws Exception
	 */
	private static void changeOriginalMethod(CtMethod m, String methodName, CtClass[] ctParams, CtClass ct, String beforeBody, String afterBody) throws Exception{
		if(beforeBody == null){
			beforeBody = "";
		}
		if(afterBody == null){
			afterBody = "";
		}
		StringBuilder bodyInvoke = new StringBuilder();
		
		StringBuilder mStr = new StringBuilder("public ");
		//按位与：m.getMethodInfo().getAccessFlags() & 0b1000
		if((m.getMethodInfo().getAccessFlags() & AccessFlag.STATIC) == 8){
			mStr.append("static ");
		}
		
		mStr.append(m.getReturnType().getName()).append(" ");
		mStr.append(methodName).append("(");
		generateParamDeclareInvoke(ctParams, mStr, bodyInvoke);
		mStr.append("){");
		
		String returnTypeName = m.getReturnType().getName();
		if(returnTypeName.equals("void")){
			mStr.append("String result = null;");
			mStr.append(beforeBody);
			mStr.append(methodName).append("Proxy___(").append(bodyInvoke).append(");");
			mStr.append(afterBody);
		}else{
//			Integer.class.getField("TYPE").get(null)//int.class
//			if(m.getReturnType() instanceof CtPrimitiveType){//还是要区分boolean，干脆直接判断
			if(PrimitiveEnum.initPrimitive0.contains(returnTypeName)){
				mStr.append(returnTypeName).append(" result = 0;");
			}else if(PrimitiveEnum.initPrimitiveFalse.equals(returnTypeName)){
				mStr.append(returnTypeName).append(" result = false;");
			}else{
				mStr.append(returnTypeName).append(" result = null;");
			}
			mStr.append(beforeBody);
			mStr.append("result = ");
			mStr.append(methodName).append("Proxy___(").append(bodyInvoke).append(");");
			mStr.append(afterBody);
			mStr.append("return result;");
		}
		mStr.append("}");
		
		String funcStr = mStr.toString();
		if(PrimitiveEnum.checkPrimitive(returnTypeName)){
			funcStr = funcStr.replaceAll("\\$\\{returnwrapper-(.*?)\\}", PrimitiveEnum.valueOf("_"+returnTypeName).getWrapper()+".valueOf($1)");
		}else{
			funcStr = funcStr.replaceAll("\\$\\{returnwrapper-(.*?)\\}", "$1");
		}
		//直接setBody不能对应形参名
//		m3.setBody(methodBody.toString());
//		System.out.println(funcStr);
		CtMethod m3 = CtMethod.make(funcStr, ct);
//		m3.getMethodInfo().setAccessFlags(m3.getMethodInfo().getAccessFlags() | AccessFlag.STATIC);
		//CtMethod.make时static会少一个本地变量this，移到上面字符串中判断static
//		m3.getMethodInfo().setAccessFlags(m.getMethodInfo().getAccessFlags());
//		m3.setModifiers(m3.getModifiers() | Modifier.VARARGS);
		
		ct.removeMethod(m);
		ct.addMethod(m3);
	}
	
	/**
	 * 生成形参定义字符串 和 形参调用字符串
	 * @param ctParams
	 * @param declareStr
	 * @param invokeStr
	 */
	private static void generateParamDeclareInvoke(CtClass[] ctParams, StringBuilder declareStr, StringBuilder invokeStr){
		if(ctParams != null && ctParams.length > 0){
			for(int i=0,length=ctParams.length; i< length; i++){
				if(i != 0){
					declareStr.append(",");
					invokeStr.append(",");
				}
				declareStr.append(ctParams[i].getName()).append(" arg").append(i);
				invokeStr.append("arg").append(i);
			}
		}
	}

	
	public static Map<String, List<MethodParam>> getMethodList(String pathName, String paramStart){
        try {
        	Map<String, List<MethodParam>> map = new HashMap<>();
        	
        	Properties prop = new Properties();
            prop.load(ProxyClass.class.getClassLoader().getResourceAsStream(pathName));
            Set<Object> propSet = prop.keySet();
            for(Object keyObj:propSet){
            	String key = (String) keyObj;
            	if(key.startsWith(paramStart)){
            		String propVal = prop.getProperty(key);
            		if(propVal.endsWith(":")){
            			propVal = propVal + "-";
            		}
            		String[] values = propVal.split(":");
            		if(values.length < 3){
            			throw new RuntimeException("配置参数长度缺失:"+key);
            		}
            		//类名指定
            		List<MethodParam> methods = map.get(values[0]);
            		if(methods == null){
            			methods = new ArrayList<>();
            			map.put(values[0], methods);
            		}
            		//方法参数指定
            		@SuppressWarnings("rawtypes")
					List<Class> clses = new ArrayList<>();
            		String[] params = values[2].replace("-", "").split(",");
            		for(String param:params){
            			if("".equals(param)){
            				continue;
            			}else if(PrimitiveEnum.checkPrimitive(param)){
            				clses.add(PrimitiveEnum.getPrimitiveClass(param));
            			}else{
            				clses.add(Class.forName(param));
            			}
            		}
            		//类关联方法
            		methods.add(new MethodParam(values[1], clses.toArray(new Class[]{})));
            	}
            }
            return map;
		} catch (Exception e) {
            throw new RuntimeException("加载配置文件异常!", e);
		}
	}
	
	
	
	/**
	 * 仅编译需要，通过tomcat启动才需要指定，main（springboot）、@Test启动不需要（加了也没关系）
	 * @param relateClassPath
	 */
	public static void addRelateClassPath(@SuppressWarnings("rawtypes") Class cls) {
		ProxyClass.relateClassPaths.add(cls.getProtectionDomain().getCodeSource().getLocation().getPath());
	}
	public static void addRelateClassPath(String relateClassPath) {
		ProxyClass.relateClassPaths.add(relateClassPath);
	}
	public static Set<String> getRelateClassPaths() {
		return relateClassPaths;
	}
	public static void setRelateClassPaths(Set<String> relateClassPaths) {
		ProxyClass.relateClassPaths = relateClassPaths;
	}
	
}
