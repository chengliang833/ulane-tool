package wang.ulane.proxy;

public class MethodParam{
	private String methodName;
	@SuppressWarnings("rawtypes")
	private Class[] params;
	private String beforeBody;
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

	public MethodParam(String methodName, @SuppressWarnings("rawtypes") Class[] params, String beforeBody, String afterBody) {
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