package wang.ulane.juc.disruptor;

import com.lmax.disruptor.RingBuffer;

public class LogEventProducer {
	
	public final RingBuffer<LogEvent> ringBuffer;

	public LogEventProducer(RingBuffer<LogEvent> ringBuffer) {
		super();
		this.ringBuffer = ringBuffer;
	}
	
	public void onData(String msg){
		//sequencer.next() current缓存在sequence中，UNSAFE.getLongVolatile获取，解决伪共享，类似于long p1, p2, p3, p4, p5, p6, p7...
		long sequence = ringBuffer.next();
		
		LogEvent longEvent = ringBuffer.get(sequence);
		longEvent.setValue(msg);
		
		ringBuffer.publish(sequence);
	}
	
}
