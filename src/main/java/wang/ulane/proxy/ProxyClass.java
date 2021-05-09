package wang.ulane.proxy;

import java.io.InputStream;
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
	
	@Deprecated
	public static void proxyMethod(String classFullName, String methodName, @SuppressWarnings("rawtypes") Class[] params, String beforeBody, String afterBody){
		proxyMethod(classFullName, new MethodParam(methodName, beforeBody, afterBody, params));
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
				proxyMethod(ct, pool, mp);
			}
			
			// 转为class
			ct.toClass();
			// 释放对象
			ct.detach();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void proxyMethod(CtClass ct, ClassPool pool, MethodParam mp) throws Exception{
		CtClass[] ctParams = null;
		if(mp.getParams() != null && mp.getParams().length > 0){
			ctParams = new CtClass[mp.getParams().length];
		}
		CtMethod m = getSignMethod(pool, mp.getMethodName(), mp.getParams(), ctParams, ct);
		//无返回值直接前后加就行，放弃，兼容ProxyClassLog前后变量关联
//		if(m.getReturnType().getName().equals("void")){
//			m.insertBefore(beforeBody);
//			m.insertAfter("String result = null;"+afterBody);
//		}else{
			createProxyMethod(m, mp.getMethodName(), ctParams, ct);
			changeOriginalMethod(m, ctParams, ct, mp);
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
	protected static void createProxyMethod(CtMethod m, String methodName, CtClass[] ctParams, CtClass ct) throws Exception{
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
	protected static void changeOriginalMethod(CtMethod m, CtClass[] ctParams, CtClass ct, MethodParam mp) throws Exception{
		StringBuilder mStr = new StringBuilder("public ");
		//按位与：m.getMethodInfo().getAccessFlags() & 0b1000
		if((m.getMethodInfo().getAccessFlags() & AccessFlag.STATIC) == 8){
			mStr.append("static ");
		}
		
		mStr.append(m.getReturnType().getName()).append(" ");
		mStr.append(mp.getMethodName()).append("(");
		
		String funcStr = null;
		if(mp.getCustomFullMethodName() == null){
			funcStr = generateParamAndBodyString(mStr, m, mp.getMethodName(), ctParams, mp.getBeforeBody(), mp.getAfterBody());
		}else{
			funcStr = generateParamAndBodyMethod(mStr, m, mp.getMethodName(), ctParams, mp.getCustomFullMethodName());
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
	
	@Deprecated
	protected static String generateParamAndBodyString(StringBuilder mStr, CtMethod m, String methodName, CtClass[] ctParams, String beforeBody, String afterBody) throws NotFoundException{
		StringBuilder bodyInvoke = new StringBuilder();
		generateParamDeclareInvoke(ctParams, mStr, bodyInvoke, false);
		mStr.append("){");
		
		if(beforeBody == null){
			beforeBody = "";
		}
		if(afterBody == null){
			afterBody = "";
		}
		
		String returnTypeName = m.getReturnType().getName();
		mStr.append(beforeBody);
		if(returnTypeName.equals("void")){
			mStr.append("String result = null;");
		}else{
//			Integer.class.getField("TYPE").get(null)//int.class
//			if(m.getReturnType() instanceof CtPrimitiveType){//还是要区分boolean，干脆直接判断
			mStr.append(returnTypeName).append(" result = ");
		}
		mStr.append(methodName).append("Proxy___(").append(bodyInvoke).append(");");
		mStr.append(afterBody);
		if(!returnTypeName.equals("void")){
			mStr.append("return result;");
		}
		mStr.append("}");
		
		String funcStr = mStr.toString();
		if(PrimitiveEnum.checkPrimitive(returnTypeName)){
			funcStr = funcStr.replaceAll("\\$\\{returnwrapper-(.*?)\\}", PrimitiveEnum.valueOf("_"+returnTypeName).getWrapper()+".valueOf($1)");
		}else{
			funcStr = funcStr.replaceAll("\\$\\{returnwrapper-(.*?)\\}", "$1");
		}
		return funcStr;
	}

	protected static String generateParamAndBodyMethod(StringBuilder mStr, CtMethod m, String methodName, CtClass[] ctParams, String customFullMethodName) throws NotFoundException{
		StringBuilder bodyInvoke = new StringBuilder();
		generateParamDeclareInvoke(ctParams, mStr, bodyInvoke, true);
		mStr.append("){try {");
		
		String returnTypeName = m.getReturnType().getName();
		if(!returnTypeName.equals("void")){
			if(PrimitiveEnum.checkPrimitive(returnTypeName)){
				mStr.append(returnTypeName).append(" result = ((").append(PrimitiveEnum.getEnum(returnTypeName).getWrapper()).append(")");
			}else{
				mStr.append(returnTypeName).append(" result = (").append(returnTypeName).append(")");
			}
		}
		mStr.append(customFullMethodName).append("(new ").append(ProxyPoint.class.getCanonicalName()).append("(");
		mStr.append(m.getDeclaringClass().getName()).append(".class");

		if((m.getMethodInfo().getAccessFlags() & AccessFlag.STATIC) == 8){
			mStr.append(", null");
		}else{
			mStr.append(", this");
		}
		mStr.append(", \"").append(methodName).append("\"");
		compileBodyInvoke(mStr, ctParams, bodyInvoke);
		mStr.append("))");
		if(returnTypeName.equals("void")){
			mStr.append(";");
		}else{
			if(PrimitiveEnum.checkPrimitive(returnTypeName)){
				mStr.append(").").append(PrimitiveEnum.getEnum(returnTypeName).getToPrimitiveMethodName()).append("()");
			}
			mStr.append(";");
			mStr.append("return result;");
		}
		mStr.append("} catch (Exception e) {throw new RuntimeException(e);}}");
		
		return mStr.toString();
	}
	
	/**
	 * 完善调用参数
	 * @param mStr
	 * @param ctParams
	 * @param bodyInvoke
	 */
	protected static void compileBodyInvoke(StringBuilder mStr, CtClass[] ctParams, StringBuilder bodyInvoke){
		if(bodyInvoke.length() > 0){
			mStr.append(", new Class[]{");
			int i = 0;
			for(CtClass cls:ctParams){
				if(i != 0){
					mStr.append(", ");
				}
				mStr.append(cls.getName()).append(".class");
				i++;
			}
			mStr.append("}");
			
			mStr.append(", new Object[]{").append(bodyInvoke).append("}");
		}else{
			mStr.append(", null, null");
		}
	}
	
	/**
	 * 生成形参定义字符串 和 形参调用字符串
	 * @param ctParams
	 * @param declareStr
	 * @param invokeStr
	 */
	protected static void generateParamDeclareInvoke(CtClass[] ctParams, StringBuilder declareStr, StringBuilder invokeStr, boolean useWrapper){
		if(ctParams != null && ctParams.length > 0){
			for(int i=0,length=ctParams.length; i< length; i++){
				if(i != 0){
					declareStr.append(",");
					invokeStr.append(",");
				}
				declareStr.append(ctParams[i].getName()).append(" arg").append(i);
				if(useWrapper && PrimitiveEnum.checkPrimitive(ctParams[i].getName())){
					invokeStr.append(PrimitiveEnum.getEnum(ctParams[i].getName()).getWrapper()).append(".valueOf(");
					invokeStr.append("arg").append(i).append(")");
				}else{
					invokeStr.append("arg").append(i);
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	public static void initClass(String pathName, String paramStart){
        try {
        	Properties prop = new Properties();
        	InputStream is = ProxyClass.class.getClassLoader().getResourceAsStream(pathName);
        	if(is == null){
        		return;
        	}
            prop.load(is);
            Set<Object> propSet = prop.keySet();
            for(Object keyObj:propSet){
            	String key = (String) keyObj;
            	if(key.startsWith(paramStart)){
            		String propVal = prop.getProperty(key);
            		String[] values = propVal.split(",");
            		for(String val:values){
            			if(!"".equals(val)){
            				addRelateClassPath(Class.forName(val));
            			}
            		}
            	}
            }
		} catch (Exception e) {
            throw new RuntimeException("加载配置文件异常!", e);
		}
	}
	
	public static Map<String, List<MethodParam>> getMethodList(String pathName, String paramStart){
        try {
        	Map<String, List<MethodParam>> map = new HashMap<>();
        	
        	Set<String> existsCheck = new HashSet<>();
        	Properties prop = new Properties();
        	InputStream is = ProxyClass.class.getClassLoader().getResourceAsStream(pathName);
        	if(is == null){
        		return map;
        	}
            prop.load(is);
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

            		String[] params = null;
            		String customeProxyMethod = null;
            		if(values.length == 3){
            			params = values[2].replace("-", "").split(",");
            		}else{
            			customeProxyMethod = values[2];
            			String customeProxyClass = customeProxyMethod.substring(0, customeProxyMethod.lastIndexOf("."));
            			addRelateClassPath(Class.forName(customeProxyClass));
            			params = values[3].replace("-", "").split(",");
            		}
            		
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
            		String existStr = values[0] + ":" + values[1] + clses.toString();
            		if(existsCheck.contains(existStr)){
            			throw new RuntimeException("同一个方法不能设置两次:" + existStr);
            		}else{
            			existsCheck.add(existStr);
            		}
            		methods.add(new MethodParam(values[1], customeProxyMethod, clses.toArray(new Class[]{})));
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
