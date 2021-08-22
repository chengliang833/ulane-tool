package wang.ulane.limitalgorithm.slide;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SlideLimiter {
	
	public static void main(String[] args) throws InterruptedException {
//		Slider sl = new Slider();
		NumSlider sl = new NumSlider();
		//每秒6
		sl.setMaxSize(6);
		sl.setTimeLimit(100);
		
		ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
		ExecutorService es = Executors.newFixedThreadPool(10);
		for(int i=0;i<3;i++){
			// 直接拿字符串的长度呗
			CountDownLatch cdl = new CountDownLatch(100);
			for (int j = 0; j < 100; j++) {
				es.execute(() -> {
//					int size = "sefedsfsdfsg".length();
//					boolean sign = sl.pass(new SlideObj((int)(Math.random()*30)+1, System.currentTimeMillis()));
					boolean sign = sl.pass(System.currentTimeMillis());
					clq.add(sign+"");
					cdl.countDown();
				});
			}
			cdl.await();
			clq.add("--------------");
			Thread.sleep(1000);
			System.out.println("--------------");
		}
		
		String a;
		while((a = clq.poll()) != null){
			System.out.println(a);
		}
		
		es.shutdown();
		
	}
	
}
