package wang.ulane.aspectj;

public aspect TxAspect {

	void around():call(static void Hello.sayHello()){
		System.out.println("before");
		proceed();
		System.out.println("after");
	}
	
}
