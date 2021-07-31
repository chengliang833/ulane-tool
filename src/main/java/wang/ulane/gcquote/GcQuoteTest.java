package wang.ulane.gcquote;

import java.util.ArrayList;
import java.util.List;

public class GcQuoteTest {
	
	public static void main(String[] args) throws InterruptedException {
		List<SecondObj> ses = new ArrayList<SecondObj>();
		for(int i=0; i<10000; i++){
			ses.add(new FirstObj("aaa"+i).getSecond());
		}
		int i = 0;
		while(true){
			System.gc();
			for(SecondObj s:ses){
				s.invokeCallback();
			}
			Thread.sleep(100);
			System.out.println("next..."+ ++i);
		}
	}
	
}
