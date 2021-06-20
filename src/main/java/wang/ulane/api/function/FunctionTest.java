package wang.ulane.api.function;

public class FunctionTest {
	
	public static Integer a = 1;
	
	public static void main(String[] args) {
		new GoFunc().go("sad", ()->{
			System.out.println(a);
		});
	}
	
}
