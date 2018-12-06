package com.zcs.yunjia.content.service.jedis;

import java.io.IOException;

public interface JedisClient {
	public String set(String key,String value);
	public String get(String key);
	public Long hset(String key,String field,String value);
	public String hget(String key,String field);
	public void close() throws IOException;
	public Long expire(String key,int seconds);
	public boolean exists(String key);
	public Long del(String... keys);
	public Long hdel(String key,String... fields);
	
}
