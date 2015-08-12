package AMQP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.*;


public class Help {
	
	public static void main(String[] argv) throws UnknownHostException, IOException{
//	Socket s = new Socket(InetAddress.getByName(null), 15672);
//	PrintWriter pw = new PrintWriter(s.getOutputStream());
//	
//	pw.println("-XPUT /api/policies/%2f/federate-me2");
//	String test = "{\"pattern\":\"^amq\\.\", \"definition\":{\"federation-upstream-set\":\"all\"}, \"apply-to\":\"exchanges\"}";
	
//	pw.flush();
//	BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//	String t=null;
//	while((t = br.readLine()) != null) System.out.println(t);
//	br.close();
		
		/**
		 * GET Request
		 */
//		String url1 = "http://localhost:15672/api/vhosts";
//		String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
//		String param1 = "value1";
//		String param2 = "value2";
//		// ...
//
//		String query = String.format("param1=%s&param2=%s", 
//		     URLEncoder.encode(param1, charset), 
//		     URLEncoder.encode(param2, charset));
//		Authenticator.setDefault (new Authenticator() {
//		    protected PasswordAuthentication getPasswordAuthentication() {
//		        return new PasswordAuthentication ("guest", "guest".toCharArray());
//		    }
//		});
//		URLConnection connection = new URL(url1).openConnection();
//		connection.setRequestProperty("Accept-Charset", charset);
//		InputStream error = ((HttpURLConnection) connection).getErrorStream();
//		InputStream response = connection.getInputStream();
//		BufferedReader br = new BufferedReader(new InputStreamReader(response));
//		String t=null;
//		while((t = br.readLine()) != null) System.out.println(t);
//		br.close();
		
		/**
		 * PUT new Vhost läuft nicht
//		 */
//		URL url4 = new URL("http://localhost:15672/api/vhosts/foo");
//		HttpURLConnection httpCon4 = (HttpURLConnection) url4.openConnection();
//		httpCon4.setDoOutput(true);
//		httpCon4.setRequestMethod("PUT");
//		httpCon4.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		Authenticator.setDefault (new Authenticator() {
//		    protected PasswordAuthentication getPasswordAuthentication() {
//		        return new PasswordAuthentication ("guest", "guest".toCharArray());
//		    }
//		});
//		httpCon4.connect();
		
		
		/**
		 * PUT new Exchange läuft
//		 */
//		URL url2 = new URL("http://localhost:15672/api/exchanges/%2f/my-new-exchange");
//		HttpURLConnection httpCon = (HttpURLConnection) url2.openConnection();
//		httpCon.setDoOutput(true);
//		httpCon.setRequestMethod("PUT");
//		httpCon.setRequestProperty("Content-Type", "application/json");
//		Authenticator.setDefault (new Authenticator() {
//		    protected PasswordAuthentication getPasswordAuthentication() {
//		        return new PasswordAuthentication ("guest", "guest".toCharArray());
//		    }
//		});
//		OutputStreamWriter out = new OutputStreamWriter(
//		    httpCon.getOutputStream());
//		out.write("{\"type\":\"direct\",\"durable\":true}");
//		out.close();
//		
//		BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
//		String t=null;
//		while((t = br.readLine()) != null) System.out.println(t);
//		br.close();
		
		/**
		 * DELETE new Exchange: läuft
		 */
//		URL url3 = new URL("http://localhost:15672/api/exchanges/%2f/my-new-exchange2");
//		HttpURLConnection httpCon2 = (HttpURLConnection) url3.openConnection();
//		httpCon2.setDoOutput(true);
//		httpCon2.setRequestMethod("DELETE");
//		httpCon2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		Authenticator.setDefault (new Authenticator() {
//		    protected PasswordAuthentication getPasswordAuthentication() {
//		        return new PasswordAuthentication ("guest", "guest".toCharArray());
//		    }
//		});
////		httpCon2.connect();   geht auch
//		OutputStreamWriter out2 = new OutputStreamWriter(
//		    httpCon2.getOutputStream());
//		
//		out2.close();
//		
//		BufferedReader br2 = new BufferedReader(new InputStreamReader(httpCon2.getInputStream()));
//		String t2=null;
//		while((t2 = br2.readLine()) != null) System.out.println(t2);
//		br2.close();
		
/**
 * 
		 * PUT new User läuft
		 */
//		URL url = new URL("http://localhost:15672/api/users/SERVERNAME\USER");
//		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//		httpCon.setDoOutput(true);
//		httpCon.setRequestMethod("PUT");
//		httpCon.setRequestProperty("Content-Type", "application/json");
//		Authenticator.setDefault (new Authenticator() {
//		    protected PasswordAuthentication getPasswordAuthentication() {
//		        return new PasswordAuthentication ("guest", "guest".toCharArray());
//		    }
//		});
//		OutputStreamWriter out = new OutputStreamWriter(
//		    httpCon.getOutputStream());
//		out.write("{\"password\":\"SERVERPW SAMEAS NAME\",\"tags\":\"\"}");
//		out.close();
//		
//		BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
//		String t=null;
//		while((t = br.readLine()) != null) System.out.println(t);
//		br.close();
		
		/**
		 * 
		 * PUT User Access
		 */
//				URL url = new URL("http://localhost:15672/api/permissions/%2f/ServerNAME"); //%2f=standardvirtualhost \
//				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//				httpCon.setDoOutput(true);
//				httpCon.setRequestMethod("PUT");
//				httpCon.setRequestProperty("Content-Type", "application/json");
//				Authenticator.setDefault (new Authenticator() {
//				    protected PasswordAuthentication getPasswordAuthentication() {
//				        return new PasswordAuthentication ("guest", "guest".toCharArray());
//				    }
//				});
//				OutputStreamWriter out = new OutputStreamWriter(
//				    httpCon.getOutputStream());
//				out.write("{\"configure\":\".*\",\"write\":\".*\",\"read\":\".*\"}");
//				out.close();
//				
//				BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
//				String t=null;
//				while((t = br.readLine()) != null) System.out.println(t);
//				br.close();
		
		/**
		 * 
		 * PUT new Upstream from target Server
		 * @throws MalformedURLException, IOException 
		 */
//		Authenticator.setDefault (new Authenticator() {
//		    protected PasswordAuthentication getPasswordAuthentication() {
//		        return new PasswordAuthentication ("guest", "guest".toCharArray());
//		    }
//		});
//		URL url = new URL("http://localhost:15672/api/policies/%2f/test2"); //%2f=standardvirtualhost \
//		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//		httpCon.setDoOutput(true);
//		httpCon.setRequestMethod("PUT");
//		httpCon.setRequestProperty("Content-Type", "application/json");
//		OutputStreamWriter out2 = new OutputStreamWriter(
//		    httpCon.getOutputStream());
//		out2.write("{\"pattern\":\"^"+"1A"+"."+"1b"+", \"definition\":{\"federation-upstream-set\":\""+"all\"},\"apply-to\":\"exchanges\"}");
//		out2.close();
//		
//		BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
//		String t=null;
//		while((t = br.readLine()) != null) System.out.println(t);
//		br.close();
		/**
		 * get policies
		 */
//		Authenticator.setDefault (new Authenticator() {
//		    protected PasswordAuthentication getPasswordAuthentication() {
//		        return new PasswordAuthentication ("guest", "guest".toCharArray());
//		    }
//		});
//	
//		String url = "http://localhost:15672/api/policies";
//		String charset = "UTF-8";
//		URLConnection connection = new URL(url).openConnection();
//		connection.setRequestProperty("Accept-Charset", charset);
//		InputStream response = connection.getInputStream();
//		BufferedReader br = new BufferedReader(new InputStreamReader(response));
//		String t=null;
//		while((t = br.readLine()) != null) System.out.println(t);
//		br.close();
		
		/**
		 * 
		 * PUT new policie: läuft
		 * @throws MalformedURLException, IOException 
		 */
		Authenticator.setDefault (new Authenticator() {
	    protected PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication ("guest", "guest".toCharArray());
	    }
	});
		URL url = new URL("http://localhost:15672/api/policies/%2f/test"); //%2f=standardvirtualhost \
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
		httpCon.setRequestProperty("Content-Type", "application/json");
		OutputStreamWriter out2 = new OutputStreamWriter(
		    httpCon.getOutputStream());
		out2.write("{\"pattern\":\"^amq.\", \"definition\":{\"federation-upstream-set\":\"all\"}, \"apply-to\":\"exchanges\"}");
		out2.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		String t=null;
		while((t = br.readLine()) != null) System.out.println(t);
		br.close();
		
		
		
		
		
	}
	}