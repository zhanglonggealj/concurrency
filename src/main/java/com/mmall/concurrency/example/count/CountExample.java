package com.mmall.concurrency.example.count;

import com.mmall.concurrency.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CountExample {
    //同时并发执行的线程数
    public static int THREADTOTAL = 200;
    //请求总数
    public static int CLIENTTOTAL = 5000 ;

    public static AtomicInteger COUNT = new AtomicInteger(0) ;

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();//定义线程池
        final Semaphore semaphore = new Semaphore(THREADTOTAL);//信号量
        final CountDownLatch countDownLatch = new CountDownLatch(CLIENTTOTAL);
        for(int i = 0 ; i < CLIENTTOTAL ; i++){
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.err.println(COUNT.get());
    }

    private static void add(){
        COUNT.incrementAndGet();
    }
}
