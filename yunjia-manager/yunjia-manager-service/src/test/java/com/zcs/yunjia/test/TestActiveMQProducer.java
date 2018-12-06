package com.zcs.yunjia.test;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActiveMQProducer {
	@Test
	public void testQueueProducer() throws JMSException{
		//1.创建连接工厂 指定远程服务器 tcp://ip:port(固定写法)
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://47.100.214.167:61616");
		//2.创建连接
		Connection connection = factory.createConnection();
		//3.打开连接
		connection.start();
		//4.创建session
		//第一个参数表示是否开启分布式事务 一般写false
		//第二个参数只有在分布式事务为false是有效 设置消息自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建目的地
		Queue queue = session.createQueue("queue-test");
		//创建发送者
		MessageProducer producer = session.createProducer(queue);
		//创建消息
		TextMessage textMessage = session.createTextMessage("queue 点对点消息");
		//发送消息
		producer.send(queue, textMessage);
		//关闭
		session.close();
		connection.close();
	}
	
	
	public static void testTopicProducer() throws JMSException{
		//创建连接工厂 连接远程服务器
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://47.100.214.167:61616");
		//创建连接并开启
		Connection connection = factory.createConnection();
		connection.start();
		//创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建目的地
		Topic topic = session.createTopic("topic-test");
		//创建发送者
		MessageProducer producer = session.createProducer(topic);
		//创建消息
		TextMessage textMessage = session.createTextMessage();
		textMessage.setText("topic 测试消息");
		//发送消息
		producer.send(textMessage);
		//关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	public static void main(String[] args) throws JMSException {
		//testTopicProducer();
	}
}
