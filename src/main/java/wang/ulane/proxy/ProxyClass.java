package wang.ulane.proxy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

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
	
	public static void proxyMethod(CtClass ct, ClassPool pool, String methodName, @SuppressWarnings("rawtypes") Class[] params, String beforeBody, String afterBody) throws Exception{
		CtMethod m = null;
		CtClass[] ctParams = null;
		if(params != null && params.length > 0){
			ctParams = new CtClass[params.length];
			for(int i=0,length=params.length; i<length; i++){
				ctParams[i] = pool.getCtClass(params[i].getCanonicalName());
			}
			// 获取被修改的方法
			m = ct.getDeclaredMethod(methodName, ctParams);
		}else{
			m = ct.getDeclaredMethod(methodName);
		}
		
		createProxyMethod(m, methodName, ctParams, ct);
		changeOriginalMethod(m, methodName, ctParams, ct, beforeBody, afterBody);
	}
	
	/**
	 * 添加代理方法名[methodName]Proxy，其他方法体，本地变量表（形参名）等不变
	 * @param m
	 * @param methodName
	 * @param ctParams
	 * @param ct
	 * @throws Exception
	 */
	public static void createProxyMethod(CtMethod m, String methodName, CtClass[] ctParams, CtClass ct) throws Exception{
		CtMethod m2 = new CtMethod(m.getReturnType(), methodName+"Proxy", ctParams, ct);
		
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
	public static List<String> init0 = Arrays.asList("byte","short","int","long","float","double","char");
	public static String initFalse = "boolean";
	public static void changeOriginalMethod(CtMethod m, String methodName, CtClass[] ctParams, CtClass ct, String beforeBody, String afterBody) throws Exception{
		if(beforeBody == null){
			beforeBody = "";
		}
		if(afterBody == null){
			afterBody = "";
		}
		StringBuilder bodyInvoke = new StringBuilder();
		
		StringBuilder mStr = new StringBuilder("public ");
		mStr.append(m.getReturnType().getName()).append(" ");
		mStr.append(methodName).append("(");
		generateParamDeclareInvoke(ctParams, mStr, bodyInvoke);
		mStr.append("){");
		
		String returnTypeName = m.getReturnType().getName();
		if(returnTypeName.equals("void")){
			mStr.append("String result = null;");
			mStr.append(beforeBody);
			mStr.append(methodName).append("Proxy(").append(bodyInvoke).append(");");
			mStr.append(afterBody);
		}else{
//			if(m.getReturnType() instanceof CtPrimitiveType){//还是要区分boolean，干脆直接判断
			if(init0.contains(returnTypeName)){
				mStr.append(m.getReturnType().getName()).append(" result = 0;");
			}else if(initFalse.equals(returnTypeName)){
				mStr.append(m.getReturnType().getName()).append(" result = false;");
			}else{
				mStr.append(m.getReturnType().getName()).append(" result = null;");
			}
			mStr.append(beforeBody);
			mStr.append("result = ");
			mStr.append(methodName).append("Proxy(").append(bodyInvoke).append(");");
			mStr.append(afterBody);
			mStr.append("return result;");
		}
		mStr.append("}");
		
		//直接setBody不能对应形参名
//		m3.setBody(methodBody.toString());
		CtMethod m3 = CtMethod.make(mStr.toString(), ct);
//		m3.getMethodInfo().setAccessFlags(m3.getMethodInfo().getAccessFlags() | AccessFlag.STATIC);
		m3.getMethodInfo().setAccessFlags(m.getMethodInfo().getAccessFlags());
		
		ct.removeMethod(m);
		ct.addMethod(m3);
	}
	
	/**
	 * 生成形参定义字符串 和 形参调用字符串
	 * @param ctParams
	 * @param declareStr
	 * @param invokeStr
	 */
	public static void generateParamDeclareInvoke(CtClass[] ctParams, StringBuilder declareStr, StringBuilder invokeStr){
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

	
	
	
	/**
	 * 仅编译需要，通过tomcat启动才需要指定，main（springboot）、@Test启动不需要（加了也没关系）
	 * @param relateClassPath
	 */
	public static void addRelateClassPath(String  relateClassPath) {
		ProxyClass.relateClassPaths.add(relateClassPath);
	}
	public static Set<String> getRelateClassPaths() {
		return relateClassPaths;
	}
	public static void setRelateClassPaths(Set<String> relateClassPaths) {
		ProxyClass.relateClassPaths = relateClassPaths;
	}
	
}
