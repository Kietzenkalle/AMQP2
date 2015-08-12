package AMQP;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class MainTest1 {
	
		
	
	public static void main(String[] argv) throws TimeoutException, Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter IP ServerB");
        String s = br.readLine();
		int kurz=1000;
		int lang=5000;
		
		//setup Server(clients)
		Server serverA = new Server("ServerA");
		serverA.addNewTrustedCloud("ServerB","s");
							
		//setup Services
		serverA.addService("Stromking1");
		serverA.addService("Stromking2");
		serverA.addService("Stromking3");
				
		//setup	Users+Devices	
		serverA.devices.put("device", new Device(serverA, "device", 1,2,3,4,5));
		serverA.users.put("Alice", new User("Alice", "user1", serverA.devices.get("device")));
		
		//Bob vergibt Zugriffsrechte
		serverA.addServiceAccess(serverA.users.get("Alice"), "Wasserking1@ServerB", 0);
		serverA.addServiceAccess(serverA.users.get("Alice"), "Stromking2@ServerA", 0);
		System.out.println(serverA.users.get("Alice").getDevice().listAccess(0));
		/**
		 * hier gehts los
		 */
			
		System.out.println("Alle Services von Server A aus abrufen: " + serverA.getAllServices()+"\n");
		
		
		
//		Thread.sleep(lang);
		//Request Response
		String requestB = "device#0@ServerB";
		System.out.println("Stromking1@ServerA fragt einmalig Datum von " + requestB +" von Bob ab. Antwort:" + serverA.getLocalService("Stromking1").getDeviceData(requestB));
//		Thread.sleep(lang);
		System.out.println("Stromking1 subscribe data from "+ requestB + ": " + serverA.getLocalService("Stromking1").subscribe(requestB));
		String requestC= "device#0@ServerA";
		System.out.println("Stromking2 subscribe data from "+ requestC + ": " + serverA.getLocalService("Stromking2").subscribe(requestC));
		Thread.sleep(lang);
		serverA.users.get("Alice").getDevice().updateData(0);
		Thread.sleep(kurz);
		serverA.users.get("Alice").getDevice().updateData(0);
		Thread.sleep(kurz);
		serverA.users.get("Alice").getDevice().updateData(0);
		Thread.sleep(kurz);
		serverA.users.get("Alice").getDevice().updateData(0);
		Thread.sleep(kurz);
		System.out.println("Stromking2 unsubscribe data from "+ requestC + ": " + serverA.getLocalService("Stromking2").unsubscribe(requestC));
		Thread.sleep(kurz);
		serverA.users.get("Alice").getDevice().updateData(0);
		Thread.sleep(kurz);
		serverA.users.get("Alice").getDevice().updateData(0);
		Thread.sleep(kurz);
		serverA.users.get("Alice").getDevice().updateData(0);
	Thread.sleep(kurz);
	System.out.println("Stromking2@ServerA fragt einmalig Datum von " + requestC +" von Alice ab. Antwort:" + serverA.getLocalService("Stromking2").getDeviceData(requestC));
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
