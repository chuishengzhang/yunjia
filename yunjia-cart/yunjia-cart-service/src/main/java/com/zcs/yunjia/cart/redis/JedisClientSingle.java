package com.zcs.yunjia.cart.redis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

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
}
