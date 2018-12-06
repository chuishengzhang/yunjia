package com.zcs.yunjia.content.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	
	
	//单机版测试
	@Test
	public void testJedis(){
		Jedis jedis = new Jedis("47.100.214.167", 6379);
		jedis.set("haha", "单机版测试");
		System.out.println(jedis.get("haha"));
		
		//关闭客户端
		jedis.close();
	}
	
	//单机版连接池测试
	@Test
	public void testJedisPool(){
		//创建连接池
		JedisPool jedisPool = new JedisPool("47.100.214.167", 6379);
		//获取jedis对象
		Jedis jedis = jedisPool.getResource();
		jedis.set("jedisPool","jedis连接池测试");
		System.out.println(jedis.get("jedisPool"));
		//关闭jedis
		jedis.close();
		//关闭连接池(到应用结束才关闭)
		jedisPool.close();
	}
	
	//测试集群版
		@Test
		public void testCluster() throws IOException{
			//集群节点的host和port集合
			Set<HostAndPort> nodes = new HashSet<>();
			/*nodes.add(new HostAndPort("47.100.214.167",6381));
			nodes.add(new HostAndPort("47.100.214.167",6382));
			nodes.add(new HostAndPort("47.100.214.167",6383));
			nodes.add(new HostAndPort("47.100.214.167",6384));
			nodes.add(new HostAndPort("47.100.214.167",6385));
			nodes.add(new HostAndPort("47.100.214.167",6386));*/
			nodes.add(new HostAndPort("47.100.214.167", 6381));
			nodes.add(new HostAndPort("47.100.214.167", 6382));
			
			JedisCluster cluster = new JedisCluster(nodes);
			
			cluster.set("cluster2", "hahahah");
			
			System.out.println(cluster.get("cluster2"));
			
			//在应用关闭的时候在关闭
			cluster.close();
		}
}
