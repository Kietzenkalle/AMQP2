package AMQP;

import java.io.IOException;

public class Service {
	private String name;
	private Server server;
	private ServiceMessageHandler handler;
	
	public Service(Server server, String name) throws IOException{
		this.name = name;
		this.server= server;
		
		handler= new ServiceMessageHandler(name, server);
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public String subscribe(String devUsrSvr) throws Exception{
		String response = server.subscribeDeviceData(devUsrSvr, name);
		if (response.equals("Subscribe erfolgreich")){
			String[] dev_Svr = devUsrSvr.split("@");
			handler.addQueueBind("pub"+dev_Svr[1]+"."+server.SERVER_NAME);
			
		}
		return response;
	}
	
	public String responseSubscribe(String)
	
	public String getDeviceData(String device) throws Exception{
		return server.getDeviceData(device, name);
		
	}
}
