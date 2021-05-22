package wang.ulane.juc;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestAbc {
	private AtomicInteger ai = new AtomicInteger(0);
	private int a = 0;
	private ReentrantLock lock = new ReentrantLock();
	
	synchronized void addA(){
		a++;
	}
	
	void addAReen(){
		lock.lock();
		a = a + 1;
		lock.unlock();
	}
}
