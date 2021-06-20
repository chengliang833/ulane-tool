package wang.ulane.juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class JucT {
	
	public static int id = 0;
	public static AtomicInteger index = new AtomicInteger(0);
	public static void main(String[] args) throws InterruptedException {
		int taskCount = 10;
		//i++线程
		ExecutorService es = Executors.newFixedThreadPool(5);
		CountDownLatch cdl = new CountDownLatch(taskCount);
//		CyclicBarrier cb = new CyclicBarrier(threadCount); //这个只阻塞子线程，不阻塞主线程
		final EntityT a = new EntityT();
		for(int i=0; i<taskCount; i++){
			es.execute(new Runnable() {
				public void run() {
					for(int j=0; j<10000; j++){
//						id = id + 1;
						id++;
						index.getAndIncrement();
//						index.getAndAdd(2);
//						a.getAi().getAndIncrement();
//						a.addA();
//						a.addAReen();
					}
					cdl.countDown();
//					try {
//						cb.await();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
				}
			});
		}
		cdl.await();
		es.shutdown();
		
		System.out.println(id);
//		System.out.println(a.getAi());
//		System.out.println(a.getA());
		System.out.println(index);
		System.out.println("finish...");
	}
}
