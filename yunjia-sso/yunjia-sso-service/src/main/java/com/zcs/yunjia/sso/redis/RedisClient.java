package com.zcs.yunjia.sso.redis;

public interface RedisClient {
    public void set(String key,String value);
    public String get(String key);
    public void expire(String key,int second);
    public void close();
}
