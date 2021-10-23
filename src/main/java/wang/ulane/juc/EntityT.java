package wang.ulane.juc;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class EntityT {
	private AtomicInteger ai = new AtomicInteger(0);
	private int a = 0;
	private Integer b = 0;
	private String str = "";
	private ReentrantLock lock = new ReentrantLock();
	
	synchronized void addA(){
		a++;
	}
	
	void addAReen(){
		lock.lock();
		a = a + 1;
		lock.unlock();
	}
	
	
	
	public AtomicInteger getAi() {
		return ai;
	}

	public void setAi(AtomicInteger ai) {
		this.ai = ai;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public ReentrantLock getLock() {
		return lock;
	}

	public void setLock(ReentrantLock lock) {
		this.lock = lock;
	}

	public Integer getB() {
		return b;
	}

	public void setB(Integer b) {
		this.b = b;
	}
	
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	
	public synchronized SimulationItr simuItr(){
		return new SimulationItr();
	}
	
	class SimulationItr{
		int ita = a;
		
		public SimulationItr() {
			System.out.println("ita:"+ita);
			System.out.println("a:"+a);
		}
		
		public int getA(){
			return a;
		}
		
		public int getIta() {
			return ita;
		}

		public void setIta(int ita) {
			this.ita = ita;
		}

		public boolean checkItaNo(){
			return ita != a;
		}
	}
	
	public static void invokeSleep(){
		invokeSleep(5000);
	}
	public static void invokeSleep(int calculateGo){
		String j = "";
		for(int i=0; i<calculateGo; i++){
			j = j + i;
		}
	}
	
	
	

	public static Integer lockNum = 8;
	//都是锁class，不会同时更改lockNum
	public static synchronized boolean trylock(){
//		if(lockNum - 1 < 0){
//			return false;
//		}
		lockNum--;
		return true;
	}
	public static synchronized void unlock(){
		lockNum++;
	}
}
