package com.zcs.yunjia.content.service.jedis;


import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * jedis单机版实现类
 * @author zcs
 */
public class JedisSingleClient implements JedisClient {
	@Autowired
	private JedisPool jedisPool;
	private Jedis jedis;
	
	JedisSingleClient(JedisPool jedisPool){
		this.jedisPool = jedisPool;
		this.jedis = jedisPool.getResource();
	}
	
	@Override
	public String set(String key, String value) {
		return jedis.set(key, value);
	}

	@Override
	public String get(String key) {
		return jedis.get(key);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return jedis.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return jedis.hget(key, field);
	}

	@Override
	public void close() {
		jedis.close();
		jedisPool.close();
	}

	@Override
	public Long expire(String key,int seconds){
		return jedis.expire(key, seconds);
	}
	
	@Override
	public boolean exists(String key){
		return jedis.exists(key);
	}

	@Override
	public Long del(String... keys) {
		return jedis.del(keys);
	}

	@Override
	public Long hdel(String key, String... fields) {
		return jedis.hdel(key, fields);
	}
}
