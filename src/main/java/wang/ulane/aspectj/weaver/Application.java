package wang.ulane.aspectj.weaver;

import wang.ulane.aspectj.weaver.bean.Account;

public class Application {
	public static void main(String[] args) {
		//run with jvm option
		//-javaagent:C:\Users\eshonulane\.m2\repository\org\aspectj\aspectjweaver\1.9.6\aspectjweaver-1.9.6.jar
		testLoadTime();
	}
	public static void testLoadTime() {
		Account account = new Account();
		System.out.println("==================");
		account.pay(10);
		account.pay(50);
		System.out.println("==================");
	}
}
