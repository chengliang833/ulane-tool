package wang.ulane.api.function;

public class GoFunc {
	
	public void go(String str, CusFunction cf){
		System.out.println(str);
		FunctionTest.a = 3;
		cf.invoke();
		System.out.println(str+"2");
	}
	
}
