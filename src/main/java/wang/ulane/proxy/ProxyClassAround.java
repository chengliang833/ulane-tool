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
import javassist.bytecode.AccessFlag;

public class ProxyClassAround {
	
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
		if(mp.getProxyType() == MethodParamTypeEnum.AROUND_TWO_BODY_STR){
			funcStr = generateParamAndBodyString(mStr, m, mp.getMethodName(), ctParams, mp.getBeforeContent(), mp.getAfterContent());
		}else if(mp.getProxyType() == MethodParamTypeEnum.AROUND_FULL_NAME){
			funcStr = generateParamAndBodyMethod(mStr, m, mp.getMethodName(), ctParams, mp.getCustomAround());
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
		mStr.append("} catch (RuntimeException e) {throw e;} catch (Exception e) {throw new RuntimeException(e);}}");
		
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
	
}
