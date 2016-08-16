package AMQP2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 * @author Fabian Hempel
 * This class sets all required properties on the broker to make federation possible.
 *
 */

public class httpAccess {
	private String adminName = "guest";
	private String adminPw = "guest";
	private String serverName;
	
	
	/**
	 * Set up an instance.
	 * @param name name of the server.
	 */
	public httpAccess(String name){
		serverName=name;
	}
	
	
	/**
	 * Create a new user on the broker.
	 * @param serverName name of the new cloud. = username & password
	 * @throws IOException
	 */
	public void setServerAccess(String serverName) throws IOException{
		addServerUser(serverName);
		setServerPermissions(serverName);
		System.out.println("User: "+serverName+" Pw: "+serverName + " added at local RabbitMQ Server");
	}
	
	
	/**
	 * Set an upstream and set federation rules for wanted exchanges.
	 * @param serverName name of the cloud.
	 * @param ip ip of the cloud.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void setUpstreamExchange(String serverName, String ip) throws MalformedURLException, IOException{
		setUpstream(serverName, ip);
		setExchange(serverName);
		System.out.println("Exchange von "+serverName+" föderiert!");
	}
	
	
		/**
		 * Adds a new user on the broker, that will have access to it.
		 * @param name name of the cloud = username&password.
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
		 * Set the permissions of the created user on this broker.
		 * @param name name of the cloud.
		 * @throws MalformedURLException
		 * @throws IOException
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
	 * Connect local broker to the broker of the new cloud.
	 * @param name name of the cloud.
	 * @param ip ip of the cloud.
	 * @throws MalformedURLException
	 * @throws IOException
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
		 * Sets the format of the exchanges that should be federated from the new cloud.
		 * @param name name of the cloud.
		 * @throws MalformedURLException
		 * @throws IOException
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
			
	
}
