package com.tom.redis.service.provider;

import com.tom.redis.service.IRedisService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: 移动端Redis缓存实现类
 * @author: YuXD
 * @create: 2021-01-05 10:40
 * reference: https://blog.csdn.net/yu102655/article/details/112217778
 **/
@Getter
@Setter
@Component
@Service
@AllArgsConstructor
public class RedisService implements IRedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    private int database;

    public void setDatabase(int database) {
        this.database = database;
        ((JedisConnectionFactory) Objects.requireNonNull(this.getRedisTemplate().getConnectionFactory())).setDatabase(database);
    }

    protected int getDatabase(){
        return ((JedisConnectionFactory)this.getRedisTemplate().getConnectionFactory()).getDatabase();
    }

    @Autowired(required = true)
    @Qualifier(value = "redisTemplate")
    protected RedisTemplate<String, Object> redisTemplate;


    @Override
    public Object get(String key) {
        if(key == null){return null;}
        setDatabase(database);
        logger.info("Try to get object[key=" + key +"] from redis[" + getDatabase() + "]");
        Object value = redisTemplate.opsForValue().get(key);
        logger.info("Success get object[key=" + key +"] from redis[" + getDatabase() + "]");
        return value;
    }

    @Override
    public boolean set(String key, Object value) {
        setDatabase(database);
        return set(key, value, 0);
    }

    @Override
    public boolean set(String key, Object value, long time) {
        setDatabase(database);
        try {
            logger.info("Try to put object[key=" + key +"] into redis[" + getDatabase() + "]");
            redisTemplate.opsForValue().set(key, value);
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                logger.info("Object[key=" + key +"] will expired in " + time + " second(s) later");
            }else{
                redisTemplate.opsForValue().set(key, value);
            }
            logger.info("Success put object[key=" + key +"] into redis[" + getDatabase() + "]");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
