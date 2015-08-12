package AMQP;
import java.io.IOException;
import java.util.ArrayList;

public class User {
	
	private String name;
	private String id;
	private Device device;
	
	public User(String name, String id, Device device){
		this.name=name;
		this.id=id;
		this.device=device;
		
	}
	
	void addDeviceData(Device device){
		this.device=device;
		
	}
	
	Device getDevice(){
		return device;
	}
	
	String getName(){
		return this.name;}
	
	String getId(){
		return this.id;}
	
//	String getData1(){
//		return device.get("data1");}
	
	void updateData(int ...data) throws IOException{
		for (int i : data){
		device.updateData(i);
		}
	}
	
	
	
	
}
