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
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import wang.ulane.util.StringUtils;

/**
 * 代理class
 * 可参考 bytebuddy包
 */
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
		CtBehavior c = getSignMethod(pool, mp.getMethodName(), mp.getParams(), ctParams, ct);
		//无返回值直接前后加就行，放弃，兼容ProxyClassLog前后变量关联
//		if(m.getReturnType().getName().equals("void")){
//			m.insertBefore(beforeBody);
//			m.insertAfter("String result = null;"+afterBody);
//		}else{
//		}
		if(MethodParamTypeEnum.isAround(mp.getProxyType())){
			if(c instanceof CtMethod){
				CtMethod m = (CtMethod) c;
				ProxyClassAround.createProxyMethod(m, mp.getMethodName(), ctParams, ct);
				ProxyClassAround.changeOriginalMethod(m, ctParams, ct, mp);
			}else{
				//暂时没有
			}
		}else{
			if(c instanceof CtConstructor){
				CtConstructor cm = (CtConstructor) c;
				ProxyClassBeforeAfter.createConstructorBefore(cm, mp, ctParams, ct);
				ProxyClassBeforeAfter.createConstructorAfter(cm, mp, ctParams, ct);
//				ct.writeFile();
			}else{
				//暂时没有
			}
		}
	}
	
	protected static CtBehavior getSignMethod(ClassPool pool, String methodName, @SuppressWarnings("rawtypes") Class[] params, CtClass[] ctParams, CtClass ct) throws NotFoundException{
		CtBehavior m = null;
		try {
			if(params != null && params.length > 0){
				for(int i=0,length=params.length; i<length; i++){
					ctParams[i] = pool.getCtClass(params[i].getCanonicalName());
				}
				// 获取被修改的方法
				m = ct.getDeclaredMethod(methodName, ctParams);
			}else{
				m = ct.getDeclaredMethod(methodName);
			}
		} catch (NotFoundException e) {
			m = ct.getDeclaredConstructor(ctParams);
		}
		return m;
	}
	
	/**
	 * 初始化二次编译需要的class，properties中指定，同一个jar包只需要指定任意一个class即可
	 * 1.需要代理的class如果在jar包中；但是需要用同一个jar包中的另一个class，否则会load两次
	 * 2.methodBody中需要的class，直接指定
	 * 3.代理当前项目中的class，也需要指定当前项目中其他任意一个class作为牵引
	 * @param pathName
	 * @param paramStart
	 */
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
        	//String:List一个类对应多个代理方法
        	Map<String, List<MethodParam>> map = new HashMap<>();
        	
        	Set<String> existsCheck = new HashSet<>();
        	Properties prop = new Properties();
        	InputStream is = ProxyClass.class.getClassLoader().getResourceAsStream(pathName);
        	if(is == null){
        		return map;
        	}
            prop.load(is);
            if("false".equals(prop.getProperty("logext.proxys.flag"))){
            	return map;
            }
            Set<Object> propSet = prop.keySet();
            for(Object keyObj:propSet){
            	String key = (String) keyObj;
            	if(key.startsWith(paramStart)){
            		String propVal = prop.getProperty(key);
            		if(propVal.endsWith(":")){
            			propVal = propVal + "-";
            		}
            		String[] values = propVal.split(":");
            		if(values.length < 4){
            			throw new RuntimeException("配置参数长度缺失:"+key);
            		}
            		//类名指定
            		List<MethodParam> methods = map.get(values[0]);
            		if(methods == null){
            			methods = new ArrayList<>();
            			map.put(values[0], methods);
            		}

            		String[] params = null;
            		List<Class> clses = null;
            		switch(values.length){
            			case 6:
                			//类名:方法名:代理类型:自定义代理执行方法before:自定义代理执行方法after:形参类型
            				MethodParamTypeEnum methodParamTypeEnum = MethodParamTypeEnum.valueOf(values[2]);
            				String beforeName = values[3];
            				if(StringUtils.isNull(beforeName)){
            					beforeName = null;
            				}else{
            					addClassRelate(beforeName);
            				}
            				String afterName = values[4];
            				if(StringUtils.isNull(afterName)){
            					afterName = null;
            				}else{
            					addClassRelate(afterName);
            				}
                			params = values[5].replace("-", "").split(",");
                    		//方法参数指定
                			clses = generateClses(params);
        					checkMethosExist(existsCheck, values, clses);

                    		methods.add(new MethodParam(values[1], methodParamTypeEnum, beforeName, afterName, clses.toArray(new Class[]{})));
            				break;
            			case 4:
                			//类名:方法名:自定义代理执行方法:形参类型
                    		String customeProxyMethod = values[2];
                    		addClassRelate(customeProxyMethod);
                			params = values[3].replace("-", "").split(",");
                    		//方法参数指定
        					clses = generateClses(params);
        					checkMethosExist(existsCheck, values, clses);
        					
                    		methods.add(new MethodParam(values[1], customeProxyMethod, clses.toArray(new Class[]{})));
            				break;
//            			case 3:
//                			//类名:方法名:形参类型
//                			params = values[2].replace("-", "").split(",");
//            				break;
//            			case 2:
//            				//类名:方法名(无参数)
//            				break;
            			default:
            				throw new RuntimeException("配置错误："+key);
            		}
            		
            	}
            }
            return map;
		} catch (Exception e) {
            throw new RuntimeException("加载配置文件异常!", e);
		}
	}
	private static List<Class> generateClses(String[] params) throws ClassNotFoundException{
		List<Class> clses = new ArrayList<>();
		for(String param:params){
			if("".equals(param)){
				continue;
			}else if(PrimitiveEnum.checkPrimitive(param)){
				clses.add(PrimitiveEnum.getPrimitiveClass(param));
			}else{
				clses.add(Class.forName(param));
			}
		}
		return clses;
	}
	private static void checkMethosExist(Set<String> existsCheck, String[] values, List<Class> clses){
		String existStr = values[0] + ":" + values[1] + clses.toString();
		if(existsCheck.contains(existStr)){
			throw new RuntimeException("同一个方法不能设置两次:" + existStr);
		}else{
			existsCheck.add(existStr);
		}
	}
	private static void addClassRelate(String methodFullName) throws ClassNotFoundException{
		String customeProxyClass = methodFullName.substring(0, methodFullName.lastIndexOf("."));
		addRelateClassPath(Class.forName(customeProxyClass));
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
