package com.tom.redis.beanServiceConfig;

import com.tom.redis.service.IPubService;
import com.tom.redis.service.IRedisService;
import com.tom.redis.service.provider.PubService;
import com.tom.redis.service.provider.RedisService;
import om.tom.redis.messageListener.RedisMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ServiceAccessConfig {
    /*
    * Redis连接工厂
    */
    @Bean(name= "jedisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    /**
     * 配置redisTemplate针对不同key和value场景下不同序列化的方式
     * @return
     */
    @Primary
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate( ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory( jedisConnectionFactory() );
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 定義redisService
     * @return
     */
    @Bean(name = "redisService" )
    public IRedisService redisService() {
        return new RedisService();
    }

    /**
     * PUB/SUB使用
     * @return
     */
    @Bean(name="redisMessageListenerContainer")
    public RedisMessageListenerContainer listenerContainer( ) {
        // 創建RedisMessageListenerContainer對象
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        // 設置 RedisConnection 工廠，多種 JavaRedis客戶端接入工廠
        redisMessageListenerContainer.setConnectionFactory( jedisConnectionFactory() );
        // 添加監聽器
//        redisMessageListenerContainer.addMessageListener(new DemoChannelTopicMessageListener(), new ChannelTopic("demoChannel"));
        redisMessageListenerContainer.addMessageListener(new RedisMessageListener(), new ChannelTopic("myTopic"));
        return redisMessageListenerContainer;
    }

    @Bean(name = "pubService")
    public IPubService pubService(){
        return new PubService();
    }


}

//https://inf.news/zh-hant/technique/c6680b1acb09ccdd2b7188b7c484a40d.html

//https://blog.csdn.net/KingCat666/article/details/77883813
//https://www.cnblogs.com/littleatp/p/10035786.html

//https://pdai.tech/md/db/nosql-redis/db-redis-x-pub-sub.html
//https://blog.csdn.net/weixin_43831204/article/details/108264163
//https://iter01.com/648948.html
//https://www.cnblogs.com/littleatp/p/10035786.html
//https://www.jdon.com/55323
//https://blog.csdn.net/KingCat666/article/details/77883813
//https://www.jdon.com/55323
