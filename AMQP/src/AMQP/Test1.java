package AMQP;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class Test1 {
	
		
	
	public static void main(String[] argv) throws TimeoutException, Exception{
		int kurz=1000;
		int lang=5000;
		
		//setup Server(clients)
		Server serverA = new Server("ServerA");
		serverA.addNewTrustedCloud("ServerB","192.168.56.101");
							
		//setup Services
		serverA.addService("Stromking1");
		serverA.addService("Stromking2");
		serverA.addService("Stromking3");
				
		//setup	Users+Devices	
		serverA.devices.put("device", new Device(serverA, "device", 1,2,3,4,5));
		serverA.users.put("Alice", new User("Alice", "user1", serverA.devices.get("device")));
		
		
		/**
		 * hier gehts los
		 */
		//Bob vergibt Zugriffsrechte	
		
		
		
		System.out.println("Alle Services von Server A aus abrufen: " + serverA.getAllServices()+"\n");
		Thread.sleep(lang);
		//Request Response
		String requestB = "device#0@ServerB";
		System.out.println("Stromking1@ServerA fragt einmalig Datum von " + requestB +" von Bob ab. Antwort:" + serverA.getDeviceData(requestB, "Stromking1@ServerA")+"\n");
		Thread.sleep(lang);
		

	
	
	
	
//	System.exit(0);
	
	
		
	
		
			
			
	
	
		
//		String request = "device@ServerB";
//		System.out.println("Request data from "+ request + ": " + serverB.getDeviceData(request, "Stromking1@ServerA"));
//		
//		System.out.println("Subscribe data from "+ request + ": " + serverB.subscribeDeviceData(request, "Stromking1@ServerA"));

		
		
		Thread.sleep(5000);
				
		
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
}
