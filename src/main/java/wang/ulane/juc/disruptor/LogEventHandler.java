package wang.ulane.juc.disruptor;

import com.lmax.disruptor.EventHandler;

public class LogEventHandler implements EventHandler<LogEvent>{
	
	private Integer i;
	public LogEventHandler(Integer i) {
		this.i = i;
	}
	
	public static int all = 0;

	@Override
	public void onEvent(LogEvent arg0, long arg1, boolean arg2) throws Exception {
		all++;
		System.out.println(i+"打印："+arg0.getValue());
		if(i == 1){
//			Thread.sleep(10);
		}else{
			Thread.sleep(2000);
		}
		System.out.println(i+"打印完了："+arg0.getValue());
	}
	
}
