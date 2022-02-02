package com.tom.redis.service.provider;

import com.tom.redis.service.IPubService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Getter
@Setter
@Component
@Service
public class PubService implements IPubService {

    @Autowired(required = true)
    @Qualifier(value = "redisTemplate0")
    protected RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(String topic, String message) {
        getRedisTemplate().convertAndSend(topic, message);
    }
}
