package com.zcs.yunjia.sso.jedis;

public interface JedisClient {
    public void set(String key, String value);
    public String get(String key);
    public void expire(String key, int second);
}
