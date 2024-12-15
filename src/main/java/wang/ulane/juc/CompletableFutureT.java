package wang.ulane.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureT {

    static ExecutorService es = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        System.out.println("main:"+Thread.currentThread());
        es.execute(()->{
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.complete("abc");
            System.out.println("es finish...");
        });
        Thread.sleep(10000);
        //这里提交任务, 由内部子线程执行,
        future.thenAcceptAsync(r->{
            System.out.println(Thread.currentThread()+":1:"+r);
        });
        //这里提交任务, 由执行.complete(v)的线程执行, 或者已经complete了，则由当前线程执行
        future.thenAccept(r->{
            System.out.println(Thread.currentThread()+":2:"+r);
        });
        System.out.println("finish...");
        es.shutdown();
        Thread.sleep(10000000L);
    }

}
