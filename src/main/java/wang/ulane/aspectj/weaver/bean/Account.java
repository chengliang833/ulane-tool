package wang.ulane.aspectj.weaver.bean;

public class Account {
	
	static int balance = 20;
	
	public static boolean pay(int amount) {
		if (balance < amount) {
			return false;
		}
		balance -= amount;
		return true;
	}
	
}