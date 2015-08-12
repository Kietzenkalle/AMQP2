package AMQP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import java.util.concurrent.TimeoutException;



public class MainTest2 {


	public static void main(String[] argv) throws TimeoutException, Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter IP ServerA");
        String s = br.readLine();
		int kurz=500;

		int lang=50000;

		

		//setup Server(clients)


		Server serverB = new Server("ServerB");

		serverB.addNewTrustedCloud("ServerA",s);

		serverB.addService("Wasserking1");

		serverB.addService("Wasserking2");

		serverB.addService("Wasserking3");

		
		//setup	Users+Devices	


		serverB.devices.put("device", new Device(serverB, "device", 6,7,8,9,10));

		serverB.users.put("Bob", new User("Bob", "user2", serverB.devices.get("device")));

		serverB.addServiceAccess(serverB.users.get("Bob"), "Stromking1@ServerA", 0);

		Thread.sleep(2000);

		

		/**

		 * hier gehts los

		 */

		//Bob vergibt Zugriffsrechte	

		

	//	serverB.addServiceAccess(serverB.users.get("Bob"), "Stromking2@ServerC", 0);

		//

		String requestA = "device#0@ServerA";

		System.out.println("Wasserking1@ServerB fragt einmalig Datum von " + requestA +" von Alice ab. Antwort:" + serverB.getLocalService("Wasserking1").getDeviceData(requestA));

		System.out.println("Subscribe data from "+ requestA + ": " + serverB.getLocalService("Wasserking1").subscribe(requestA));

		//serverB.sendMessage("hallo", "ServerA");

//		serverB.getAllServices();

//		Thread.sleep(lang);

		

//		4x update an Bobs daten+publish

		serverB.users.get("Bob").getDevice().updateData(0);

		Thread.sleep(kurz);

		serverB.users.get("Bob").getDevice().updateData(0);

		Thread.sleep(kurz);

		serverB.users.get("Bob").getDevice().updateData(0);

		Thread.sleep(kurz);

		serverB.users.get("Bob").getDevice().updateData(0);

		Thread.sleep(kurz);

		System.out.println("Alle Services von Server B aus abrufen: " + serverB.getAllServices()+"\n");

		

//		Thread.sleep(lang);

		//Bobs Follower

//		System.out.println("Follower von Bob: "+serverB.discoverFollowers(serverB.users.get("Bob"))+"\n");

//		Thread.sleep(5000);

		





	

	

		

	

		

			

			

	

	

		

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

}

