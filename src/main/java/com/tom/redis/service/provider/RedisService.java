package com.tom.redis.service.provider;

import com.tom.redis.service.IRedisService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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
public class RedisService implements IRedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired(required = true)
    @Qualifier(value = "redisTemplate")
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = true)
    @Qualifier(value = "jedisConnectionFactory")
    protected JedisConnectionFactory jedisConnectionFactory;

    protected void setDatabase(int dbIndex) {
        if(dbIndex < 0 || dbIndex > 16){
            throw new RuntimeException("資料庫的值只能為 0~15");
        }
        getJedisConnectionFactory().setDatabase(dbIndex);
        getRedisTemplate().setConnectionFactory(jedisConnectionFactory);
    }

    protected int getDatabase(){
        return getJedisConnectionFactory().getDatabase();
    }

    @Override
    public Object get(int dbIndex, String key) {
        if(key == null){return null;}
        setDatabase(dbIndex);
        logger.info("嘗試從Redis[" + getDatabase() + "]取键值[" + key +"]");
        Object value = getRedisTemplate().opsForValue().get(key);
        logger.info("成功從Redis[" + getDatabase() + "]取得键值[" + key +"]");
        return value;
    }


    @Override
    public boolean set(int dbIndex, String key, Object value, long time)  {
        try {
            if(key == null){throw new Exception("Key Can't be null.");}
            setDatabase(dbIndex);
//            logger.info("嘗試放入鍵/值[" + key +"/" + value+"]到Redis[" + getDatabase() + "]");
            if (time > 0) {
                getRedisTemplate().opsForValue().set(key, value, time, TimeUnit.SECONDS);
                logger.info("鍵[" + key +"]的有效期為 " + time + " 秒後");
            }else{
                getRedisTemplate().opsForValue().set(key, value);
            }
            logger.info("成功放入鍵/值[" + key +"/" + value+"]到Redis[" + getDatabase() + "]");
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    public void del(int dbIndex, String... keys) {
        try {
            setDatabase(dbIndex);
            if (keys != null && keys.length > 0) {
                for (String key : keys) {
                    logger.info("從Redis[" + dbIndex + "]中刪除键[" + key + "]");
                    getRedisTemplate().delete(key);
                }
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void del( String... keys) {
        del(0, keys);
    }

}
