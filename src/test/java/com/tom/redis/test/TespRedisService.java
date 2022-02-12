package com.tom.redis.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.redis.service.IGenericRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.tom.redis.TestApplication.class)
@TestPropertySource(locations = "/test-application.properties")
public class TespRedisService {

    @Autowired
    @Qualifier(value = "genericRedisService")
    private IGenericRedisService redisService;

    @Test
    public void testIncr(){
        System.out.println(redisService.incr("prod-1234567", 1000L));
    }


    @Test
    public void testdecr(){
        System.out.println(redisService.decr("prod-1234567", 1L));
    }



    @Test
    public void testString() throws JsonProcessingException {
        redisService.set("key-0", "hi benny", 60);
        System.out.println(redisService.get("key-0"));
        //redisService.set("key-00", "hi benny00", 60);
        //redisService.del(0,"key-00");
        System.out.println(redisService.get("prod-1234567"));
    }

    @Test
    public void testMap() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("a", "map-a");
        map.put("b", "map-b");
        redisService.set("ab", new ObjectMapper().writeValueAsString(map), 60);
        System.out.println(redisService.get("ab"));
    }


    @Test
    public void testDel(){
        redisService.del("ab", "key-00");
    }


    @Test
    public void testGetAllKeys(){
        redisService.set("key-0", "hi benny0", 60);
        redisService.set("kkey-1", "hi benny1", 60);
        redisService.set("key-2", "hi benny2", 60);
        redisService.set("key-4", "hi benny4", 60);
        redisService.set("key-3", "hi benny3", 60);
        redisService.set("kkey-5", "hi benny5", 60);
        redisService.set("key-5", "hi benny55", 60);
        redisService.set("key-6", "hi benny6", 60);
        redisService.set("key-7", "hi benny7", 60);
        redisService.set("key-9", "hi benny9", 60);
        System.out.println(redisService.getAllKeys("kk"));
    }

}



