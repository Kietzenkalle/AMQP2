package AMQP;


import java.io.IOException;
import java.util.HashMap;

import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @author Fabian Hempel
 * This class handles the creation of needed queues for an service to receive subscribed data.
 * Connects to the local exchange and to others if needed.
 *
 */
public class ServiceMessageHandler extends DefaultConsumer {
	
	private String name;
	private Server server;
	
	/**
	 * Sets up the service message handler. Binds a queue to the local exchange.
	 * @param name name of the service.
	 * @param server name of the server.
	 * @throws IOException
	 */
	public ServiceMessageHandler(String name, Server server) throws IOException {
		super(server.channel);
		this.name=name;
		server.channel.queueDeclare(name, false, false, false, null);
		server.channel.queueBind(name, "localExchange", name+"."+server.SERVER_NAME);
		this.server=server;
		server.channel.basicConsume(name, false, this);
	}

	/**
	 * Connects the queue to a publish/subscribe exchange.
	 * @param targetExchange name of the exchange.
	 * @throws IOException
	 */
	public void addQueueBind(String targetExchange) throws IOException{
		server.channel.queueBind(name, targetExchange, name+"."+server.SERVER_NAME);
		//System.out.println(name+"@"+server.SERVER_NAME+" connected to "+ targetExchange);
		
	}
	
	
	/**
	 * Prints out the message if a publish-message is received.
	 */
	 @Override
	     public void handleDelivery(String consumerTag,
	                               Envelope envelope,
	                               AMQP.BasicProperties properties,
	                               byte[] body) throws IOException {
		 
		 try {
			 	server.channel.basicAck(envelope.getDeliveryTag(), false);
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
	    HashMap map = (HashMap)SerializationUtils.deserialize(body);
	    System.out.println(name +" hat Nachricht: "+ map.get("message") + " erhalten");
	    
	    }
	 
}
