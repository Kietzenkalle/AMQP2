package AMQP;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

public class JavaRequestSamplerDemo extends AbstractJavaSamplerClient {

	  @Override
	  public SampleResult runTest(JavaSamplerContext ctx) {
	    JMeterVariables vars = JMeterContextService.getContext().getVariables();
	    vars.put("demo", "demoVariableContent");

	    SampleResult sampleResult = new SampleResult();
	    sampleResult.setSuccessful(true);
	    sampleResult.setResponseCodeOK();
	    sampleResult.setResponseMessageOK();
	    sampleResult.setSuccessful(true);
	    sampleResult.setSampleLabel("SUCCESS: ");
	    return sampleResult;
	  }  
	}