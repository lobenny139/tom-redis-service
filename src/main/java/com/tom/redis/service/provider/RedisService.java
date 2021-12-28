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
//@AllArgsConstructor
public class RedisService implements IRedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired(required = true)
    @Qualifier(value = "redisTemplate")
    protected RedisTemplate<String, Object> redisTemplate;

    protected void setDatabase(int dbIndex) {
        if(dbIndex < 0 || dbIndex > 16){
            throw new RuntimeException("DB index must between 1 and 16");
        }
        ((JedisConnectionFactory)this.getRedisTemplate().getConnectionFactory()).setDatabase(dbIndex);
    }

    protected int getDatabase(){
        return ((JedisConnectionFactory)this.getRedisTemplate().getConnectionFactory()).getDatabase();
    }

    @Override
    public Object get(int dbIndex, String key) {
        if(key == null){return null;}
        logger.info("Try to get object[key=" + key +"] from redis[" + dbIndex + "]");
        setDatabase(dbIndex);
        Object value = redisTemplate.opsForValue().get(key);
        logger.info("Success get object[key=" + key +"] from redis[" + dbIndex + "]");
        return value;
    }


    @Override
    public Object get(String key) {
        return get(0, key);
    }

    @Override
    public boolean set(String key, Object value) {
        return set(0, key, value, 0);
    }

    @Override
    public boolean set(String key, Object value, long time) {
        return set(0, key, value, time);
    }

    @Override
    public boolean set(int dbIndex, String key, Object value, long time)  {
        try {
            if(key == null){throw new Exception("Key Can't be null.");}
            logger.info("Try to put object[key=" + key +"] into redis[" + dbIndex + "]");
            setDatabase(dbIndex);
            redisTemplate.opsForValue().set(key, value);
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                logger.info("Object[key=" + key +"] will expired in " + time + " second(s) later");
            }else{
                redisTemplate.opsForValue().set(key, value);
            }
            logger.info("Success put object[key=" + key +"] into redis[" + dbIndex + "]");
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void del(int dbIndex, String... keys) {
        setDatabase(dbIndex);
        if (keys != null && keys.length > 0) {
            for(String key : keys){
                redisTemplate.delete(key);
            }
        }
    }

    public void del( String... keys) {
        del(0, keys);
    }

}
