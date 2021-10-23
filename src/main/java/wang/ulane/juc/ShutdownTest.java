package wang.ulane.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ShutdownTest {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
        test();
        System.out.println("finish2...");
	}
	
	public static List<Future<Boolean>> test() throws InterruptedException, ExecutionException{
		List<Future<Boolean>> futures = new ArrayList<>();
		ThreadPoolExecutor exec = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
        for(int i=0; i<100; i++){
        	final int tempi = i;
        	futures.add(exec.submit(()->{
        		Utils.locktime();
        		System.out.println("end"+tempi);
        		return true;
        	}));
        }
        exec.shutdown();
//        exec.shutdownNow();
        
        System.out.println("finish...");
        System.out.println("exec.isShutdown():"+exec.isShutdown());
        System.out.println("exec.isTerminated():"+exec.isTerminated());
//        for(Future<Boolean> f:futures){
//        	System.out.println(f.get());
//        }
//        System.out.println("exec.isTerminated():"+exec.isTerminated());
		return futures;
		
	}
	
}
