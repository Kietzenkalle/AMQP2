package AMQP;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


/**
 * @author Fabian Hempel
 * Class that contains a Consumer to empty a given queue.
 *
 */
public class Worker extends DefaultConsumer {

    String name;
    long sleep;
    Channel channel;
    String queue;
    int processed;
    ExecutorService executorService;
    Server server;

    
    /**
     * Creates a Consumer.
     * @param server name of the server.
     * @param threadExecutor threadpool to handle the workers.
     * @param c channel of the queue.
     * @param q queue name.
     * @throws Exception
     */
    public Worker(Server server, ExecutorService threadExecutor,
                   Channel c, String q) throws Exception {
        super(c);
        this.server= server;
        channel = c;
        queue = q;
       
        channel.basicConsume(queue, false, this);
        executorService = threadExecutor;
        
    }

    /**
     * creates a new MessageHandler when a message is received
     */
    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {

    	new MessageHandler(body, properties, envelope.getDeliveryTag(), server, channel);
    }
}