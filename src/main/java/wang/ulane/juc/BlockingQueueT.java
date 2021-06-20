package wang.ulane.juc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueT {
	
	public static void main(String[] args) throws InterruptedException {
//		ArrayBlockingQueue<Integer> bq = new ArrayBlockingQueue<>(5);
		LinkedBlockingQueue<Integer> bq = new LinkedBlockingQueue<>(1000);
		ConcurrentLinkedQueue<Integer> clq = new ConcurrentLinkedQueue<>();
		
//		ArrayList<Integer> syncList = new ArrayList<>();
//		List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
		Vector<Integer> syncList = new Vector<>();
//		CopyOnWriteArrayList<Integer> syncList = new CopyOnWriteArrayList<>();
		
		CountDownLatch cdlGo = new CountDownLatch(1);
		CountDownLatch cdl = new CountDownLatch(10);
		
		for(int i=1; i<11; i++){
			final int tempi = i;
			Thread t = new Thread(()->{
				try {
					cdlGo.await();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				for(int j=11; j<1000011; j++){
					try {
						bq.put(tempi * j);
						clq.offer(tempi * j);
						syncList.add(tempi * j);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				cdl.countDown();
			});
			t.start();
		}
		
		for(int i=1; i<11; i++){
			final int tempi = i;
			Thread t = new Thread(()->{
				while(true){
					System.out.println(syncList.size());
//					System.out.println(clq.size());
					try {
//						Thread.sleep(1000);
						System.out.println(bq.take());
						System.out.println(clq.poll());
						syncList.get(0);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
		
		cdlGo.countDown();
//		cdl.await();
		
		Thread.sleep(1);

		List<Integer> temp = new ArrayList<>();
		long start = System.currentTimeMillis();
		synchronized (syncList) 
		{
			Iterator<Integer> it = syncList.iterator();
			while(it.hasNext()){
				temp.add(it.next());
			}
		}
		System.out.println(System.currentTimeMillis()-start);
		
//		System.out.println(temp);
		
		cdl.await();
		
		System.out.println(syncList.size());
		
		Thread.sleep(100000);
		
	}
	
}
