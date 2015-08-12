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
		String response = server.subscribeDeviceData(devUsrSvr, name+"@"+server.SERVER_NAME);
		if (response.equals("Subscribe erfolgreich")){
			String[] dev_Svr = devUsrSvr.split("@");
			if(dev_Svr[1].equals(server.SERVER_NAME)){
				handler.addQueueBind("localExchange");
			}
			else handler.addQueueBind("pub"+dev_Svr[1]+"."+server.SERVER_NAME);
			
		}
		return response;
	}
	
	public String unsubscribe(String devUsrSvr) throws Exception{
		return server.unsubscribeDeviceData(devUsrSvr, name+"@"+server.SERVER_NAME);
	}
	
	
	
	public String getDeviceData(String device) throws Exception{
		return server.getDeviceData(device, name);
		
	}
}
