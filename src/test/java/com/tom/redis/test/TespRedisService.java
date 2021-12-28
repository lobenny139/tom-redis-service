package com.tom.redis.test;

import com.tom.redis.service.IRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.tom.redis.TestApplication.class)
@TestPropertySource(locations = "/test-application.properties")
public class TespRedisService {

    @Autowired
    @Qualifier(value = "redisService")
    private IRedisService redisService;

    @Test
    public void testString() {
        redisService.set(0,"key-0", "hi benny", 60);
        System.out.println(redisService.get(0, "key-0"));
        redisService.set("key-00", "hi benny00", 60);
        //redisService.del(0,"key-00");
        System.out.println(redisService.get(0, "key-00"));
    }

    @Test
    public void testDel(){
        redisService.del(-1,"key-0", "key-00");
    }

}
