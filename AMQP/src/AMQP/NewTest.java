package AMQP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewTest {
	
	static Server server;
	static String serverA="ServerA";
	static String serverB="ServerB";
	static String serverC="ServerC";
	static String userA = "Alice";
	static String userB = "Bob";
	static String userC = "Charlie";
	static ExecutorService threadExecutor;
	static int kurz=1000;
	int lang=5000;
	
	
	public static void main(String[] argv) throws Exception{
		threadExecutor = Executors.newFixedThreadPool(10);
		Calendar calendar = Calendar.getInstance();
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.print("To Start ServerA press Enter");
//        String s = br.readLine();
        server = new Server(serverA);
        server.addNewTrustedCloud("ServerB", "192.168.0.106");
        
        server.addService("Stromking1");
		server.addService("Stromking2");
		server.addService("Stromking3");
        
		//setup	Users+Devices	
		server.devices.put("device", new Device(server, "device", 1,2,3,4,5));
		server.users.put("Alice", new User("Alice", "user1", server.devices.get("device")));
        
		//Bob vergibt Zugriffsrechte
		server.addServiceAccess(server.users.get("Alice"), "Wasserking1@ServerB", 0);
		server.addServiceAccess(server.users.get("Alice"), "Stromking2@ServerA", 0);
		server.addServiceAccess(server.users.get("Alice"),  "Heizungking1@ServerC", 0);
		//System.out.println(server.users.get("Alice").getDevice().listAccess(0));
		
		while(true){
			Thread.sleep(10);
		threadExecutor.submit(new TMethod(server,"getData","Stromking1","device#0@ServerB"));
//		threadExecutor.submit(new TMethod(server,"getData","Stromking1","device#1@ServerB"));
//		threadExecutor.submit(new TMethod(server,"getData","Stromking1","device#2@ServerB"));
//		threadExecutor.submit(new TMethod(server,"getData","Stromking1","device#3@ServerB"));
//		threadExecutor.submit(new TMethod(server,"getData","Stromking1","device#4@ServerB"));
		}
//		threadExecutor.submit(new TMethod(server,"subscribe","Stromking1","device#0@ServerB"));
//		
//		threadExecutor.submit(new TMethod(server,"getServices", "", ""));
		
		
//		Thread.sleep(kurz);
//		server.users.get("Alice").getDevice().updateData(0);
//		Thread.sleep(kurz);
//		server.users.get("Alice").getDevice().updateData(0);
//		Thread.sleep(kurz);
//		server.users.get("Alice").getDevice().updateData(0);
//		Thread.sleep(kurz);
//		server.users.get("Alice").getDevice().updateData(0);
//		Thread.sleep(kurz);
//		threadExecutor.submit(new TMethod(server,"unsubscribe","Stromking2","device#0@ServerA"));
//		Thread.sleep(kurz);
//		server.users.get("Alice").getDevice().updateData(0);
//		Thread.sleep(kurz);
//		server.users.get("Alice").getDevice().updateData(0);
//		Thread.sleep(kurz);
//		server.users.get("Alice").getDevice().updateData(0);
//		Thread.sleep(kurz);
//		threadExecutor.submit(new TMethod(server,"getData","Stromking2","device#0@ServerA"));
		
		
		
		
	}
}
