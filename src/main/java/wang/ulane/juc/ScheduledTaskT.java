package wang.ulane.juc;

import java.lang.reflect.Field;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledTaskT {
	
	public static void main(String[] args) throws InterruptedException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(5);
		ses.scheduleAtFixedRate(()->{
			System.out.println("123");
		}, 1000, 2000, TimeUnit.MILLISECONDS);
		
		ses.scheduleWithFixedDelay(()->{
			System.out.println("1234");
		}, 1000, 2000, TimeUnit.MILLISECONDS);
		
		ses.schedule(()->{
			System.out.println("one time1...");
		}, 1000, TimeUnit.MILLISECONDS);
		long n1 = System.nanoTime();
		ScheduledFuture sf = ses.schedule(()->{
			System.out.println("one time4...");
			return null;
		}, 5000, TimeUnit.MILLISECONDS);
		
		System.out.println(System.currentTimeMillis());
		System.out.println(System.nanoTime());
		Field f = sf.getClass().getDeclaredField("time");
		f.setAccessible(true);
		System.out.println(f.get(sf));
		System.out.println((long)f.get(sf) - n1);
		
		Thread.sleep(1000000);
	}
	
}
