package AMQP;

import java.util.concurrent.TimeoutException;

public class Test4 {
	public static void main(String[] argv) throws TimeoutException, Exception{
		int kurz=500;
		int lang=5000;
		
		//setup Server(clients)

		
		
		Server serverB = new Server("ServerB");
		serverB.addNewTrustedCloud("ServerA","192.168.56.1");
		
		
		
		
		
		serverB.addService("Wasserking1");
		serverB.addService("Wasserking2");
		serverB.addService("Wasserking3");
		
		
		
		//setup	Users+Devices	

		
		serverB.devices.put("device", new Device(serverB, "device", 6,7,8,9,10));
		serverB.users.put("Bob", new User("Bob", "user2", serverB.devices.get("device")));
		serverB.addServiceAccess(serverB.users.get("Bob"), "Stromking1@ServerA", 0);
	Thread.sleep(lang);
	System.out.println("Follower von Bob: "+serverB.discoverFollowers(serverB.users.get("Bob"))+"\n");
	serverB.users.get("Bob").getDevice().updateData(0);
	Thread.sleep(kurz);
	serverB.users.get("Bob").getDevice().updateData(0);
	Thread.sleep(kurz);
	serverB.users.get("Bob").getDevice().updateData(0);
	Thread.sleep(kurz);
	serverB.users.get("Bob").getDevice().updateData(0);
	Thread.sleep(lang);
	System.out.println("Follower von Bob: "+serverB.discoverFollowers(serverB.users.get("Bob"))+"\n");
	}
}
