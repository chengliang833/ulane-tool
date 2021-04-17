package wang.ulane.proxy;

public class TestMain {
	
	static {
//		ProxyClassLog.proxyMethodLog("wang.ulane.proxy.BeanTest", "test", new Class[]{Integer.class,Integer.class,Integer.class});
//		ProxyClassLog.proxyMethodLog("wang.ulane.proxy.BeanTest", "testChar", new Class[]{Integer.class});
//		ProxyClassLog.proxyMethodLog("wang.ulane.proxy.BeanTest", "testBoolean", new Class[]{Integer.class});
		ProxyClassLog.proxyMethodLog("wang.ulane.proxy.BeanTest",
				new MethodParam("test"),
				new MethodParam("test", Integer.class, Integer.class, Integer.class),
				new MethodParam("test", Integer.class, Integer.class),
				new MethodParam("testChar", Integer.class),
				new MethodParam("testBoolean", Integer.class)
				);
	
	}
	public static void main(String[] args) throws Exception {
		BeanTest.test();
		System.out.println("---");
		new BeanTest().test(5,6);
		System.out.println("---");
		System.out.println(new BeanTest().test(5, 6, 7));
		System.out.println("---");
		System.out.println(new BeanTest().testChar(3));
		System.out.println("---");
		System.out.println(new BeanTest().testBoolean(3));
	}
	
}
