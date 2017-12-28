package com.activemq.activemq_pub_sub;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ActiveMq发布者
 */
public class Publish {

	private static final Logger LOGGER = LoggerFactory.getLogger(Publish.class);
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String MQ_USERNAME = "";
    private static final String MQ_PASSWORD = "";
    private static final String MQ_BROKER_URL = "tcp://127.0.0.1:61616";
    private ConnectionFactory connectionFactory;  
    private Connection conn;  
    private Destination destination;  
    private MessageProducer producer;  
    private Session session;  
    private static final String SUBJECT = "CustomTopic";
  
    public Publish() throws JMSException {  
    	
    	//初始化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);  
        //获得连接
        conn = connectionFactory.createConnection();  
        //启动连接
        conn.start();  
        //创建Session，此方法第一个参数表示会话是否在事务中执行，第二个参数设定会话的应答模式
        session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);  
        //createTopic方法用来创建Topic
        destination = session.createTopic(SUBJECT);  
        //通过session可以创建消息的发布者
        producer = session.createProducer(destination);  
    }  
  
    public void publishMessage() throws JMSException {  
        
        for (int i = 0;i < 10;i++) {
            //初始化消息
        	TextMessage textMessage = this.session.createTextMessage("发布测试信息：" + i);  
            //发布消息
            this.producer.send(destination, textMessage);
            //LOGGER.info("发布测试信息： {}", i);
        }
        //关闭连接
        conn.close();
    }
    
    public static void main(String[] args) throws JMSException {  
        
        Publish publish  = new Publish();  
        publish.publishMessage();  
    }  
}