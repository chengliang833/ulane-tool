package wang.ulane.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class InterruptT {

	public static int outi = 0;
	public static void main(String[] args) {
		List<Thread> ts = new ArrayList<Thread>();
		
		ReentrantLock lock = new ReentrantLock();
		for(int i=0; i<10; i++){
			final int tempi = i;
			Thread t = new Thread(()->{
				try {
					while (true) {
						System.out.println("beforei:"+tempi);
//						lock.lock();//不受unpark和interrupt影响
						System.out.println("ini:"+tempi);
						outi = tempi;
						
						LockSupport.park();
//						Thread.sleep(100000);
						
						System.out.println("in2i:"+tempi);
						
						if(Thread.interrupted()){
							System.out.println("interrupted..."+tempi);
//							break;
						}
						
//						lock.unlock();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			t.start();
			ts.add(t);
		}
		
//		LockSupport.unpark(ts.get(outi));
		/*
		 * 必须在子线程中搭配Thread.interrupted()使用，Thread.interrupted()会清除interrupt状态，以便重新park
		 * 否则park判断到interrupt失效不会阻塞
		 */
		ts.get(outi).interrupt();
		
	}
	
}
