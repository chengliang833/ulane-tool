package wang.ulane.gcquote;

public class FirstObj {
	
	private byte[] bytes = new byte[100*1024];
	private String str;
	
	public FirstObj(String str) {
		super();
		this.str = str;
	}
	
	//这里有一个内部类，内部类引用了FirstObj实例
	//匿名的是<wang/ulane/gcquote/FirstObj$1>
	//lambda是<java/lang/invoke/MethodHandles$Lookup
	public SecondObj getSecond(){
//		return new SecondObj(null);
//		return new SecondObj(new Callback() {
//			public void invoke() {
//				str.toString();
//			}
//		});
		return new SecondObj(()->{
			str.toString();
//			System.out.println(str);
//			int a = bytes.length+1;
//			a++;
//			System.out.println(a);
		});
	}
	
	
}
