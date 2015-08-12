package AMQP;

import java.util.concurrent.TimeoutException;


public class Main {
	public static void main(String[] argv) throws TimeoutException, Exception{

		
		
//		
		
		//		Server test = new Server("Test");
//		test.createQueue("testQueue");
//		
		;
		Server serverA = new Server("ServerA");
		Server serverB = new Server("ServerB");
		
		serverA.addNewTrustedCloud("ServerB","192.168.56.101");
//		serverB.addNewTrustedCloud("ServerA");
//		serverA.sendMessage("Hallo B", "sr_ServerA|ServerB");
//		serverB.sendMessage("Hallo", "sr_ServerB|ServerA");
		Thread.sleep(5000);
		System.exit(0);
	}
}
