package AMQP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;

import com.rabbitmq.client.Channel;


public class MessageHandler implements Runnable {

    
    
    Channel chan;
    Worker worker;
    AMQP.BasicProperties properties;
    byte[] message;
    long tag;
    Server server;

public MessageHandler(byte[] body, BasicProperties properties, long tag, Server server, Channel channel) {
		
    	message=body;
    	this.tag=tag;
    	chan=channel;
    	this.properties=properties;
    	this.server = server;
	}

//    public MessageHandler(Worker worker2, Channel c, byte[] body) {
//		
//    	worker = worker2;
//        chan = c;
//        message=body;
//	}


	public void run() {
			try {
				chan.basicAck(tag, false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Map map = (HashMap)SerializationUtils.deserialize(message);
            System.out.println("Message erhalten: "+ map.get("message"));
            
            
          //handle type of message
   	     switch (properties.getType()){
   	     case "request": 	
   	    	switch(map.get("message").toString()){
   	    		case "getServices":
   	    							{ ArrayList help = server.getServices();
   	    							String[] split = map.get("sender").toString().split("@");
   	    							try {System.out.println(split[1]);
										server.sendResponse(help.toString(),"", server.SERVER_NAME, map.get("sender").toString(), "response", properties.getCorrelationId(), split[1]);
										
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
   	    							break;}
   	    		
   	    		case "getDeviceData":	
   									try {
   										String[] split = map.get("sender").toString().split("@");
   										server.sendResponse(server.getDeviceData(map.get("params").toString(), map.get("sender").toString()),"", server.SERVER_NAME , map.get("sender").toString(), "response", properties.getCorrelationId(), split[1]);
   									} catch (Exception e) { System.out.println("wtf");
   										// TODO Auto-generated catch block
   										e.printStackTrace();
   									}
   						    		break;
   	    						
   	    		case "subscribeDeviceData":
   					    			
   									try {
   										String[] split = map.get("sender").toString().split("@");
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
   	    					
   	    					
   	    					
   	    					
   	    					
   	    case "response":	//System.out.println("RESPONSE " +map.get("message"));
   	    					server.responses.put(properties.getCorrelationId(), map.get("message").toString());
   	    					break;
   	    case "publish":   	System.out.println("Publish von "+ map.get("sender")+ "an " + map.get("receiver") + " data:"+ map.get("message"));
   	    					break;
   	    case "unsubscribe": 
   	    					break;
   	    case "services"   :
   	    					break;
   	    default:			
   	    					break;
   	    }			
            
    }
}