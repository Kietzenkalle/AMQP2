package AMQP;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Device {
		private String name, id;
		private int[] dataalt;
		HashMap<Integer,ArrayList<Object>> data;
		private Server server;
	
	
		/**
		 * Konstruktor
		 */
	public Device(Server server, String name, double ...datas){
		this.name=name;
		id= "ID";
		this.server=server;
		data= new HashMap<Integer,ArrayList<Object>>();
		for (double i : datas){
			this.addData(i);
		}
	}
	
	/**
	 * zusätzliches Datum hinzufügen
	 */
	protected void addData(double singleData){
		int help=data.size();
		data.put(help, new ArrayList<Object>());
		data.get(help).add(singleData);
		data.get(help).add(new ArrayList<String>());
		data.get(help).add(new ArrayList<String>());
	}
	
	/**
	 * zusätzliche DatEN hinzufügen
	 */
	protected void addData(double ...moreData){
		for (double i : moreData){
			addData(i);
		}
	}
	
	/**
	 * einzelnes Datum um eins erhöhen und publishen falls Subscriber vorhanden
	 */
	protected void updateData(int i) throws IOException{
		double help=(double) data.get(i).get(0);
		data.get(i).set(0, help+1);
		ArrayList<String> al2 = (ArrayList<String>) data.get(i).get(2);
		if (al2.size()>0){ 
			publish(Integer.toString(i), String.valueOf(help+1).toString(), al2);
		}
	}
	
	/**
	 * einzelnes Datum ändern und publishen falls Subscriber vorhanden
	 */
	protected void updateData(int i, double j) throws IOException{
		data.get(i).set(0, j);
		ArrayList<String> al2 = (ArrayList<String>) data.get(i).get(2);
		if (al2.size()>0){ 
			publish(Integer.toString(i), String.valueOf(j).toString(), al2);
		}
	}
	
	/**
	 * übergebenem Service Zugriff auf Datum an index i erlauben
	 */
	protected boolean setServiceAccess(String service, int i ){
		ArrayList<String> al = (ArrayList<String>) data.get(i).get(1);
				if (!al.contains(service)) {
					al.add(service);
					data.get(i).set(1, al);
					return true;
				}
				else return false;		
	}
	
	/**
	 * Zugriff entziehen
	 */
	protected void removeServiceAccess(String name, int i){
			ArrayList<String> al = (ArrayList<String>) data.get(i).get(1);
			ArrayList<String> al2 = (ArrayList<String>) data.get(i).get(2);
			if (!al.contains(name)) al.remove(name);
			if (!al2.contains(name)) al2.remove(name);
	}
	
	/**
	 * check ob Zugriff besteht
	 */
	protected boolean hasAccess(String name, int i){
		ArrayList<String> al = (ArrayList<String>) data.get(i).get(1);
		if(al.contains(name)) return true;
		else return false;
	}
	
	/**
	 * service subscriben wenn berechtigt
	 */
	protected boolean setServiceSubscribe(String name, int i){
			ArrayList<String> al = (ArrayList<String>) data.get(i).get(1);
			ArrayList<String> al2 = (ArrayList<String>) data.get(i).get(2);
			if (al.contains(name)) {
				
				if (!al2.contains(name)){
					al2.add(name);
					data.get(i).set(2, al2);
				}
				return true;
			}
			else return false;
	}
	
	/**
	 * unsuscribe
	 */
	protected boolean setServiceUnsubscribe(String name, int i){
		
			ArrayList<String> al2 = (ArrayList<String>) data.get(i).get(2);
			if(al2.contains(name)){
				al2.remove(name);
				return true;
			}
			else return false;
		
	}
	
	/**
	 * Subscriber auflisten
	 */
	protected HashMap<String, String> listFollower(){
		HashMap<String, String> follower=new HashMap<String,String>();
		//follower.put("Data", "Follower");
		for(Map.Entry<Integer, ArrayList<Object>> entry : data.entrySet()){ 
			ArrayList<String> al2 = (ArrayList<String>) data.get(entry.getKey()).get(2);
			follower.put(entry.getKey().toString(), al2.toString());
		}
		return follower;
	}
	
	/**
	 * getter für Datum an index i
	 */
	public String getData(int i) {
		String total = String.valueOf(data.get(i).get(0));
		return total ;
	}
	
	/**
	 * auflisten wer zugriff hat
	 */
	protected ArrayList<String> listAccess(int i){
		return (ArrayList<String>) data.get(i).get(1);
	}
	
	/**
	 * publishen
	 */
	protected void publish(String id, String data, ArrayList<String> subscriber) throws IOException{
		for (String one : subscriber){ 
			String[] split = one.split("@");
			server.sendPublish(data, "", name+"#"+id, one , "publish", split[1]);
		}
	}
	
//	public Device(String name, String id, int[] data){
//		this.name=name;
//		this.id=id;
//		this.dataalt=data;
	/*	this.data2=data2;
		this.data3=data3; */
//	}
	
	
	
//	public String getName(){
//		return name;
//	}
//	
//	String get(String filter){
//		if (filter.equals("name")) return this.name;
//		if (filter.equals("id")) return this.id;
//	//	if (filter.equals("data")) return this.data;
//		else return "";
//	}
	
//	void set(String filter, String wert){
//		
//		if (filter.equals("data1")) this.data=wert;
//	}
	

//	}
	
//	public HashMap<Integer,Integer> getData(int[] index){
//		HashMap<Integer,Integer> datas = new HashMap<Integer,Integer>();
//		for(int i: index){ datas.put(i, data[i]);}
//		return datas;
//		
//	}
//	
//	public HashMap getFilterData(String args[]){
//		HashMap help = new HashMap();
//		for (String help2 : args){
//			help.put(help2, this.get(help2));}
//		return help;
//		}
//	
//	public void updateData(){
//		for(int i=0; i<5; i++){
//			data[i]++;}
//					
//	}

	
	
}
