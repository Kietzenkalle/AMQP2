package AMQP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;


/**
 * @author Fabian Hempel
 * Handles all requests.
 *
 */

public class MessageHandler{

    Channel chan;
    Worker worker;
    AMQP.BasicProperties properties;
    byte[] message;
    long tag;
    Server server;
    String[] split;

        
/**
 * Sets up a new MessageHandler.
 * @param body unserialised messagebody.
 * @param properties AMQP-Properties of the message.
 * @param tag tag of the message to acknowledge its consumption.
 * @param server name of the local server.
 * @param channel corresponding channel.
 */
public MessageHandler(byte[] body, BasicProperties properties, long tag, Server server, Channel channel) {

		

    	message=body;
    	this.tag=tag;
    	chan=channel;
    	this.properties=properties;
    	this.server = server;


			try {
				//acknowledge the channel that the message was received.
				chan.basicAck(tag, false);

			} catch (IOException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			//deserialize the messagebody
            HashMap map = (HashMap)SerializationUtils.deserialize(message);
            split = map.get("sender").toString().split("@");

			

            

          //handle type of message

   	     switch (properties.getType()){

   	     case "request": 	
   	    	switch(map.get("message").toString()){
   	    		case "getServices":
   	    							{ ArrayList help = server.getServices();
				
   	    							try {

										server.sendResponse(help.toString(),"", server.SERVER_NAME, map.get("sender").toString(), "response", properties.getCorrelationId(), split[1]);

									} catch (IOException e) {

										// TODO Auto-generated catch block

										e.printStackTrace();

									}

   	    							break;}
   	    							
   	    		case "getDeviceData":	

   									try {
   										server.sendResponse(server.getDeviceData(map.get("params").toString(), map.get("sender").toString()),"", server.SERVER_NAME , map.get("sender").toString(), "response", properties.getCorrelationId(), split[1]);

   									} catch (Exception e) { System.out.println("wtf");

   										// TODO Auto-generated catch block

   										e.printStackTrace();

   									}

   						    		break;

   	    						

   	    		case "subscribeDeviceData":

   					    			

   									try {

   										server.sendResponse(server.subscribeDeviceData(map.get("params").toString(), map.get("sender").toString()),"", server.SERVER_NAME , map.get("sender").toString(), "response", properties.getCorrelationId(), split[1]);

   									} catch (Exception e) {

   										// TODO Auto-generated catch block

   										e.printStackTrace();

   									}

   									break;

   									

   	    		case "unsubscribeDeviceData":

   					    			

   									try {

   										String[] split = map.get("sender").toString().split("@");

   										server.sendResponse(server.unsubscribeDeviceData(map.get("params").toString(), map.get("sender").toString()),"", server.SERVER_NAME , map.get("sender").toString(), "response", properties.getCorrelationId(), split[1]);

   									} catch (Exception e) {

   										// TODO Auto-generated catch block

   										e.printStackTrace();

   									}

   									break;

   	    							

   	    		default: break;

   	    	}		

  
   	    default:			

   	    					break;

   	    }			

            

    }

}