package com.zcs.yunjia.sso.jedis.impl;

import com.zcs.yunjia.sso.jedis.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * jedisPool实现的redis单机版
 */
public class JedisClientPool implements JedisClient {

    @Resource
    private Jedis jedis;
    //private JedisPool jedisPool;

    //private Jedis jedis = jedisPool.getResource();

    public void set(String key,String value){
        //Jedis jedis = jedisPool.getResource();
        jedis.set(key,value);
    }
    public String get(String key){
       // Jedis jedis = jedisPool.getResource();
        return jedis.get(key);
    }
    public void expire(String key,int second){
       // Jedis jedis = jedisPool.getResource();
        jedis.expire(key,second);
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
