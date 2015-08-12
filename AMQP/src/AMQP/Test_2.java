package AMQP;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class Test_2 {
	
		
	
	public static void main(String[] argv) throws TimeoutException, Exception{
		int kurz=1000;
		int lang=500;
		
		//setup Server(clients)
		Server serverA = new Server("ServerA");
		serverA.addNewTrustedCloud("ServerB","1");
		
		
		Server serverB = new Server("ServerB");
		serverB.addNewTrustedCloud("ServerA","1");
		
		
		
		
		//setup Services
		serverA.addService("Stromking1");
		serverA.addService("Stromking2");
		serverA.addService("Stromking3");
		
		serverB.addService("Wasserking1");
		serverB.addService("Wasserking2");
		serverB.addService("Wasserking3");
		
		
		
		//setup	Users+Devices	
		serverA.devices.put("device", new Device(serverA, "device", 1,2,3,4,5));
		serverA.users.put("Alice", new User("Alice", "user1", serverA.devices.get("device")));
		
		serverB.devices.put("device", new Device(serverB, "device", 6,7,8,9,10));
		serverB.users.put("Bob", new User("Bob", "user2", serverB.devices.get("device")));
		
	
		
		/**
		 * hier gehts los
		 */
		//Bob vergibt Zugriffsrechte	
		serverB.addServiceAccess(serverB.users.get("Bob"), "Stromking1@ServerA", 0);
		serverB.addServiceAccess(serverB.users.get("Bob"), "Stromking2@ServerC", 0);
		Thread.sleep(lang);
		
		System.out.println("Alle Services von Server A aus abrufen: " + serverA.getAllServices()+"\n");
		Thread.sleep(lang);
		//Request Response
		String requestB = "device#0@ServerB";
		System.out.println("Stromking1@ServerA fragt einmalig Datum von " + requestB +" von Bob ab. Antwort:" + serverA.getDeviceData(requestB, "Stromking1@ServerA")+"\n");
		Thread.sleep(lang);
		
		//Service von a und c subscriben bei Bob
//		System.out.println("Stromking1@ServerA subscribed "+ requestB + ". Antwort: " + serverA.subscribeDeviceData(requestB, "Stromking1@ServerA"));
		String requestC = "device#0@ServerB";
		System.out.println("Stromking1@ServerA subscribed "+ requestB + ". Antwort: ");
		serverA.serviceList.get(0).subscribe(requestB);
		
		Thread.sleep(lang);
		//Bobs Follower
		System.out.println("Follower von Bob: "+serverB.discoverFollowers(serverB.users.get("Bob"))+"\n");
		Thread.sleep(5000);
		
//		4x update an Bobs daten+publish
		serverB.users.get("Bob").getDevice().updateData(0);
		Thread.sleep(lang);
		serverB.users.get("Bob").getDevice().updateData(0);
		Thread.sleep(lang);
		serverB.users.get("Bob").getDevice().updateData(0);
		Thread.sleep(lang);
		serverB.users.get("Bob").getDevice().updateData(0);
		Thread.sleep(lang);
	
	
	
	
	System.exit(0);
	
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
