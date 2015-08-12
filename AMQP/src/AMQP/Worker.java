package AMQP;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Worker extends DefaultConsumer {

    String name;
    long sleep;
    Channel channel;
    String queue;
    int processed;
    ExecutorService executorService;
    Server server;

    public Worker(Server server, ExecutorService threadExecutor,
                   Channel c, String q) throws Exception {
        super(c);
        this.server= server;
        channel = c;
        queue = q;
       
        channel.basicConsume(queue, false, this);
        executorService = threadExecutor;
        
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
    //    Runnable task = new MessageHandler(this, channel, body);
    	Runnable task = new MessageHandler(body, properties, envelope.getDeliveryTag(), server, channel);
    	executorService.submit(task);
    }
}