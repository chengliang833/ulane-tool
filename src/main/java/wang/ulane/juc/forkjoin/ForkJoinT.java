package wang.ulane.juc.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import wang.ulane.juc.Utils;

public class ForkJoinT {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		System.out.println("start...");
		ForkJoinPool fjp = new ForkJoinPool();
		int[] nums = Utils.buildRandomIntArray(100000000);
		System.out.println("start sum...");
		
		long start = System.currentTimeMillis();
		long sum = 0;
		for(int i=0,length=nums.length; i<length; i++){
			sum += nums[i];
		}
		System.out.println(sum);
		System.out.println(System.currentTimeMillis() - start);
		
		start = System.currentTimeMillis();
		ForkJoinTask<Long> fjt = fjp.submit(new ForkJoinRecursiveT(Runtime.getRuntime().availableProcessors(), nums));
		System.out.println(fjt.get());
		System.out.println(System.currentTimeMillis() - start);
		
	}
	
}
