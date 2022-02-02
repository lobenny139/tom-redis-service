package com.tom.redis.test;

import com.tom.redis.service.IPubService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.tom.redis.TestApplication.class)
@TestPropertySource(locations = "/test-application.properties")
public class TestPubSubService {

    @Autowired
    @Qualifier(value = "pubService")
    private IPubService pubService;

    @Test
    public void testPubSub() {
        pubService.publish("myTopic", "發布發布");
    }


}
