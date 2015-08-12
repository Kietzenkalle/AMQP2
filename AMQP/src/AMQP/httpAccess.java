package AMQP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

public class httpAccess {
	private String adminName = "guest";
	private String adminPw = "guest";
	private String serverName;
	
	public httpAccess(String name){
		serverName=name;
	}
	
	
	/**
	 * add friendly Server as a User to the RabbitMQ Server
	 * @param serverName
	 * @throws IOException
	 */
	
	public void setServerAccess(String serverName) throws IOException{
		addServerUser(serverName);
		setServerPermissions(serverName);
		System.out.println("User: "+serverName+" Pw: "+serverName + " added at local RabbitMQ Server");
	}
	
	public void setUpstreamExchange(String serverName, String ip) throws MalformedURLException, IOException{
		setUpstream(serverName, ip);
		setExchange(serverName);
		//setQueue(serverName);
		System.out.println("Exchange von "+serverName+" föderiert!");
	}
	
	
	
		/**
		* PUT new Server/User läuft
		 * @throws IOException 
		*/
	private void addServerUser(String name) throws IOException{
		URL url = new URL("http://localhost:15672/api/users/"+name);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
		httpCon.setRequestProperty("Content-Type", "application/json");
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication (adminName, adminPw.toCharArray());
		    }
		});
		OutputStreamWriter out = new OutputStreamWriter(
		    httpCon.getOutputStream());
		out.write("{\"password\":\""+name+"\",\"tags\":\"\"}");
		out.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		String t=null;
		while((t = br.readLine()) != null) System.out.println(t);
		br.close();
	}	
	
		/**
		 * 
		 * PUT User Access
		 * @throws MalformedURLException, IOException 
		 */
	private void setServerPermissions(String name) throws MalformedURLException, IOException{
		URL url = new URL("http://localhost:15672/api/permissions/%2f/"+name); //%2f=standardvirtualhost \
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
		httpCon.setRequestProperty("Content-Type", "application/json");
		OutputStreamWriter out2 = new OutputStreamWriter(
		    httpCon.getOutputStream());
		out2.write("{\"configure\":\".*\",\"write\":\".*\",\"read\":\".*\"}");
		out2.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		String t=null;
		while((t = br.readLine()) != null) System.out.println(t);
		br.close();
	}
	
	
	/**
	 * 
	 * PUT new Upstream from target Server
	 * @throws MalformedURLException, IOException 
	 */
	private void setUpstream(String name, String ip) throws MalformedURLException, IOException{
		URL url = new URL("http://localhost:15672/api/parameters/federation-upstream/%2f/"+name+"-upstream"); //%2f=standardvirtualhost \
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
		httpCon.setRequestProperty("Content-Type", "application/json");
		OutputStreamWriter out2 = new OutputStreamWriter(
		    httpCon.getOutputStream());
		out2.write("{\"value\":{\"uri\":\"amqp://"+serverName+":"+serverName+"@"+ip+"\",\"expires\":3600000}}");
		out2.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		String t=null;
		while((t = br.readLine()) != null) System.out.println(t);
		br.close();
	}
		

		/**
		 * 
		 * PUT new Policy what Exchange to get federated
		 * @throws MalformedURLException, IOException 
		 */
		private void setExchange(String name) throws MalformedURLException, IOException{
			String policyName=name+"to"+serverName;
			String policyPattern=name+"."+serverName;
			Authenticator.setDefault (new Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication ("guest", "guest".toCharArray());
			    }
			});
				URL url = new URL("http://localhost:15672/api/policies/%2f/"+policyName+"Exchange"); //%2f=standardvirtualhost \
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				httpCon.setDoOutput(true);
				httpCon.setRequestMethod("PUT");
				httpCon.setRequestProperty("Content-Type", "application/json");
				OutputStreamWriter out2 = new OutputStreamWriter(
				    httpCon.getOutputStream());
				out2.write("{\"pattern\":\""+policyPattern+"\", \"definition\":{\"federation-upstream\":\""+name+"-upstream\"}, \"apply-to\":\"exchanges\"}");
				out2.close();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
				String t=null;
				while((t = br.readLine()) != null) System.out.println(t);
				br.close();}
		/**
		 * 
		 * PUT direct queue
		 * @throws MalformedURLException, IOException 
		 */
		private void setQueue(String name) throws MalformedURLException, IOException{
			String policyName=name+"to"+serverName;
			String policyPattern=name+"|"+serverName;
			Authenticator.setDefault (new Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication ("guest", "guest".toCharArray());
			    }
			});
				URL url = new URL("http://localhost:15672/api/policies/%2f/"+policyName+"Queue"); //%2f=standardvirtualhost \
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				httpCon.setDoOutput(true);
				httpCon.setRequestMethod("PUT");
				httpCon.setRequestProperty("Content-Type", "application/json");
				OutputStreamWriter out2 = new OutputStreamWriter(
				    httpCon.getOutputStream());
				out2.write("{\"pattern\":\"^"+policyPattern+"\", \"definition\":{\"federation-upstream\":\""+name+"-upstream\"}, \"apply-to\":\"queues\"}");
				out2.close();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
				String t=null;
				while((t = br.readLine()) != null) System.out.println(t);
				br.close();}
	
	
}
