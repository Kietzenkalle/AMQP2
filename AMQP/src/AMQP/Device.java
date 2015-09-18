package AMQP;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * @author Fabian Hempel
 * set up a new device, contains all data, access lists
 *
 */

public class Device {
		private String name, id;
		private int[] dataalt;
		HashMap<Integer,ArrayList<Object>> data;
		private Server server;
	
	
		/**
		 * Creates a new Device.
		 * @param server name of the local server.
		 * @param name name of the device.
		 * @param datas one ore more datas.
		 */
	public Device(Server server, String name, double ...datas){
		this.name=name;
		id= "ID";
		this.server=server;
		data= new HashMap<Integer,ArrayList<Object>>();
		for (double i : datas){
			this.addData(i);
		}
	}
	
	/**
	 * Add a new data row.
	 * @param singleData data value.
	 */
	protected void addData(double singleData){
		int help=data.size();
		data.put(help, new ArrayList<Object>());
		data.get(help).add(singleData);
		data.get(help).add(new ArrayList<String>());
		data.get(help).add(new ArrayList<String>());
	}
	
	/**
	 * Adds multiple new data rows.
	 * @param moreData one ore more data values.
	 */
	protected void addData(double ...moreData){
		for (double i : moreData){
			addData(i);
		}
	}
	
	/**
	 * Increase a single data value, publish it if it has subscribers.
	 * @param i index of the data.
	 * @throws IOException
	 */
	protected void updateData(int i) throws IOException{
		double help=(double) data.get(i).get(0);
		data.get(i).set(0, help+1);
		ArrayList<String> al2 = (ArrayList<String>) data.get(i).get(2);
		if (al2.size()>0){ 
			publish(Integer.toString(i), String.valueOf(help+1).toString(), al2);
		}
	}
	
	/**
	 * Update a single data value, publish it if it hast subscribers.
	 * @param i index of the data.
	 * @param j new value.
	 * @throws IOException
	 */
	protected void updateData(int i, double j) throws IOException{
		data.get(i).set(0, j);
		ArrayList<String> al2 = (ArrayList<String>) data.get(i).get(2);
		if (al2.size()>0){ 
			publish(Integer.toString(i), String.valueOf(j).toString(), al2);
		}
	}
	
	/**
	 * Set service access for given service to data at specified index.
	 * @param service name of the service.
	 * @param i index of the data.
	 * @return
	 */
	protected boolean setServiceAccess(String service, int i ){
		ArrayList<String> al = (ArrayList<String>) data.get(i).get(1);
				if (!al.contains(service)) {
					al.add(service);
					data.get(i).set(1, al);
					return true;
				}
				else return false;		
	}
	
	/**
	 * Remove the service access.
	 * @param name name of the service.
	 * @param i index of the data.
	 */
	protected void removeServiceAccess(String name, int i){
			ArrayList<String> al = (ArrayList<String>) data.get(i).get(1);
			ArrayList<String> al2 = (ArrayList<String>) data.get(i).get(2);
			if (al.contains(name)) al.remove(name);
			if (al2.contains(name)) al2.remove(name);
	}
	
	/**
	 * Check if a service has access to the data.
	 * @param name name of the service.
	 * @param i index of the data.
	 * @return Boolean for the access state.
	 */
	protected boolean hasAccess(String name, int i){
		ArrayList<String> al = (ArrayList<String>) data.get(i).get(1);
		if(al.contains(name)) return true;
		else return false;
	}
	
	/**
	 * Sets a subscription.
	 * @param name name of the service
	 * @param i index of the data.
	 * @return Boolean, if subscription was successfull.
	 */
	protected boolean setServiceSubscribe(String name, int i){
			ArrayList<String> al = (ArrayList<String>) data.get(i).get(1);
			ArrayList<String> al2 = (ArrayList<String>) data.get(i).get(2);
			if (al.contains(name)) {
				
				if (!al2.contains(name)){
					al2.add(name);
					data.get(i).set(2, al2);
				}
				return true;
			}
			else return false;
	}
	
	/**
	 * Remove a subscription.
	 * @param name name of the service.
	 * @param i index of the data.
	 * @return Boolean, if the unsubscribe was successfull.
	 */
	protected boolean setServiceUnsubscribe(String name, int i){
		
			ArrayList<String> al2 = (ArrayList<String>) data.get(i).get(2);
			if(al2.contains(name)){
				al2.remove(name);
				return true;
			}
			else return false;
		
	}
	
	/**
	 * List all followers of this device.
	 * @return HashMap with all followers.
	 */
	protected HashMap<String, String> listFollower(){
		HashMap<String, String> follower=new HashMap<String,String>();
		//follower.put("Data", "Follower");
		for(Map.Entry<Integer, ArrayList<Object>> entry : data.entrySet()){ 
			ArrayList<String> al2 = (ArrayList<String>) data.get(entry.getKey()).get(2);
			follower.put(entry.getKey().toString(), al2.toString());
		}
		return follower;
	}
	
	/**
	 * Get data value at given index.
	 * @param i index of the data.
	 * @return String with the data value.
	 */
	public String getData(int i) {
		String total = String.valueOf(data.get(i).get(0));
		return total ;
	}
	
	/**
	 * List all service that have access to a given data index.
	 * @param i index of the data.
	 * @return ArrayList with services.
	 */
	protected ArrayList<String> listAccess(int i){
		return (ArrayList<String>) data.get(i).get(1);
	}
	
	/**
	 * Publish a data change to the local exchange, if there are active subscribers.
	 * @param id index of the data.
	 * @param data data value.
	 * @param subscriber list of subscribers.
	 * @throws IOException
	 */
	protected void publish(String id, String data, ArrayList<String> subscriber) throws IOException{
		for (String one : subscriber){ 
			String[] split = one.split("@");
			HashMap fullMessage = new HashMap();
			BasicProperties properties = new BasicProperties.Builder()
					.messageId(java.util.UUID.randomUUID().toString())
					.build();
			fullMessage.put("sender", name+"@"+server.SERVER_NAME);
			fullMessage.put("message", name+"#"+id+"@"+server.SERVER_NAME+":"+data);
			server.channel.basicPublish("localExchange", split[0]+"."+split[1], properties, SerializationUtils.serialize(fullMessage));
						
		}
	}
	
}
