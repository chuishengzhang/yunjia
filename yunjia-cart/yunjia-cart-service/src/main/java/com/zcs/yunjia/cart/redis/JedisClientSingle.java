package com.zcs.yunjia.cart.redis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class JedisClientSingle implements JedisClient {
    @Autowired
    private Jedis jedis;

    public void set(String key,String value){
        jedis.set(key,value);
        jedis.close();
    }
    public String get(String key){
       return  jedis.get(key);
    }
    public void expire(String key,int second){
        jedis.expire(key,second);
        jedis.close();
    }
    public void close(){
        jedis.close();
    }

    public Long hSet(String key,String field,String value){
       return jedis.hset(key,field,value);
    }
    public String hGet(String key,String field){
        return jedis.hget(key,field);
    }
    public Map<String,String> hGetAll(String key){
        return jedis.hgetAll(key);
    }
    public Long hDel(String key,String field){
        return jedis.hdel(key,field);
    }
}
