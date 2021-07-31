package wang.ulane.api.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JDKProxyTest {
	
	//lambda
	public static void main(String[] args) {
		InterfaceImpl ii = new InterfaceImpl();
		//完全接口化，与实现类完全区分开
		InterfaceSupper gfs = (InterfaceSupper) Proxy.newProxyInstance(InterfaceSupper.class.getClassLoader(), new Class[]{InterfaceSupper.class}, (proxy, method, args1)->{
			System.out.println("before...");
			Object result = method.invoke(ii, args1);
			System.out.println("after...");
			return result;
		});
		
		gfs.test(1);
	}
	
	
	//基本测试
	public static void main1(String[] args) {
		InterfaceImpl ii = new InterfaceImpl();
		InvocationHandler ih = new DynamicProxy(ii);
		
		InterfaceSupper gfs = (InterfaceSupper) Proxy.newProxyInstance(ii.getClass().getClassLoader(), ii.getClass().getInterfaces(), ih);
		
		gfs.test(1);
		
	}
	
	
	
}
