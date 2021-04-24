package wang.ulane.proxy;

import java.util.List;
import java.util.Map;

public class TestMain {
	
	static {
//		ProxyClassLog.proxyMethodLog("wang.ulane.proxy.BeanTest", "test", new Class[]{Integer.class,Integer.class,Integer.class});
//		ProxyClassLog.proxyMethodLog("wang.ulane.proxy.BeanTest", "testChar", new Class[]{Integer.class});
//		ProxyClassLog.proxyMethodLog("wang.ulane.proxy.BeanTest", "testBoolean", new Class[]{Integer.class});
//		ProxyClassLog.proxyMethodLog("wang.ulane.proxy.BeanTest",
//				new MethodParam("test"),
//				new MethodParam("test", int.class),
//				new MethodParam("test", Integer.class, Integer.class, Integer.class),
//				new MethodParam("test", Integer.class, Integer.class),
//				new MethodParam("testChar", Integer.class),
//				new MethodParam("testBoolean", Integer.class)
//				);
		ProxyClass.initClass("app.properties", "log.proxys.initclass");
		Map<String, List<MethodParam>> map = ProxyClass.getMethodList("app.properties", "log.proxys.list");
		ProxyClassLog.proxyMethodLog(map);
	}
	
	public static void main(String[] args) throws Exception {
		BeanTest.test(2132L);
		System.out.println("---");
		new BeanTest().test(5);
		System.out.println("---");
		System.out.println(new BeanTest().test(5, 6, 7));
		System.out.println("---");
		System.out.println(new BeanTest().testChar(3));
		System.out.println("---");
		System.out.println(new BeanTest().testBoolean(3));
	}
	
}
