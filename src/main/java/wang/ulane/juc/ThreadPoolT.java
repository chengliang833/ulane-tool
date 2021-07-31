package wang.ulane.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolT {
	
	public static void main(String[] args) {
//		ExecutorService es = Executors.newFixedThreadPool(5);
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 10, 5000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5));
		//for
		tpe.execute(()->{
			//execute
		});
		tpe.submit(()->{
			//submit
			return null;
		});
		tpe.shutdown();
		tpe.shutdownNow();
		
	}
	
}
