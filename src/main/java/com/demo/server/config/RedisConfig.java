/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-29T14:34:16.341+08:00
 */

package com.demo.server.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // 配置连接工厂
        template.setConnectionFactory(factory);

        // JdkSerializationRedisSerializer在存储内容时，除了属性的内容外还存了其它内容在里面，总长度长，且不容易阅读(\x0\x0\x00\x000\x0\x0)
        // Jackson2JsonRedisSerializer不是很好用. 所以采用FastJsonRedisSerializer. 也可以直接使用StringRedisSerializer,更为清晰明了.
        // 使用FastJsonRedisSerializer序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        FastJsonRedisSerializer jacksonSeializer = new FastJsonRedisSerializer(Object.class);

        // 创建FastJsonConfig对象并设定序列化规则
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        jacksonSeializer.setFastJsonConfig(fastJsonConfig);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(stringRedisSerializer);
        // 值采用json序列化
        template.setValueSerializer(jacksonSeializer);

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jacksonSeializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 对hash类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}
