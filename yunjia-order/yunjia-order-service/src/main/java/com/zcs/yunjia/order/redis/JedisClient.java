package com.zcs.yunjia.order.redis;

import java.util.Map;

public interface JedisClient {
    public void set(String key, String value);
    public String get(String key);
    public void expire(String key, int second);
    public void close();
    public Long incr(String key);
    public boolean exists(String key);
    //哈希
    public Long hSet(String key, String field, String value);
    public String hGet(String key, String field);
    public Map<String,String> hGetAll(String key);
    public Long hDel(String key, String field);
}
