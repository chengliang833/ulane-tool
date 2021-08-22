package wang.ulane.limitalgorithm.slide;

import java.util.LinkedList;

public class Slider {
	
	private LinkedList<SlideObj> clq = new LinkedList<>();
	private int allSize = 0;
	
	private long timeLimit = 0;
	private int maxSize = 0;
	
	public synchronized boolean pass(SlideObj sliNew){
//		System.out.println(sliNew);
		SlideObj sli = clq.peek();
		while(sli != null){
			Long timestamp = sli.getTimestamp();
			//过期的直接poll掉
			if(System.currentTimeMillis() - timestamp <= timeLimit){
				//没过期之后，再判断数量上限
				return checkSize(sliNew);
			}
			sub(sli);
			sli = clq.peek();
		}
		
		return checkSize(sliNew);
	}
	
	private boolean checkSize(SlideObj sliNew){
		if(allSize + sliNew.getSize() <= maxSize){
			add(sliNew);
			return true;
		}else{
			return false;
		}
	}
	
	private void add(SlideObj sliNew){
		clq.add(sliNew);
		allSize += sliNew.getSize();
	}
	private void sub(SlideObj sli){
		clq.poll();
		allSize -= sli.getSize();
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
