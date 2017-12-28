package com.activemq.activemq_pub_sub;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;

/**
 * ActiveMq订阅者
 */
public class Subscribe {
	/* 
	 * Producer/Consumer和 Pub/Sub的区别在于  
	 * Producer/Consumer:
	 * 		有多个Consumer时 Producer发送一个消息  随机(空闲的)只有其中一个Consumer才可以收到消息, 一个消息只能消费一次 消费后消失
	 * Pub/Sub:
	 * 		有多个Sub(Consumer)时 Pub(Producer)发送一个消息 每个Sub(Consumer)都可以收到
	 * */
	private static final Logger LOGGER = LoggerFactory.getLogger(Subscribe.class);
	private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String MQ_USERNAME = "";
    private static final String MQ_PASSWORD = "";
    private static final String MQ_BROKER_URL = "tcp://127.0.0.1:61616";
    private ConnectionFactory connectionFactory;  
    private Connection conn;  
    private Destination destination;  
    private MessageConsumer consumer;  
    private Session session;  
    private static final String SUBJECT = "CustomTopic";
    
    public Subscribe() throws JMSException {
    	
        connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);  
        conn = connectionFactory.createConnection();  
        conn.start();  
        session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);  
        destination = session.createTopic(SUBJECT);  
        consumer = session.createConsumer(destination);  
        consumer.setMessageListener(new TopicListener());  
    }
  
    final class TopicListener implements MessageListener {  
  
        public void onMessage(Message message) {  
 
            try {  
            	
                System.out.println(((TextMessage) message).getText());  
            } catch (JMSException e) {  
                LOGGER.info("error {}", e);
            }  
        }  
    }
    
	public static void main(String[] args) throws JMSException {

		new Subscribe();
	}
}