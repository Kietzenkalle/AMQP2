package AMQP;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class sets up an owner of a device.
 * @author Fa
 *
 */
public class User {
	
	private String name;
	private String id;
	private Device device;
	
	/**
	 * Sets up an user, creates an id and connects the device to the user.
	 * @param name name of the user.
	 * @param id id of the user.
	 * @param device device that connects to the user.
	 */
	public User(String name, String id, Device device){
		this.name=name;
		this.id=id;
		this.device=device;
		
	}
	
	/**
	 * Returns the device of the user.
	 * @return Device of the user.
	 */
	Device getDevice(){
		return device;
	}
	
	/**
	 * Returns the name of the user.
	 * @return String name of the user.
	 */
	String getName(){
		return this.name;}
	
	/**
	 * Returns the id of the user.
	 * @return String id of the user.
	 */
	String getId(){
		return this.id;}
	
	
	
	
	
	
}
