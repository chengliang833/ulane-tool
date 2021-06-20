package wang.ulane.juc.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorT {
	
	/**
	 * BlockingQueue 阻塞队列，且put和take都有lock
	 * ConcurrentLinkedQueue 非阻塞队列，cas，无锁
	 * Disruptor 类似阻塞队列，但使用Sequence+cas，无锁
	 */
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		EventFactory<LogEvent> eventFactory = new LogEventFactory();
		int ringBufferSize = 1 << 10;
		//ProducerType.MULTI 多生产者模式
		//ProducerType.SINGLE 单生产者模式，好像也支持多线程
		Disruptor<LogEvent> disruptor = new Disruptor<LogEvent>(eventFactory, ringBufferSize, es, ProducerType.SINGLE, new YieldingWaitStrategy());
		disruptor.handleEventsWith(new LogEventHandler(1));
		
		disruptor.start();
		
		RingBuffer<LogEvent> ringBuffer = disruptor.getRingBuffer();
		
		LogEventProducer producer = new LogEventProducer(ringBuffer);
		
		for(int j=65; j<65+10; j++){
			final int tempj = j;
			Thread t = new Thread(()->{
				for(int i=1; i<=100; i++){
					try {
						System.out.println("记录日志"+(char)tempj+i);
						producer.onData("日志"+(char)tempj+i);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
		
		disruptor.shutdown();
		es.shutdown();
		
		System.out.println(LogEventHandler.all);
		System.out.println("finish...");
	}
	
}
