package wang.ulane.limitalgorithm.slide;

import java.util.LinkedList;

public class NumSlider {
	
	private LinkedList<Long> clq = new LinkedList<>();
	
	private long timeLimit = 0;
	private int maxSize = 0;
	
	public NumSlider() {
		super();
	}

	public NumSlider(long timeLimit, int maxSize) {
		super();
		this.timeLimit = timeLimit;
		this.maxSize = maxSize;
	}

	public synchronized boolean pass(Long timeNew){
		Long timestamp = clq.peek();
		while(timestamp != null){
			if(System.currentTimeMillis() - timestamp <= timeLimit){
				//没过期之后，再判断数量上限
				return checkSize(timeNew);
			}
			//过期的直接poll掉
			clq.poll();
			timestamp = clq.peek();
		}
		
		return checkSize(timeNew);
	}
	
	private boolean checkSize(Long timeNew){
		if(clq.size() + 1 <= maxSize){
//			System.out.println(clq.size());
			boolean a = clq.add(timeNew);
//			System.out.println(a);
			return true;
		}else{
			return false;
		}
	}
	
	public long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
}
