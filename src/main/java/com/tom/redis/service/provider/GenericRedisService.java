package com.tom.redis.service.provider;

import com.tom.redis.service.IGenericRedisService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
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
public class GenericRedisService implements IGenericRedisService {

    private static Logger logger = LoggerFactory.getLogger(GenericRedisService.class);

    @Autowired(required = true)
    @Qualifier(value = "redisTemplate0")
    private RedisTemplate<String, Object> redisTemplate;

    protected int getDatabase(){
        return ((JedisConnectionFactory)this.getRedisTemplate().getConnectionFactory()).getDatabase();
    }

    @Override
    public Object get(String key) {
        try {
            if (key == null) {
                throw new RuntimeException("鍵值不能為空");
            }
            Object value = getRedisTemplate().opsForValue().get(key);
            if (value != null) {
                logger.info("成功從Redis[{}]取得鍵值[{}]", getDatabase(), key);
            } else {
                logger.warn("鍵[{}]不存在於Redis[{}]", key, getDatabase());
            }
            return value;
        }catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean set( String key, Object value, long time)  {
        if(key == null){throw new RuntimeException("鍵不能為空");}
        try {
            if (time > 0) {
                getRedisTemplate().opsForValue().set(key, value, time, TimeUnit.SECONDS);
                logger.info("鍵[{}]的有效期為 {} 秒後", key, time);
            }else{
                getRedisTemplate().opsForValue().set(key, value);
            }
            logger.info("成功放入[{}/{}]到Redis[{}]", key, value, getDatabase());
            return true;
        } catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        } catch (Exception e) {
            logger.warn("不能放入[{}/{}]到Redis[{}]", key, value, getDatabase());
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
                getRedisTemplate().delete(Arrays.asList(keys));
                logger.info("成功從Redis[{}]中刪除鍵{}", getDatabase(), Arrays.asList(keys) );
            }
        }catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAllKeys(String keyPreifx){
        try {
            StringBuffer targetKey = new StringBuffer();
            if (keyPreifx != null) {
                targetKey.append(keyPreifx.trim());
            }
            targetKey.append("*");

            Set<String> redisKeys = getRedisTemplate().keys(targetKey.toString());
            // Store the keys in a List
            List<String> keysList = new ArrayList<>();
            Iterator<String> it = redisKeys.iterator();
            while (it.hasNext()) {
                String data = it.next();
                keysList.add(data);
            }
            logger.info("成功從Redis[{}]中取得取得鍵(prefix={}), 共{}筆記錄.", getDatabase(), targetKey, redisKeys.size() );
            return keysList;
        }catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasKey(String key) {
        try{
            return getRedisTemplate().hasKey(key);
        }catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public long incr(String key, long delta) {
        try{
            return getRedisTemplate().opsForValue().increment(key, delta);
        }catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public long decr(String key, long delta) {
        try{
            return getRedisTemplate().opsForValue().decrement(key, delta);
        }catch(RedisConnectionFailureException e){
            e.printStackTrace();
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
