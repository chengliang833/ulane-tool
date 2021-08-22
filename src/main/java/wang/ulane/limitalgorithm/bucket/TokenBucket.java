package wang.ulane.limitalgorithm.bucket;

import java.util.concurrent.locks.ReentrantLock;

/***
 * 令牌桶
 *
 */
public class TokenBucket {
	
	private TokenBucket() {
		super();
	}
	
	public static TokenBucket getInstance(){
		return Singleton.instance;
	}
	
	/**
	 * 令牌数
	 */
	private int tokens = 0;
	private int maxSize = 0;
	
	/**
	 * 增加令牌，返回剩余数量
	 * @param size
	 * @return
	 */
	public synchronized int addToken(int size){
		int tokenstemp = tokens + size;
		if(tokenstemp > maxSize){
			tokens = maxSize;
		}else{
			tokens = tokenstemp;
		}
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
	
	public int getTokens() {
		return tokens;
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}



	private static class Singleton{
		private static final TokenBucket instance = new TokenBucket();
	}
}
