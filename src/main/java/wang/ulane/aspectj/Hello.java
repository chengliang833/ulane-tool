package wang.ulane.aspectj;

public class Hello {
	//aspectj和lombok冲突，不能一起用
	public static void main(String[] args) {
		sayHello();
		
		Hello.sayHello();
		
		Hello h = new Hello();
		h.sayHello();
	}
	
	public static void sayHello(){
		System.out.println("测试。。。");
	}
	
}
