package AMQP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;


public class Help extends DefaultConsumer {

    String name;
    long sleep;
    Channel channel;
    String queue;
    int processed;
    ExecutorService executorService;
    Server server;

    public Help(Server server, ExecutorService threadExecutor,
                   Channel c) throws Exception {
        super(c);
        this.server= server;
        channel = c;
        channel.queueDeclare("abfangqueue", false, false, false, null);
        channel.queueBind( "abfangqueue","pubServerB.ServerA", "#");
        channel.basicConsume("abfangqueue", false, this);
        executorService = threadExecutor;
        
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
    //    Runnable task = new MessageHandler(this, channel, body);
    	try {

			channel.basicAck(envelope.getDeliveryTag(), false);

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}
    	System.out.println("abgefangen");
    	HashMap map = (HashMap)SerializationUtils.deserialize(body);
	    System.out.println("Nachricht: "+ map.get("message")+ " von " + map.get("sender")+ " erhalten, Sender:"+ map.get("sender")+"to"+envelope.getRoutingKey());
    }
}