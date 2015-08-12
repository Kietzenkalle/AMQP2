package AMQP;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class Test2 {
	
		
	
	public static void main(String[] argv) throws TimeoutException, Exception{
		int kurz=1000;
		int lang=500;
		
		//setup Server(clients)

		
		
		Server serverB = new Server("ServerB");
		serverB.addNewTrustedCloud("ServerA");
		
		
		
		
		
		serverB.addService("Wasserking1");
		serverB.addService("Wasserking2");
		serverB.addService("Wasserking3");
		
		
		
		//setup	Users+Devices	

		
		serverB.devices.put("device", new Device(serverB, "device", 6,7,8,9,10));
		serverB.users.put("Bob", new User("Bob", "user2", serverB.devices.get("device")));
		
	
		
		/**
		 * hier gehts los
		 */
		//Bob vergibt Zugriffsrechte	
		serverB.addServiceAccess(serverB.users.get("Bob"), "Stromking1@ServerA", 0);
		serverB.addServiceAccess(serverB.users.get("Bob"), "Stromking2@ServerC", 0);
		Thread.sleep(lang);
		

		
		Thread.sleep(lang);
		//Bobs Follower
		System.out.println("Follower von Bob: "+serverB.discoverFollowers(serverB.users.get("Bob"))+"\n");
		Thread.sleep(5000);
		


	
	}
		
	
		
			
			
	
	
		
//		String request = "device@ServerB";
//		System.out.println("Request data from "+ request + ": " + serverB.getDeviceData(request, "Stromking1@ServerA"));
//		
//		System.out.println("Subscribe data from "+ request + ": " + serverB.subscribeDeviceData(request, "Stromking1@ServerA"));

		
		
//		Thread.sleep(5000);
		//		
//		
//		System.out.println("Alle Services: " + serverB.getAllServices());
		
		//("getDeviceData", "Bob@ServerB", serverB.users.get("Bob").getName(), "Alice", "request", "ServerA");
		
		/**
		 * Zugriffsberechtigungen
		 */
//		System.out.println(serverB.serviceAccess.get(serverB.devices.get("device")).get("Stromking1@ServerA").get(0)[0]);
//		System.out.println(serverB.serviceAccess.get(serverB.devices.get("device")).get("Stromking2@ServerA").get(0)[0]);
//
//		serverB.removeServiceAccess(serverB.users.get("Bob"), "Stromking2@ServerA");
//		
//		System.out.println(serverB.serviceAccess.get(serverB.devices.get("device")).get("Stromking1@ServerA").get(0)[0]);
//		System.out.println(serverB.serviceAccess.get(serverB.devices.get("device")).get("Stromking2@ServerA").get(0)[0]);
//
//		String request = "device@ServerB";
//		System.out.println("Request data from "+ request + ": " + serverB.getDeviceData(request,"Stromking1@ServerA"));
//	
		
	
}
