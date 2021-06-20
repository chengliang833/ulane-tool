package wang.ulane.juc;

public class VolatileT {
	
	private static volatile Integer a = 0, b = 0;
	private static Integer x = 0, y = 0;
	
	private static volatile boolean flag = true;
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(()->{
			while(flag){
//				x++;
//				System.out.println("in...");
			}
			System.out.println("out...");
		});
		t1.start();
		
		Thread.sleep(1000);
		Thread t2 = new Thread(()->{
			System.out.println("change...");
			flag = false;
		});
		
		t2.start();

		System.out.println("waiting...");
		Thread.sleep(100000);
		
	}
	
	public static void main1(String[] args) throws InterruptedException {
		Object obj = new Object();
		
		while(true){
			a = 0; b = 0;
			x = 0; y = 0;
			
			Thread t1 = new Thread(()->{
				a++;
				y = b;
			});

			Thread t2 = new Thread(()->{
				b++;
				x = a;
			});
			
			t1.start();
			t2.start();
			t1.join();
			t2.join();
			
			System.out.println("x:"+x+",y:"+y);
			if(x == 0 && y == 0){
				break;
			}
		}
		
	}
	
}
