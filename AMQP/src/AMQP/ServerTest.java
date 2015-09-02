package AMQP;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

 
/**
 *  com.edw.test.StudentStressTest
 *
 *  @author edw
 */
public class ServerTest extends AbstractJavaSamplerClient {
 
    private Map<String, String> mapParams = new HashMap<String, String>();
    Server server;
    
 
    public ServerTest() {
        super();
    }
 
    @Override
    public void setupTest(JavaSamplerContext context) {
        for (Iterator<String> it = context.getParameterNamesIterator(); it.hasNext();) {
            String paramName =  it.next();
            mapParams.put(paramName, context.getParameter(paramName));
            try {
				server = new Server("ServerA");
			
            server.addNewTrustedCloud("ServerB", "192.168.0.106");
            
            server.addService("Stromking1");
            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
 
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
 
        try {
 
            
            String ergebnis;
            result.sampleStart();
            
            ergebnis=server.getLocalService("Stromking1").getDeviceData("device#0@ServerB");
 
            result.sampleEnd();
            if (ergebnis.equals("6.0")){             
            result.setSuccessful(true);
            result.setSampleLabel("SUCCESS: "+ergebnis);
            }
            else {
            	result.setSuccessful(false);
                result.setSampleLabel("TimeOut");
            }
 
        } catch (Throwable e) {
            result.sampleEnd();
            result.setSampleLabel("FAILED: '" + e.getMessage() + "' || " + e.toString());
            result.setSuccessful(false);
 
            e.printStackTrace();
            System.out.println("\n\n\n");
        }
 
        return result;
    }
 
    @Override
    public Arguments getDefaultParameters() {
 
        Arguments params = new Arguments();
 
        params.addArgument("name", "edw");
 
        return params;
    }
}