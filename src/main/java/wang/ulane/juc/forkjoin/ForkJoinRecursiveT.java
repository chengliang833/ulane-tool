package wang.ulane.juc.forkjoin;

import java.util.concurrent.RecursiveTask;

public class ForkJoinRecursiveT extends RecursiveTask<Long> {
	
	private static final long serialVersionUID = 2772940674001298607L;
	
	private int threadNum;
	private int currentSplitNum;
	private int[] nums;
	private int start;
	private int end;
	
	public ForkJoinRecursiveT(int threadNum, int[] nums) {
		super();
		this.threadNum = threadNum;
		this.currentSplitNum = 1;
		this.nums = nums;
//		this.start = 0;
		this.end = nums.length;
	}

	public ForkJoinRecursiveT(int threadNum, int currentSplitNum, int[] nums, int start, int end) {
		super();
		this.threadNum = threadNum;
		this.currentSplitNum = currentSplitNum;
		this.nums = nums;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Long compute() {
		if(currentSplitNum >= threadNum){
			long sum = 0;
			for(int i=start; i<end; i++){
				sum += nums[i];
			}
//			System.out.println(Thread.currentThread().getName()+"start:"+start);
//			System.out.println(Thread.currentThread().getName()+"end:"+end);
//			System.out.println(Thread.currentThread().getName()+"sum:"+sum);
			return sum;
		}else{
			int splitToNum = 2;
			int mid = start + (end - start) / splitToNum;
//			System.out.println(Thread.currentThread().getName()+"start:"+start);
//			System.out.println(Thread.currentThread().getName()+"mid:"+mid);
//			System.out.println(Thread.currentThread().getName()+"end:"+end);
			int nextNum = this.currentSplitNum * splitToNum;
			ForkJoinRecursiveT left = new ForkJoinRecursiveT(this.threadNum, nextNum, nums, start, mid);
			ForkJoinRecursiveT right = new ForkJoinRecursiveT(this.threadNum, nextNum, nums, mid, end);
			
//			left.fork();
//			right.fork(); 
			invokeAll(left, right);
			
			long rleft = left.join();
			long rright = right.join();
			
			return rleft + rright;
		}
	}

}
