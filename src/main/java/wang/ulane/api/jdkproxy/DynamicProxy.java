package wang.ulane.api.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class DynamicProxy implements InvocationHandler {
	
	private Object subject;

	public DynamicProxy(Object subject) {
		super();
		this.subject = subject;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("before...");
		System.out.println(method);
		System.out.println(Arrays.asList(args));
		
		Object result;
		//proxy是Proxy.newProxyInstance得到的对象，可以执行类似递归的代理
		//实际方法只执行一遍，代理嵌套了两层
		if((int)args[0] == 1){
			args[0] = (int)args[0] + 1;
			result = method.invoke(proxy, args);
		}else{
			result = method.invoke(subject, args);
		}

		System.out.println("after...");
		return result;
	}

}
