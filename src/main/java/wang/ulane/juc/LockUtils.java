package wang.ulane.juc;

import java.util.LinkedList;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class LockUtils {
	
	//TODO 可以试试ReentrantLock公平锁的各种锁状态位
	//保证lockNum
	private static ReentrantLock rl = new ReentrantLock(true);
	private static Integer lockNum = 8;
	//等待列表
	private static LinkedList<Thread> threads = new LinkedList<>();
	
	public static boolean trylock(Thread t){
		rl.lock();
		boolean result = false;
//		System.out.println("trylock:"+lockNum);
		if(lockNum - 1 >= 0){
			if(threads.size() == 0){
				lockNum--;
				result = true;
			}else if(t == threads.peek()){
				threads.poll();
				lockNum--;
				result = true;
				if(lockNum - 1 >= 0 && threads.size() > 0){
					LockSupport.unpark(threads.peek());
				}
			}else if(!threads.contains(t)){
				threads.offer(t);
			}
		}else{
			threads.offer(t);
		}
		rl.unlock();
		return result;
	}
	
	public static void unlock(){
		rl.lock();
		lockNum++;
		Thread t = threads.peek();
		if(t != null){
//			System.out.println("state1:"+t.getState());
			LockSupport.unpark(t);
//			System.out.println("state2:"+t.getState());
		}
//		System.out.println("unlock:"+lockNum);
		rl.unlock();
	}
}
