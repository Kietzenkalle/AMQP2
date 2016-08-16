package AMQP2;

import java.io.IOException;

/**
 * @author Fabian Hempel
 * Class that contains a service and let it get and receive data.
 *
 */

public class Service {
	private String name;
	private Server server;
	private ServiceMessageHandler handler;
	
	
	/**
	 * Creates a new Service.
	 * @param server name of the server.
	 * @param name name of the service.
	 * @throws IOException
	 */
	public Service(Server server, String name) throws IOException{
		this.name = name;
		this.server= server;
		
		handler= new ServiceMessageHandler(name, server);
	}
	
	
	@Override
	/**
	 * Returns the service as a String.
	 */
	public String toString(){
		return name;
	}
	
	/**
	 * Subscribe to a device data. If the subscription was successfull, build a new queue where the published messages are received.
	 * @param devUsrSvr target device, has to be in format "devicename#dataindex@targetserver".
	 * @return String with the success of the subscription.
	 * @throws Exception
	 */
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
	
	
	/**
	 * Unsubscribe device data.
	 * @param devUsrSvr target device, has to be in format "devicename#dataindex@targetserver".
	 * @return String with the success of the unsubscription.
	 * @throws Exception
	 */
	public String unsubscribe(String devUsrSvr) throws Exception{
		return server.unsubscribeDeviceData(devUsrSvr, name+"@"+server.SERVER_NAME);
	}
	
	
	/**
	 * Request the data of a device.
	 * @param device target device, has to be in format "devicename#dataindex@targetserver".
	 * @return String with the response.
	 * @throws Exception
	 */
	public String getDeviceData(String device) throws Exception{
		return server.getDeviceData(device, name+"@"+server.SERVER_NAME);
		
	}
}
