package wang.ulane.proxy;

public class BeanTest {
	
	public static void test(){
		System.out.println(2343);
		System.out.println(3243);
	}
	
	public void test(Integer i){
		System.out.println(i);
	}

	public void test(Integer i,Integer b){
		System.out.println(i);
		System.out.println(b);
	}

	public int test(Integer i,Integer b,Integer c){
		System.out.println(i);
		System.out.println(b);
		return c;
	}
	
	public char testChar(Integer i){
		System.out.println(i);
		return 9;
	}

	public boolean testBoolean(Integer i){
		System.out.println(i);
		return false;
	}
	
//	public void testProxy(Integer i,Integer b){
//		System.out.println(i);
//		System.out.println(b);
//	}
//	public void test(java.lang.Integer arg0, java.lang.Integer arg1) {
//		com.alibaba.fastjson.JSONArray arr = new com.alibaba.fastjson.JSONArray();
//		arr.add(arg0);
//		arr.add(arg1);
//		StringBuilder targetMethod = new StringBuilder("top.ulane.springtest.aop.test.BeanTest").append(":").append("test");
//		long start = System.currentTimeMillis();
//		StringBuilder startStr = new StringBuilder("开始").append(Thread.currentThread().getName()).append("调用--> ").append(targetMethod).append(" 参数:").append(arr.toJSONString());
//		System.out.println(startStr);
//		Integer result = null;
//		testProxy(arg0, arg1);
//		StringBuilder endStr = new StringBuilder("结束").append(Thread.currentThread().getName()).append("调用<-- ").append(targetMethod).append(" 返回值:").append(String.valueOf(result)).append(" 耗时:").append(System.currentTimeMillis() - start).append("ms");
//		System.out.println(endStr);
//		
////		System.out.println("开始" + Thread.currentThread().getName() + "调用--> " + targetMethod + " 参数:" + arr.toJSONString());
////		System.out.println("结束" + Thread.currentThread().getName() + "调用<-- " + targetMethod + " 返回值:" + String.valueOf(result) + " 耗时:" + (System.currentTimeMillis() - start) + "ms");
//	}
}
