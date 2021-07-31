package wang.ulane.gcquote;

public class SecondObj {
	
	private Callback cb;
	
	public SecondObj(Callback cb) {
		super();
		this.cb = cb;
	}
	
	public void invokeCallback(){
		cb.invoke();
	}
	
}
