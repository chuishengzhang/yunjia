package com.zcs.yunjia.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActiveMQConsumer {
	//queue消息是存在服务器的  消费一次就没了
	//topic消息不存在服务器 所以订阅前的消息就丢失了 只有后面发布的消息才能接收到
	
	//@Test
	public void testQueueConsumer() throws Exception{
		//1.创建工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://47.100.214.167:61616");
		//创建连接 
		Connection connection = factory.createConnection();
		connection.start();
		//创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建目的地 即消息的发送者
		Queue queue = session.createQueue("queue-test");
		//创建消费者
		MessageConsumer consumer = session.createConsumer(queue);
		//接收消息
		//第一种方法 设置监听器 开启了一个新线程
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					//判断消息是否为文本类型
					if(message instanceof TextMessage){
						TextMessage me = (TextMessage)message;
						System.out.println(me.getText());
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		Thread.sleep(5000l);
		//关闭
		session.close();
		consumer.close();
		connection.close();
	}
	
	//@Test
	public void testTopicConsumer() throws Exception{
		//创建连接工厂 连接远程服务器
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://47.100.214.167:61616");
		//创建连接 
		Connection connection = factory.createConnection();
		connection.start();
		//创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建目的地 即消息的发送者
		Topic topic = session.createTopic("topic-test");
		//创建消费者
		MessageConsumer consumer = session.createConsumer(topic);
		//添加监听器  开启了一个新线程
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				//判断是否为文本消息
				if(message instanceof TextMessage){
					TextMessage me = (TextMessage)message;
					try {
						System.out.println(me.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		Thread.sleep(50000);
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
