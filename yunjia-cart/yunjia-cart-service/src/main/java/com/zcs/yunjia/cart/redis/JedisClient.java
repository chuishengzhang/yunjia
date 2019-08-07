package com.zcs.yunjia.cart.redis;

public interface JedisClient {
    public void set(String key, String value);
    public String get(String key);
    public void expire(String key, int second);
    public void close();
}
