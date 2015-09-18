package AMQP;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import com.rabbitmq.client.ConnectionFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author Fabian Hempel
 * class to start a new server + main logic
 *
 */



public class Server {
	String SERVER_NAME = new String();
	
	Channel channel;
	ExecutorService threadExecutor;
	HashMap<String, String[]> trustedClouds;
	public HashMap<String,User> users;
	public HashMap<String,Device> devices;
	public HashMap<String,Service> serviceList;
	public HashMap<String, String> responses;
	private httpAccess webServer;
	
		
	
	/**
	 *  
	 * Start a new server.
	 * @param name Name of the server.
	 * @throws Exception
	 */
	public Server(String name) throws Exception {
		SERVER_NAME=name;
		trustedClouds = new HashMap<String, String[]>(); //cloud-Hashmap + queues
		users = new HashMap<String,User>();
		devices= new HashMap<String,Device>();
		serviceList = new HashMap<String,Service>();
		webServer= new httpAccess(SERVER_NAME);
		
		ConnectionFactory factory = new ConnectionFactory();
	    
        factory.setHost("localhost");
       
		Connection connection = factory.newConnection();
	    
		channel = connection.createChannel();
		
		channel.exchangeDeclare("localExchange", "topic");
		
		
		threadExecutor = Executors.newFixedThreadPool(10);
		
		

	}
	
	
	/**
	 * Add a trusted cloud, build corresponding exchanges and queues.
	 * @param sName Name of the cloud.
	 * @param ip IP of the cloud.
	 *  
	 */
	@SuppressWarnings("deprecation")
	public void addNewTrustedCloud(String sName, String ip) throws TimeoutException, Exception{
//		if(trustedClouds.size()==0){empty=true;}
		String to= SERVER_NAME+"|"+sName;
		String from= sName+"|"+SERVER_NAME;
		
		if (!this.trustedClouds.containsKey(sName)){
			trustedClouds.put(sName, new String[] {"sr_"+to, "pubsub_"+to, "sr_"+from, "pubsub_"+from});
			
			//createQueue(trustedClouds.get(sName)[2]);
			
			webServer.setServerAccess(sName);
			webServer.setUpstreamExchange(sName, ip);
			
			//exchanges / queues für requests /responds
			channel.queueDeclare(trustedClouds.get(sName)[0], true, false, false, null);
			channel.queueDeclare(trustedClouds.get(sName)[2], true, false, false, null);
			channel.exchangeDeclare(SERVER_NAME+"."+sName, "direct");
			channel.exchangeDeclare(sName+"."+SERVER_NAME, "direct");
			channel.queueBind(trustedClouds.get(sName)[2], sName+"."+SERVER_NAME, "request");
			
			
			
			channel.exchangeDeclare("pub"+sName+"."+SERVER_NAME, "topic");
			channel.exchangeDeclare("pub"+SERVER_NAME+"."+sName, "topic");
			channel.exchangeBind("pub"+SERVER_NAME+"."+sName, "localExchange", "#."+sName);
			
			
			createWorker(threadExecutor, channel, trustedClouds.get(sName)[2]);
			System.out.println(sName+ " zu den TrustedClouds hinzugefügt.");
//				createConsumer(trustedClouds.get(address)[3]);
//				createProducer(trustedClouds.get(address)[0], trustedClouds.get(address)[1]);
						
			
				
		}
		else {System.out.println(sName+ " ist bereits in den TrustedClouds.");}
	//	System.out.println("Alle TrustedClouds: ");
		
			
	}
	
	
	
	/**
	 * Create a new Requester for Request/Respond.
	 * @param message type of the Request.
	 * @param params optional parameter, e.g. the name of the device for an getDeviceData-Request.
	 * @param from sender of the message.
	 * @param to receiver of the message.
	 * @param type type of the message to put in queue, mostly "request".
	 * @param target queue to put the message in.
	 * @return returns the 
	 * @throws String with the respond.
	 */
	String sendRequest(String message, String params, String from, String to, String type, String target) throws Exception{
		RPCRequester newRequest = new RPCRequester(message, params, from, to, type, target, this, channel);
		String response = newRequest.request();
		return response;
		
	}
	
	
	
	
	
	
	
	
	/**
	 * Sends the respond to a related request.
	 * @param message content of the response.
	 * @param params optional parameters for the response.
	 * @param from sender of the response.
	 * @param to receiver of the response.
	 * @param type type of the message, in this case "response"
	 * @param correlId correlated ID to assign the response to the right request.
	 * @param target queue to put the response in.
	 * @throws IOException
	 */
	void sendResponse(String message, String params, String from, String to, String type, String correlId, String target) throws IOException{
		HashMap fullMessage = new HashMap();
		BasicProperties properties = new BasicProperties.Builder()
				.messageId((java.util.UUID.randomUUID().toString())).correlationId(correlId).type(type)
				.build();
		fullMessage.put("sender", from+"@"+SERVER_NAME);
		fullMessage.put("receiver", to);
		fullMessage.put("type", type);
		fullMessage.put("message", message);
		fullMessage.put("params", params);
//		channel.queueDeclare(correlId, false, false, false, null);
//		System.out.println(target);
//		channel.queueBind( correlId, SERVER_NAME+"."+target, "");
//		System.out.println("Message: '"+ message+" "+ params+ "' sent from '" + SERVER_NAME +"' to '"+ target +"' over Exchange: "+SERVER_NAME+"."+target+ " queue '"+ correlId);
//		channel.basicPublish(SERVER_NAME+"."+target, correlId, properties, SerializationUtils.serialize(fullMessage));
		System.out.println("Message: '"+ message+" "+ params+ "' sent from '" + fullMessage.get("sender") +"' to '"+ target +"' over Exchange: "+SERVER_NAME+"."+target);
		channel.basicPublish(SERVER_NAME+"."+target, correlId, properties, SerializationUtils.serialize(fullMessage));
		
		
	}
	
	
	/**
	 * Sends a publish message.
	 * @param message content of the message.
	 * @param params optional parameters.
	 * @param from sender of the publish message.
	 * @param to receiver of the publish message.
	 * @param type type of the message, in this case "publish"
	 * @param target queue to put the message in.
	 * @throws IOException
	 */
	void sendPublish(String message, String params, String from, String to, String type,  String target) throws IOException{
		HashMap fullMessage = new HashMap();
		BasicProperties properties = new BasicProperties.Builder()
				.messageId(java.util.UUID.randomUUID().toString()).type(type)
				.build();
		fullMessage.put("sender", from+"@"+SERVER_NAME);
		fullMessage.put("receiver", to);
		fullMessage.put("type", type);
		fullMessage.put("message", message);
		fullMessage.put("params", params);
		
		System.out.println("Message: '"+ message+" "+ params+ "' sent from '" + from+"@"+SERVER_NAME +"' to '"+ to +"' over queue '"+ trustedClouds.get(target)[1]);
		channel.basicPublish("",  trustedClouds.get(target)[1], properties, SerializationUtils.serialize(fullMessage));
	}
	
	

	/**
	 * Creates a consumer to receive message at the corresponding queue.
	 * @param threadExecutor threadexecutor that handles the worker.
	 * @param chan channel where the queue is.
	 * @param queue name of the queue to observe.
	 * @throws Exception
	 */
	void createWorker(ExecutorService threadExecutor, Channel chan, String queue) throws Exception{
	new Worker(this, threadExecutor, chan, queue);
	}
	
	/**
	 * Create a local service and put it in the service list.
	 * @param serviceName name of the service.
	 * @throws IOException
	 */
	public void addService(String serviceName) throws IOException{
		serviceList.put(serviceName,new Service(this, serviceName));
	}
	
	/**
	 * Returns a local service by name.
	 * @param serviceName name of the service.
	 * @return Service instance
	 * @throws NullPointerException
	 */
	public Service getLocalService(String serviceName) throws NullPointerException{
		return serviceList.get(serviceName);
	}
	
	/**
	 * Get all services, locals and from trusted clouds.
	 * @return String with all available services.
	 * @throws Exception
	 */
	public String getAllServices() throws Exception{
		ArrayList<String> allServices = getServices();
		for(Map.Entry<String, String[]> entry : trustedClouds.entrySet()){ 
			allServices.add(sendRequest("getServices", "",  SERVER_NAME, "ziel", "request", entry.getKey()));
		}
		return allServices.toString();
	};
	
	
	
	
	/**
	 * Get all local services in a list.
	 * @return ArrayList<String> of services
	 */
	public ArrayList<String> getServices(){
		ArrayList<String> helplist = new ArrayList<String>();
		for(Map.Entry<String, Service> entry : serviceList.entrySet()){ 
			helplist.add(entry.getKey()+"@"+SERVER_NAME);
		}
		return helplist;
	}
	
	/**
	 * Get the data of a device.
	 * @param device targeted device, has to be like "name#dataid@targetserver".
	 * @param requester origin of the request, has to be like "requestername@requesterserver".
	 * @return String of the data.
	 * @throws Exception
	 */
	String getDeviceData (String device, String requester) throws Exception{
		String[] dev_Svr = device.split("@");
		String[] dev_Data = dev_Svr[0].split("#");
		String[] split = requester.split("@");
		if (dev_Svr[1].equals(SERVER_NAME)){
		
			try{
				if (devices.containsKey(dev_Data[0])) {
					if (devices.get(dev_Data[0]).hasAccess(requester, Integer.valueOf(dev_Data[1]))){;
					return devices.get(dev_Data[0]).getData(Integer.valueOf(dev_Data[1]));}
					else return "Keine Berechtigung für die Daten";
				}
				else return "Device nicht vorhanden";
			}
			catch (NullPointerException e){ System.out.println("Nullpointer in serviceAccess");
				 }	 
		}	
		else {
				return sendRequest("getDeviceData", device, split[0], "to", "request", dev_Svr[1]);
				
			}
		return "nix";	
	}
	
	
	/**
	 * Subscribe device data.
	 * @param device targeted device, has to be like "name#dataid@targetserver".
	 * @param requester origin of the request, has to be like "requestername@requesterserver".
	 * @return String with the respond to the subscribe request.
	 * @throws Exception
	 */
	public String subscribeDeviceData (String device, String requester) throws Exception{
		String[] dev_Svr = device.split("@");
		String[] dev_Data = dev_Svr[0].split("#");
		String[] split = requester.split("@");
		if (dev_Svr[1].equals(SERVER_NAME)){
		
			try{
				if (devices.containsKey(dev_Data[0])) {
					if (devices.get(dev_Data[0]).setServiceSubscribe(requester, Integer.valueOf(dev_Data[1]))){
						return "Subscribe erfolgreich";
					}
					else return "Keine Berechtigung für die Daten";
				}
				else return "Device nicht vorhanden";
			}
			catch (NullPointerException e){ System.out.println("Nullpointer in serviceAccess");
				 }	 
		}	
		else {
				return sendRequest("subscribeDeviceData", device, split[0], "to", "request", dev_Svr[1]);
				
			}
		return "nix";
	}
	
	
	/**
	 * Unsubscribe device data.
	 * @param device targeted device, has to be like "name#dataid@targetserver".
	 * @param requester origin of the request, has to be like "requestername@requesterserver".
	 * @return String with the respond to the subscribe request.
	 * @throws Exception
	 */
	public String unsubscribeDeviceData(String device, String requester) throws Exception{
		String[] dev_Svr = device.split("@");
		String[] dev_Data = dev_Svr[0].split("#");
		String[] split = requester.split("@");
		
		if (dev_Svr[1].equals(SERVER_NAME)){
			try{
				if (devices.containsKey(dev_Data[0])) {
					if (devices.get(dev_Data[0]).setServiceUnsubscribe(requester, Integer.valueOf(dev_Data[1]))){
							return "Unsubscribe erfolgreich";
					}
					else return "kein Subscribe vorhanden";
				}
				else return "Device nicht vorhanden";
			}
			catch (NullPointerException e){ System.out.println("Nullpointer in serviceAccess");
				 }	 
		}	
		else {
				String response=sendRequest("unsubscribeDeviceData", device, split[0], "to", "request", dev_Svr[1]);
				return response;
			}
		return "nix";
	}
	
	/**
	 * Set service access to on or more device datas.
	 * @param user user of the device.
	 * @param service servicename to allow access.
	 * @param dataIndex one ore more data indexes.
	 */
	public void addServiceAccess(User user, String service, int ...dataIndex){
		for (int j : dataIndex){
			user.getDevice().setServiceAccess(service, j);
		}
	}
	
	/**
	 * Remove the access to on or more device datas.
	 * @param user owner of the device.
	 * @param service servicename to allow access.
	 * @param dataIndex one ore more data indexes.
	 */
	public void removeServiceAccess(User user, String service, int ...dataIndex){
		
		for (int j : dataIndex){
				users.get(user).getDevice().removeServiceAccess(service, j);
		}
	}
	
	/**
	 * Get a HashMap of all services that currently subscribe device data.
	 * @param user owner of the device.
	 * @return HashMap with data index and current subscribers.
	 */
	protected HashMap<String, String> discoverFollowers(User user){
		return user.getDevice().listFollower();
	}
	
	
	
}
