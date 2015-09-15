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
	
	private String exchange="";
	
	
	
	/**
	 * Constructor
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
		//new Help(this,threadExecutor, channel);
		

	}
	
	
	/**
	 * Add a Trusted Cloud, build corresponding queues
	 * @return 
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
	 * Sendet Nachricht an angegebene Queue
	 * @throws IOException 
	 */
	void sendMessage(String message, String target) throws IOException{
		HashMap fullMessage = new HashMap();
		BasicProperties properties = new BasicProperties.Builder()
				.messageId(java.util.UUID.randomUUID().toString())
				.build();
//		fullMessage.put("sender", from+"@"+SERVER_NAME);
//		fullMessage.put("receiver", to);
//		fullMessage.put("type", type);
		fullMessage.put("message", message);
//		fullMessage.put("params", params);
		System.out.println(fullMessage.get("message"));
//		channel.basicPublish("", queue, properties, SerializationUtils.serialize(fullMessage));
		channel.basicPublish(SERVER_NAME+"."+target, "response", properties, SerializationUtils.serialize(fullMessage));
	}
	
	
	
	
	
	
	
	
//	/**
//	 * Sendet Nachricht an angegebene Queue
//	 * @throws IOException 
//	 */
//	void sendMessage(String message, String params, String queue, String from, String to, String target) throws IOException{
//		HashMap fullMessage = new HashMap();
//		BasicProperties properties = new BasicProperties.Builder()
//				.messageId(java.util.UUID.randomUUID().toString())
//				.build();
////		fullMessage.put("sender", from+"@"+SERVER_NAME);
////		fullMessage.put("receiver", to);
////		fullMessage.put("type", type);
//		fullMessage.put("message", message);
////		fullMessage.put("params", params);
//		System.out.println(fullMessage.get("message"));
////		channel.basicPublish("", queue, properties, SerializationUtils.serialize(fullMessage));
//		channel.basicPublish(SERVER_NAME+"."+target, "response", properties, SerializationUtils.serialize(fullMessage));
//	}
//	/**
//	 * Send Testmessage
//	 * @throws IOException 
//	 */
//	void sendMessage(String message) throws IOException{
//		HashMap fullMessage = new HashMap();
//		BasicProperties properties = new BasicProperties.Builder()
//				.messageId(java.util.UUID.randomUUID().toString())
//				.build();
////		fullMessage.put("sender", from+"@"+SERVER_NAME);
////		fullMessage.put("receiver", to);
////		fullMessage.put("type", type);
//		fullMessage.put("message", message);
////		fullMessage.put("params", params);
//		System.out.println(fullMessage.get("message"));
//		channel.basicPublish("", queue, properties, SerializationUtils.serialize(fullMessage));
//	}
	
//	/**
//	 * Send a Request to the "target"-Cloud
//	 * @throws Exception 
//	 */
//	String sendRequest1(String message, String params, String from, String to, String type, String target) throws Exception{
//		HashMap fullMessage = new HashMap();
//		String correlId= java.util.UUID.randomUUID().toString();
//		BasicProperties properties = new BasicProperties.Builder().
//				correlationId(correlId).type(type).replyTo(trustedClouds.get(target)[2])
//				.build();
//		fullMessage.put("sender", from+"@"+SERVER_NAME);
//		fullMessage.put("receiver", to);
//		fullMessage.put("type", type);
//		fullMessage.put("message", message);
//		fullMessage.put("params", params);
//		channel.queueDeclare(correlId, false, false, false, null);
//		channel.queueBind(correlId, target+"."+SERVER_NAME, "");
//		
//		channel.basicPublish("", trustedClouds.get(target)[0], properties, SerializationUtils.serialize(fullMessage));
//		System.out.println("Message: '"+ message+ "' sent from '" + SERVER_NAME +"' to '"+ target +"' over queue '" + trustedClouds.get(target)[0]);
//		QueueingConsumer consumer = new QueueingConsumer(channel);
//		channel.basicConsume(correlId, false, consumer);
//		System.out.println("Listening at Exchange: "+ target+"."+SERVER_NAME +" queue: "+correlId );
//		
//		while (true) {
//	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//	        if (delivery.getProperties().getCorrelationId().equals(correlId)) {
//	        	fullMessage=(HashMap)SerializationUtils.deserialize(delivery.getBody());
//	        	channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//	            return (String) fullMessage.get("message");
//	        }
//		}
//	}
	
	/**
	 * Send a Request to the "target"-Cloud
	 * @throws Exception 
	 */
	//rpc über thread
	/*
	String sendRequest1(String message, String params, String from, String to, String type, String target) throws Exception{
//		System.out.println(message+" "+ params+ " "+from+" "+to+" "+type+" "+ target+ " "+ this + " " + channel);
		RPCRequester newRequest = new RPCRequester(message, params, from, to, type, target, this, channel);
		Future<String> response = threadExecutor.submit(newRequest);
		try{
			return response.get(3, TimeUnit.SECONDS);
		} catch(TimeoutException e){
			response.cancel(true);
			return "TimeOut!";
		}
		
	}*/
	//rpc über normale klasse
	String sendRequest(String message, String params, String from, String to, String type, String target) throws Exception{
//		System.out.println(message+" "+ params+ " "+from+" "+to+" "+type+" "+ target+ " "+ this + " " + channel);
		RPCRequester2 newRequest = new RPCRequester2(message, params, from, to, type, target, this, channel);
		String response = newRequest.request();
		return response;
		
	}
	
	
	
	
	
	
	
	
	
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
	 * Send Publish
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
	 * erstellt Queue
	 * @throws Exception 
	 */
	void createQueue(String queueName) throws Exception{
		System.out.println(queueName);
		channel.queueDeclare(queueName, true, false, false, null);
		createWorker(threadExecutor, channel, queueName);
		
	}
	/**
	 * erstellt Queue
	 * @throws Exception 
	 * @throws IOException 
	 */
	void createWorker(ExecutorService threadExecutor, Channel chan, String queue) throws Exception{
	new Worker(this, threadExecutor, chan, queue);
	}
	
	/**
	 * Add local Service
	 * @throws IOException 
	 */
	public void addService(String serviceName) throws IOException{
		serviceList.put(serviceName,new Service(this, serviceName));
	}
	
	/**
	 * Add local Service
	 * @throws IOException 
	 */
	public Service getLocalService(String serviceName) throws NullPointerException{
		return serviceList.get(serviceName);
	}
	
	/**
	 * Request Services from all trusted Clouds
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
	 * Local Services
	 */
	public ArrayList<String> getServices(){
		ArrayList<String> helplist = new ArrayList<String>();
		for(Map.Entry<String, Service> entry : serviceList.entrySet()){ 
			helplist.add(entry.getKey()+"@"+SERVER_NAME);
		}
		return helplist;
	}
	
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
	 * subscribe Data
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
	 * unsubscribe Data
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
	 * Zugriffsberechtigung setzen
	 */
	public void addServiceAccess(User user, String service, int ...dataIndex){
		for (int j : dataIndex){
			user.getDevice().setServiceAccess(service, j);
		}
	}
	
	/**
	 * Zugriffsberechtigungen entziehen
	 */
	public void removeServiceAccess(User user, String service, int ...dataIndex){
		
		for (int j : dataIndex){
				users.get(user).getDevice().removeServiceAccess(service, j);
		}
	}
	
	/**
	 * Get followers
	 */
	protected HashMap<String, String> discoverFollowers(User user){
		return user.getDevice().listFollower();
	}
	
	/**
	 * send user response 
	 */
	protected void sendUserResponse(String user, String message){
	}
	
}
