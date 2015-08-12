package AMQP;

public class Service {
	private String name;
	private Server server;
	
	public Service(Server server, String name){
		this.name = name;
		this.server= server;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public void subscribe(String devUsrSvr) throws Exception{
//		String[] device = devUsrSvr.split("@");
//		String[] dev_Data = device[0].split("#");
		System.out.println(server.subscribeDeviceData(devUsrSvr, name));
		
	}
}
