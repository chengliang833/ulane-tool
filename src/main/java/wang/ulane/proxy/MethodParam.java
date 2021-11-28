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
	 * 代理方式
	 */
	private MethodParamTypeEnum proxyType;
	
	/**
	 * 指定执行ProxyPoint.proceed的方法全类名+方法名 wang.ulane.proxy.TestMain.testProxy
	 * 与下面一组（两个）自定义body二选一 
	 * proxyType == MethodParamTypeEnum.AROUND_FULL_NAME
	 */
	private String customAround;
	/**
	 * 代理方法之前执行部分
	 * proxyType == MethodParamTypeEnum.BEFOR_AFTER_BODY_STR
	 */
	private String beforeContent;
	/**
	 * 代理方法之后执行部分
	 * proxyType == MethodParamTypeEnum.BEFOR_AFTER_BODY_STR
	 */
	private String afterContent;
	
	public MethodParam() {
		super();
	}
	
//	public MethodParam(String methodName) {
//		super();
//		this.methodName = methodName;
//	}
//	
//	public MethodParam(String methodName, @SuppressWarnings("rawtypes") Class... params) {
//		super();
//		this.methodName = methodName;
//		this.params = params;
//	}

	public MethodParam(String methodName, String customFullMethodName, @SuppressWarnings("rawtypes") Class... params) {
		super();
		this.methodName = methodName;
		this.customAround = customFullMethodName;
		this.params = params;
		this.proxyType = MethodParamTypeEnum.AROUND_FULL_NAME;
	}
	
	@Deprecated
	public MethodParam(String methodName, String beforeBody, String afterBody, @SuppressWarnings("rawtypes") Class[] params) {
		super();
		this.methodName = methodName;
		this.params = params;
		this.beforeContent = beforeBody;
		this.afterContent = afterBody;
		this.proxyType = MethodParamTypeEnum.BEFOR_AFTER_BODY_STR;
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
	public MethodParamTypeEnum getProxyType() {
		return proxyType;
	}
	public void setProxyType(MethodParamTypeEnum proxyType) {
		this.proxyType = proxyType;
	}
	public String getCustomAround() {
		return customAround;
	}
	public void setCustomAround(String customAround) {
		this.customAround = customAround;
	}
	public String getBeforeContent() {
		return beforeContent;
	}
	public void setBeforeContent(String beforeContent) {
		this.beforeContent = beforeContent;
	}
	public String getAfterContent() {
		return afterContent;
	}
	public void setAfterContent(String afterContent) {
		this.afterContent = afterContent;
	}
	
}