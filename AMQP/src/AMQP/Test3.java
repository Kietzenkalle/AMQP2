package AMQP;

import java.util.concurrent.TimeoutException;

public class Test3 {
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
		
		serverA.devices.put("device", new Device(serverA, "device", 1,2,3,4,5));
		serverA.users.put("Alice", new User("Alice", "user1", serverA.devices.get("device")));
		
		//Bob vergibt Zugriffsrechte
		serverA.addServiceAccess(serverA.users.get("Alice"), "Wasserking1@ServerB", 0);
		serverA.addServiceAccess(serverA.users.get("Alice"), "Stromking2@ServerA", 0);
		
		String requestB = "device#0@ServerB";
		//		Thread.sleep(lang);
		System.out.println("Stromking1 subscribe data from "+ requestB + ": " + serverA.getLocalService("Stromking1").subscribe(requestB));
		Thread.sleep(5000);
		System.out.println("Stromking1 unsubscribe data from "+ requestB + ": " + serverA.getLocalService("Stromking1").unsubscribe(requestB));
}
}