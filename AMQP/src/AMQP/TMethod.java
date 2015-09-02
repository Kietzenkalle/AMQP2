package AMQP;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

public class TMethod implements Runnable{
	String type, from, to;
	Server server;
	Calendar calendar;
	long request;
	long response;
	static int counter=0;
	int id;
	
	
	TMethod(Server server, String type, String from, String to){
		this.type=type;
		this.from=from;
		this.to=to;
		this.server=server;
		calendar = Calendar.getInstance();
				
	}
	@Override
	public void run() {
		counter++;
		id=counter;
		switch (type){
		case "subscribe":{
			
			try {
				
				request= System.nanoTime();
				System.out.println(new Timestamp(calendar.getTime().getTime()) + " subscribe from "+ from + " to "+to);
				System.out.println(new Timestamp(calendar.getTime().getTime()) + " "+from+ " subscribe data from "+ to + "Result: " + server.getLocalService(from).subscribe(to) + " Laufzeit: " + (request- System.nanoTime()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		}
		case "unsubscribe":{
				try {
					
					System.out.println(from+" unsubscribe data from "+ to + ": " + server.getLocalService(from).unsubscribe(to));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
		}
		case "getData":{
				try {
					request= System.nanoTime();
					System.out.println(new Timestamp(calendar.getTime().getTime())+ " "+id+" getData "+from + " "+to);
					System.out.println(new Timestamp(calendar.getTime().getTime())+ " "+id+" getData "+from + " "+to + " Result: " + server.getLocalService(from).getDeviceData(to)+ " Laufzeit: " + (System.nanoTime()-request)/1e6);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
		}
		case "getServices":{
					try {
						System.out.println("Alle Services von "+server.SERVER_NAME+" aus abrufen: " + server.getAllServices()+"\n");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			break;
		}
		case "getServiceAccesses":{
			
			break;
		}
		
		default: break;
		
		}
		
		
	}

}
