package wang.ulane.proxy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;

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
//		ProxyClass.initClass("app.properties", "logext.proxys.initclass");
//		Map<String, List<MethodParam>> map = ProxyClass.getMethodList("app.properties", "logext.proxys.list");
//		ProxyClassLog.proxyMethodLog(map);
		
//		ProxyClass.proxyMethod("wang.ulane.proxy.BeanTest", 
//				new MethodParam("test", "wang.ulane.proxy.TestMain.testProxy", Integer.class, Integer.class, Integer.class)
//				);
		ProxyClass.addRelateClassPath(HttpEntityEnclosingRequestBase.class);
		ProxyClass.proxyMethod("org.apache.http.client.methods.HttpPost", 
				new MethodParam("HttpPost", MethodParamTypeEnum.BEFOR_AFTER_FULL_NAME, null, "wang.ulane.proxy.TestMain.testAfter", String.class)
				);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("2:"+new HttpPost("http://www.baicu.com"));
//		BeanTest.test(2132L);
//		BeanTest.test(new Integer(3));
//		System.out.println("---");
//		new BeanTest().test(5);
//		System.out.println("---");
//		new BeanTest().test(5, 6);
//		System.out.println("---");
//		System.out.println(new BeanTest().test(5, 6, 7));
//		System.out.println("---");
//		System.out.println(new BeanTest().testChar(3));
//		System.out.println("---");
//		System.out.println(new BeanTest().testBoolean(3));
		
//		int taskCount = 10;
//		BeanTest bt = new BeanTest();
//		ExecutorService es = Executors.newFixedThreadPool(5);
//		CountDownLatch cdl = new CountDownLatch(taskCount);
////		CyclicBarrier cb = new CyclicBarrier(taskCount);
//		for(int i=0; i<taskCount; i++){
//			es.execute(new Runnable() {
//				public void run() {
////					try {
//							//全部到达需要taskCount = 线程池数
////						System.out.println("到达");
////						cb.await();
////					} catch (Exception e) {
////						e.printStackTrace();
////					}
//					for(int i=0; i<10000; i++){
////						bt.id++;
//						bt.test(34, 4);
//						//System.out.println内部有同步处理，需要注释
//					}
//					cdl.countDown();
//				}
//			});
//		}
//		cdl.await();
//		es.shutdown();
//		
//		System.out.println("---");
//		System.out.println(bt.id);
		System.out.println("finish...");
	}
	
	public static Object testProxy(ProxyPoint proxyPoint) throws Exception{
		//before
		System.out.println("before");
		//proceed
		Object result = proxyPoint.proceed();
//		Object result = 0;
		//after
		System.out.println("after:"+result);
		return result;
	}

	public static void testAfter(Object _this) throws Exception{
		System.out.println("after:"+_this);
	}
}
