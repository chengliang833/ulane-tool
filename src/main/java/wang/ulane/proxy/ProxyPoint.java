package wang.ulane.proxy;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ProxyPoint {
	
	private Class cls;
	private Object bean;
	private String methodName;
	private Class[] paramClses;
	private Object[] args;
	
	public ProxyPoint() {
		super();
	}
	
	public ProxyPoint(Class cls, Object bean, String methodName, Class[] paramClses, Object[] args) {
		super();
		this.cls = cls;
		this.bean = bean;
		this.methodName = methodName;
		this.paramClses = paramClses;
		this.args = args;
	}

	@SuppressWarnings("unchecked")
	public Object proceed() throws Exception {
		Method m = cls.getDeclaredMethod(methodName+"Proxy___", paramClses);
		if(!m.isAccessible()){
			m.setAccessible(true);
		}
		return m.invoke(bean, args);
//		if(m.isAccessible()){
//			return m.invoke(bean, args);
//		}else{
//			m.setAccessible(true);
//			Object result = m.invoke(bean, args);
//			m.setAccessible(false);
//			return result;
//		}
	}

	public Class getCls() {
		return cls;
	}
	public void setCls(Class cls) {
		this.cls = cls;
	}
	public Object getBean() {
		return bean;
	}
	public void setBean(Object bean) {
		this.bean = bean;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class[] getParamClses() {
		return paramClses;
	}
	public void setParamClses(Class[] paramClses) {
		this.paramClses = paramClses;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	
}
