package AMQP;


import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class RPCClient {
	
	private Connection connection;
	private Channel channel;
	private String requestQueueName;
	private String replyQueueName;
	private QueueingConsumer consumer;

	public RPCClient(Channel channel, String queue) throws Exception {
	    
		
	    replyQueueName = channel.queueDeclare().getQueue(); 
	    consumer = new QueueingConsumer(channel);
	    channel.basicConsume(replyQueueName, true, consumer);
	    requestQueueName = queue;
	}

	public String call(byte[] message, BasicProperties props) throws Exception {     
	    String response = null;
	    String corrId = props.getCorrelationId();

	    
	    System.out.println(message+ props.toString()+ requestQueueName + corrId);
	    channel.basicPublish("", requestQueueName, props, message);

	    while (true) {
	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	        if (delivery.getProperties().getCorrelationId().equals(corrId)) {
	            response = new String(delivery.getBody());
	            break;
	        }
	    }

	    return response; 
	}

	public void close() throws Exception {
	    connection.close();
	}
}
