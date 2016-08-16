package AMQP;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;

/**
 * @author Fabian Hempel
 * Sets up an request and waits for the corresponding respond.
 *
 */
public class RPCRequester {

	private String message, params, from, to, type, target;
	private Server server;
	private Channel channel;
	private String replyQueueName;
	
	
	/**
	 * Set up a new instance.
	 * @param message type of the request.
	 * @param params optional parameters for the request.
	 * @param from sender of the request.
	 * @param to receiver of the request.
	 * @param type type of the message, in this case "request".
	 * @param target queue to put the message in.
	 * @param server local server.
	 * @param channel channel to be used.
	 */
	public RPCRequester(String message, String params, String from, String to, String type, String target, Server server, Channel channel){
		this.message=message;
		this.params=params;
		this.from=from;
		this.to=to;
		this.type=type;
		this.target=target;
		this.server=server;
		this.channel=channel;
	}
	
	/**
	 * Send the request and wait for the response.
	 * @return String with the response.
	 * @throws IOException
	 * @throws ShutdownSignalException
	 * @throws ConsumerCancelledException
	 * @throws InterruptedException
	 */
	public String request() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		//create a unique queue where the response is send to
		QueueingConsumer consumer = new QueueingConsumer(channel);
		String correlId=channel.queueDeclare().getQueue();
		channel.queueBind(correlId, target+"."+server.SERVER_NAME, correlId);
		channel.basicConsume(correlId, false, consumer);
		
		//create the message body & send the request
		HashMap fullMessage = new HashMap();
		BasicProperties properties = new BasicProperties.Builder().
				correlationId(correlId).type(type).replyTo(server.trustedClouds.get(target)[2])
				.build();
		fullMessage.put("sender", from+"@"+server.SERVER_NAME);
		fullMessage.put("receiver", to);
		fullMessage.put("type", type);
		fullMessage.put("message", message);
		fullMessage.put("params", params);
		channel.basicPublish(server.SERVER_NAME+"."+target, "request", properties, SerializationUtils.serialize(fullMessage));

		//wait for the response and return it, delete the queues
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			        
	        	fullMessage=(HashMap)SerializationUtils.deserialize(delivery.getBody());
	        	channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        	
	        	channel.queueUnbind(correlId, target+"."+server.SERVER_NAME, correlId);
	        	channel.queueDelete(correlId);
	        	return (String) fullMessage.get("message");
	            

		}
		
	}

}
