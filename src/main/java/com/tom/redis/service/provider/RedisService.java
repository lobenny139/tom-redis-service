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
    @Qualifier(value = "redisTemplate1")
    protected RedisTemplate<String, Object> redisTemplate;

    protected int getDatabase(){
        return ((JedisConnectionFactory)this.getRedisTemplate().getConnectionFactory()).getDatabase();
    }

    @Override
    public Object get(String key) {
        if(key == null){throw new RuntimeException("鍵值不能為空");}
        Object value = getRedisTemplate().opsForValue().get(key);
        if(value != null ){
            logger.info("成功從Redis[" + getDatabase() +"]取得鍵值[" + key +"]");
        }else{
            logger.warn("鍵值[" + key + "]不存在於Redis[" + getDatabase() + "]");
        }
        return value;
    }

    @Override
    public boolean set( String key, Object value, long time)  {
        if(key == null){throw new RuntimeException("鍵不能為空");}
        try {
            if (time > 0) {
                getRedisTemplate().opsForValue().set(key, value, time, TimeUnit.SECONDS);
                logger.info("鍵[" + key +"]的有效期為 " + time + " 秒後");
            }else{
                getRedisTemplate().opsForValue().set(key, value);
            }
            logger.info("成功放入鍵/值[" + key +"/" + value+"]到Redis[" + getDatabase() + "]");
            return true;
        } catch (Exception e) {
            logger.warn("不能放入鍵/值[" + key +"/" + value+"]到Redis[" + getDatabase() +"]");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean set(String key, Object value) {
        return set(key, value, 0);
    }

    @Override
    public void del(String... keys) {
        try {
            if (keys != null && keys.length > 0) {
                for (String key : keys) {
                    logger.info("從Redis[" + getDatabase() +"]中刪除键[" + key + "]");
                    getRedisTemplate().delete(key);
                }
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
