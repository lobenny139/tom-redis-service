package com.tom.redis.test;

import com.tom.redis.service.IGenericRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.tom.redis.TestApplication.class)
@TestPropertySource(locations = "/test-application.properties")
public class Test控管領獎 {
    @Autowired
    @Qualifier(value = "genericRedisService")
    private IGenericRedisService redisService;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    @Test
    public void ThreadTest(){
        redisService.set("prod-1234",200);
//        for(int i = 0; i <= 10000; i++){
//            new Thread(new MyThread(System.currentTimeMillis())).start();
//        }
    }

    class MyThread implements Runnable {
        long startTime;
        public MyThread(long time){
            this.startTime = time;
        }
        @Override
        public void run() {
            long left;
            long endTime = System.currentTimeMillis();
            if( (left=redisService.decr("prod-1234",1)) >= 0){
                System.out.println(Thread.currentThread().getName() + " 在 " + simpleDateFormat.format(new Date(startTime)) + " 參加領獎, 在 " + simpleDateFormat.format(new Date(endTime) ) +   " 領到, 剩" + left +"個, 耗時 " +  (endTime - startTime) + "ms <<<<<<<<" )  ;
            }
            else{
                System.out.println(Thread.currentThread().getName() + " 在 " + simpleDateFormat.format(new Date(startTime)) + " 參加領獎, 在 " + simpleDateFormat.format(new Date(endTime) ) +   " 沒領到, 剩" + left + "個, 耗時 " +  (endTime - startTime) + "ms")  ;
            }
        }
    }
}
