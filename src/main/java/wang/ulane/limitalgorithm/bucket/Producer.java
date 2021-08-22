package wang.ulane.limitalgorithm.bucket;

/***
 * 生产者 往桶中放令牌
 *
 */
public class Producer {
	
	//模拟注入，拿到令牌桶
	private TokenBucket tb = TokenBucket.getInstance();
	
	private int goodsSize;
	
	public Producer(int goodsSize) {
		super();
		this.goodsSize = goodsSize;
	}

	public void produce(){
		tb.addToken(goodsSize);
	}
	
}
