package wang.ulane.proxy;

public class MethodParam{
	/**
	 * 要代理的方法名
	 */
	private String methodName;
	@SuppressWarnings("rawtypes")
	/**
	 * 要代理的方法参数列表
	 */
	private Class[] params;
	/**
	 * 指定执行ProxyPoint.proceed的方法全类名+方法名 wang.ulane.proxy.TestMain.testProxy
	 * 与下面一组（两个）自定义body二选一 
	 */
	private String customFullMethodName;
	/**
	 * 代理方法之前执行部分
	 */
	private String beforeBody;
	/**
	 * 代理方法之后执行部分
	 */
	private String afterBody;
	
	public MethodParam() {
		super();
	}
	
	public MethodParam(String methodName) {
		super();
		this.methodName = methodName;
	}
	
	public MethodParam(String methodName, @SuppressWarnings("rawtypes") Class... params) {
		super();
		this.methodName = methodName;
		this.params = params;
	}

	public MethodParam(String methodName, String customFullMethodName, @SuppressWarnings("rawtypes") Class... params) {
		super();
		this.methodName = methodName;
		this.customFullMethodName = customFullMethodName;
		this.params = params;
	}
	
	@Deprecated
	public MethodParam(String methodName, String beforeBody, String afterBody, @SuppressWarnings("rawtypes") Class[] params) {
		super();
		this.methodName = methodName;
		this.params = params;
		this.beforeBody = beforeBody;
		this.afterBody = afterBody;
	}
	
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	@SuppressWarnings("rawtypes")
	public Class[] getParams() {
		return params;
	}
	public void setParams(@SuppressWarnings("rawtypes") Class[] params) {
		this.params = params;
	}
	public String getCustomFullMethodName() {
		return customFullMethodName;
	}
	public void setCustomFullMethodName(String customFullMethodName) {
		this.customFullMethodName = customFullMethodName;
	}
	public String getBeforeBody() {
		return beforeBody;
	}
	public void setBeforeBody(String beforeBody) {
		this.beforeBody = beforeBody;
	}
	public String getAfterBody() {
		return afterBody;
	}
	public void setAfterBody(String afterBody) {
		this.afterBody = afterBody;
	}
	
}