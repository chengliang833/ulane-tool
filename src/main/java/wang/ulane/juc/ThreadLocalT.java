package wang.ulane.juc;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalT {
	private static ThreadLocal<Map<Integer, Integer>> tlm = ThreadLocal.withInitial(()->new HashMap<>());
	private static ThreadLocal<SimpleDateFormat> tlsdf = ThreadLocal.withInitial(()->new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//1.7版本中初始化ThreadLocal
	private static ThreadLocal<SimpleDateFormat> tlsdf7 = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue(){
//			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	}; 
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(10, 10, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
		List<Future<String>> fs = new ArrayList<>();
		
		for(int i=0; i<100; i++){
			final int tempi = i;
			fs.add(tpe.submit(()->{
//				Calendar c = Calendar.getInstance();
//				LocalDateTime ldt = LocalDateTime.now();
				LocalDateTime ldt = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
				ldt = ldt.minusDays(tempi);
				Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
//				tlm.get().put(tempi, tempi);
				return tlsdf.get().format(date);
//				return sdf.format(date);
			}));
		}
//		System.out.println(tlm.get().get("1"));
		
		Set<String> set = new HashSet<>();
		for(Future<String> f:fs){
			set.add(f.get());
		}
		
		System.out.println(set.size());
		
	}
	
}
