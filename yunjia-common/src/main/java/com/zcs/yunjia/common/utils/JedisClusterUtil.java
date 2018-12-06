package com.zcs.yunjia.common.utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * jedis集群工具类
 * @author 1
 *
 */
public class JedisClusterUtil implements Serializable{
	
	/**
	 * 获取jedis集群对象
	 * @param host 服务器地址
	 * @param port 端口
	 * @return JedisCluster
	 */
	public static JedisCluster getJedisCluster(String host,int... port){
		
		Set<HostAndPort> nodes = new HashSet<>();
		for (int p : port) {
			nodes.add(new HostAndPort(host, p));
		}
		JedisCluster cluster = new JedisCluster(nodes);
		
		return cluster;
	}

}
