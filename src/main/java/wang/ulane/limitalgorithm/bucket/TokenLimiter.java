package wang.ulane.limitalgorithm.bucket;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokenLimiter {
	
	public static void main(String[] args) throws InterruptedException {
		//生产
		Producer p = new Producer(1000);
		//每一秒放100
		ScheduledExecutorService esp = Executors.newSingleThreadScheduledExecutor();
		esp.scheduleAtFixedRate(()->{
			p.produce();
		}, 0, 1, TimeUnit.SECONDS);
		
		//获取
		TokenBucket tb = TokenBucket.getInstance();
		tb.setTokens(1000);
		tb.setMaxSize(2000);
		
		ConcurrentLinkedQueue<Boolean> clq = new ConcurrentLinkedQueue<>();
		ExecutorService es = Executors.newFixedThreadPool(10);
		for(int i=0;i<3;i++){
//			String data = "ewfesdfdfdssdgdsgdfgdffsf";
			// 直接拿字符串的长度呗
			CountDownLatch cdl = new CountDownLatch(100);
			for (int j = 0; j < 100; j++) {
				es.execute(() -> {
					boolean sign = tb.getToken((int)Math.random()*30);
					clq.add(sign);
					cdl.countDown();
				});
			}
			cdl.await();
			Thread.sleep(1000);
		}
		
		Boolean a;
		while((a = clq.poll()) != null){
			System.out.println(a);
		}
		
		esp.shutdown();
		es.shutdown();
	}
	
}

	