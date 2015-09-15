package AMQP;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;

public class RPCRequester2 {

	private String message, params, from, to, type, target;
	private Server server;
	private Channel channel;
	private String replyQueueName;
	
	
	public RPCRequester2(String message, String params, String from, String to, String type, String target, Server server, Channel channel){
		this.message=message;
		this.params=params;
		this.from=from;
		this.to=to;
		this.type=type;
		this.target=target;
		this.server=server;
		this.channel=channel;
	}
	
	
	public String request() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//String correlId= java.util.UUID.randomUUID().toString();
		
		//replyQueueName = channel.queueDeclare().getQueue();
		String correlId=channel.queueDeclare().getQueue();
		//channel.queueDeclare(correlId, false, true, true, null);
//		channel.queueBind(replyQueueName, target+"."+server.SERVER_NAME, correlId);
//		channel.basicConsume(replyQueueName, false, consumer);
		//channel.queueBind(correlId, target+"."+server.SERVER_NAME, correlId);
		
		channel.basicConsume(correlId, false, consumer);
		HashMap fullMessage = new HashMap();
		
		BasicProperties properties = new BasicProperties.Builder().
				correlationId(correlId).type(type).replyTo(server.trustedClouds.get(target)[2])
				.build();
		fullMessage.put("sender", from+"@"+server.SERVER_NAME);
		fullMessage.put("receiver", to);
		fullMessage.put("type", type);
		fullMessage.put("message", message);
		fullMessage.put("params", params);
		//channel.basicPublish(server.SERVER_NAME+"."+target, "request", properties, SerializationUtils.serialize(fullMessage));
//		System.out.println("Message: '"+ message+ "' sent from '" + server.SERVER_NAME +"' to '"+ target +"' over exchange: "+server.SERVER_NAME+"."+target);

//		channel.basicPublish(server.SERVER_NAME+"."+target, "request", properties, SerializationUtils.serialize(fullMessage));
//		channel.basicPublish(server.SERVER_NAME+"."+target, "request", properties, SerializationUtils.serialize(fullMessage));
		channel.basicPublish("", correlId, null, SerializationUtils.serialize(fullMessage));
//		while (true) {
//			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
////				        if (delivery.getProperties().getCorrelationId().equals(correlId)) {
//	        	fullMessage=(HashMap)SerializationUtils.deserialize(delivery.getBody());
//	        	channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
////	        	server.sendUserResponse(from, message);
//	        	channel.queueUnbind(correlId, target+"."+server.SERVER_NAME, correlId);
//	        	channel.queueDelete(correlId);
//	        	return (String) fullMessage.get("message");
//	            
////	        }
//		}
		return "";
	}

}