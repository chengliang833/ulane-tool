package wang.ulane.juc;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

import wang.ulane.juc.EntityT.SimulationItr;

public class IteratorT {
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("start...");
		CountDownLatch cdl = new CountDownLatch(10);
		
		EntityT ta = new EntityT();
		
		for(int i=1; i<11; i++){
			final int tempi = i;
			Thread t = new Thread(()->{
				for(int j=11; j<1011; j++){
//					synchronized (ta) {
//						lock.lock();
						ta.addA();
						EntityT.invokeSleep(1000);
//						ta.setStr(ta.getStr()+tempi*j);
//						ta.setB(ta.getB()+1);
//						clq.add(ta.getB());
//						lock.unlock();
//					}
				}
				cdl.countDown();
			});
			t.start();
		}
		Thread t = new Thread(()->{
			System.out.println("check mem?");
			A:while(true){
//				synchronized (ta) {
					SimulationItr si = ta.simuItr();
					for(int i=0; i<100; i++){
						if(si.checkItaNo()){
							System.out.println(si.getIta());
							System.out.println(si.getA());
							System.out.println("break..."+i);
							break A;
						}
					}
//				}
			}
		});
		t.start();
		
//		for(int i=1; i<11; i++){
//			Thread t = new Thread(()->{
//				for(int j=11; j<14; j++){
//					lock.lock();
////					synchronized (ta) {
//						clq.add(10000000);
//						clq.add(ta.getB());
//						try {
//							Thread.sleep(10);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
////					}
//					lock.unlock();
//				}
//				cdl.countDown();
//			});
//			t.start();
//		}

		cdl.await();
		System.out.println("first end...");
		
//		System.out.println(clq.toString());
		
		Thread.sleep(100000);
		System.out.println("finish...");
	}
	
}
