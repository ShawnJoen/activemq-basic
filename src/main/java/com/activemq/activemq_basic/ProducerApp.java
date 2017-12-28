package com.activemq.activemq_basic;

import java.io.UnsupportedEncodingException;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ActiveMq生产者
 */
public class ProducerApp {
	/* 
	 * Producer/Consumer和 Pub/Sub的区别在于  
	 * Producer/Consumer:
	 * 		有多个Consumer时 Producer发送一个消息  随机(空闲的)只有其中一个Consumer才可以收到消息, 一个消息只能消费一次 消费后消失
	 * 		未消费的ActiveMQ缓存留着等消费
	 * Pub/Sub:
	 * 		有多个Sub(Consumer)时 Pub(Producer)发送一个消息 每个Sub(Consumer)都可以收到
	 * 		发送时无一个订阅者阅读也是不会留着 未消费过一次也会消失
	 * */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerApp.class);
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String MQ_USERNAME = "";
    private static final String MQ_PASSWORD = "";
    private static final String MQ_BROKER_URL = "tcp://127.0.0.1:61616";
    private static final String SUBJECT = "CustomQueueKEY";
    
	public static void main(String[] args) throws JMSException, InterruptedException {
        //初始化连接工厂
        //ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(MQ_USERNAME, MQ_PASSWORD, MQ_BROKER_URL);
        //获得连接
        Connection conn = connectionFactory.createConnection();
        //启动连接
        conn.start();
        //创建Session，此方法第一个参数表示会话是否在事务中执行，第二个参数设定会话的应答模式
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建队列
        Destination dest = session.createQueue(SUBJECT);
        //createTopic方法用来创建Topic
        //session.createTopic("TOPIC");//Pub
        //通过session可以创建消息的生产者
        MessageProducer producer = session.createProducer(dest);
        for (int i = 0;i < 10;i++) {
            //初始化一个mq消息
            TextMessage message = session.createTextMessage("测试信息：" + i);
            //发送消息
            producer.send(message);
            //LOGGER.info("发送消息 测试信息： {}", i);
        }
        //发送消息
        //writeTextMessage(session, producer,"测试文字转编码！！");
        //关闭连接
        conn.close();
	}
	
    private static void writeTextMessage(Session session, MessageProducer messageProducer, String txt) throws JMSException {        
    	try {            
    		String iso = new String(txt.getBytes("GBK"), "ISO-8859-1");   
    		TextMessage message = session.createTextMessage(); 
    		message.setStringProperty("language", "java"); 
    		message.setText(iso);
    		messageProducer.send(message);
    		//LOGGER.info("发送消息 测试信息： {}", message);
    	} catch (UnsupportedEncodingException e) {            
			e.printStackTrace();        
    	}     
	}
}