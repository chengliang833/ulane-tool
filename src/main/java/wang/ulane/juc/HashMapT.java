package wang.ulane.juc;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class HashMapT {
	
	private static Object obj = new Object();
	
	public static void main(String[] args) throws InterruptedException {
//		HashMap<Object, Object> map = new HashMap<>();
		ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
		
		CountDownLatch cdl = new CountDownLatch(10);
		CyclicBarrier cb = new CyclicBarrier(10);
		
		for(int i=1; i<11; i++){
			final int tempi = i;
			Thread t = new Thread(()->{
				try {
					cb.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
				for(int j=11; j<111; j++){
					map.put(tempi + "*" + j, obj);
				}
				cdl.countDown();
			});
			t.start();
		}
		cdl.await();
		
		System.out.println(map.size());
		
	}
	
}
