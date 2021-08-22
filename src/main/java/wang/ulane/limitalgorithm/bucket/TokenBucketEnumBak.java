package wang.ulane.limitalgorithm.bucket;

import java.util.concurrent.locks.ReentrantLock;

/***
 * 令牌桶
 *
 */
public enum TokenBucketEnumBak {
	instance;
	
	/**
	 * 令牌数
	 */
	private int tokens = 0;
	
	/**
	 * 增加令牌，返回剩余数量
	 * @param size
	 * @return
	 */
	public synchronized int addToken(int size){
		tokens = tokens + size;
		return tokens;
	}
	
	/**
	 * 获取令牌，返回是否获取成功
	 * @param size
	 * @return
	 */
	public synchronized boolean getToken(int size){
		boolean r = true;
		if(tokens >= size){
			tokens = tokens - size;
		}else{
			r = false;
		}
		return r;
	}
	
}
