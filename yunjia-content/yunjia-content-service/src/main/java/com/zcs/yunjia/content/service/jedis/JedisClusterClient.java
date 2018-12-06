package com.zcs.yunjia.content.service.jedis;

import java.io.IOException;
import java.util.Set;


import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * jedis集群版实现类
 * @author zcs
 */
public class JedisClusterClient implements JedisClient {

	private JedisCluster jedisCluster;
	
	public JedisClusterClient(Set<HostAndPort> nodes,JedisPoolConfig poolConfig) {
		this.jedisCluster = new JedisCluster(nodes, poolConfig);
	}
	
	@Override
	public String set(String key, String value) {
		return jedisCluster.set(key, value);
	}

	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return jedisCluster.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return jedisCluster.hget(key, field);
	}

	@Override
	public void close() throws IOException {
		jedisCluster.close();
	}

	@Override
	public Long expire(String key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}

	@Override
	public boolean exists(String key) {
		return jedisCluster.exists(key);
	}

	@Override
	public Long del(String... keys) {
		return jedisCluster.del(keys);
	}

	@Override
	public Long hdel(String key, String... fields) {
		return jedisCluster.hdel(key, fields);
	}

}
