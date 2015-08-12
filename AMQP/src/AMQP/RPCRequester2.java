package AMQP;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class RPCRequester2 implements Callable{

	private String message, params, from, to, type, target;
	private Server server;
	private Channel channel;
	
	
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
	
	@Override
	public Object call() throws Exception {
		HashMap fullMessage = new HashMap();
		String correlId= java.util.UUID.randomUUID().toString();
		BasicProperties properties = new BasicProperties.Builder().
				correlationId(correlId).type(type).replyTo(server.trustedClouds.get(target)[2])
				.build();
		fullMessage.put("sender", from+"@"+server);

		System.out.println(from+"@"+server.SERVER_NAME);
		fullMessage.put("receiver", to);
		fullMessage.put("type", type);
		fullMessage.put("message", message);
		fullMessage.put("params", params);
//		channel.queueDeclare(correlId, false, false, false, null);
//		
//		channel.queueBind(correlId, target+"."+server.SERVER_NAME, "");
//		channel.basicPublish("", server.trustedClouds.get(target)[0], properties, SerializationUtils.serialize(fullMessage));
//		System.out.println("Message: '"+ message+ "' sent from '" + server.SERVER_NAME +"' to '"+ target +"' over queue '" + server.trustedClouds.get(target)[0]);
//		QueueingConsumer consumer = new QueueingConsumer(channel);
//		channel.basicConsume(correlId, false, consumer);
		channel.queueDeclare("test", false, false, false, null);
		
		channel.queueBind("test", target+"."+server.SERVER_NAME, "");
		channel.basicPublish("", server.trustedClouds.get(target)[0], properties, SerializationUtils.serialize(fullMessage));
		System.out.println("Message: '"+ message+ "' sent from '" + server.SERVER_NAME +"' to '"+ target +"' over queue '" + server.trustedClouds.get(target)[0]);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume("test", false, consumer);
		while (true) {
	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//	        if (delivery.getProperties().getCorrelationId().equals(correlId)) {
	        	fullMessage=(HashMap)SerializationUtils.deserialize(delivery.getBody());
	        	channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//	        	server.sendUserResponse(from, message);
	        	System.out.println(message);
	        	return message;
	            
//	        }
		}
		
	}

}
