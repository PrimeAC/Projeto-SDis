package pt.upa.transporter.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;

import example.ws.handler.SignatureHandler;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;

public class TransporterClient {
	
	private TransporterPortType port;
	
	private String name;
	
	private Map<String, Object> requestContext;
	
	public static final String BROKER_ENTITY = "UpaBroker";
	
	public TransporterClient(String endpointAddress) {
		
		
		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}
		
		name = "UpaTransporter" + endpointAddress.charAt(20);
		
		
		//System.out.println("Creating stub ...");
		TransporterService service = new TransporterService();
		port = service.getTransporterPort();
	
		//System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		
	}

	public String getCompanyName() {
		requestContext.put(SignatureHandler.REQUEST_PROPERTY, BROKER_ENTITY);
		return name;
	}
	
	public TransporterPortType getPort() {
		requestContext.put(SignatureHandler.REQUEST_PROPERTY, BROKER_ENTITY);
		return port;
	}
	
	public String ping(String message){
		requestContext.put(SignatureHandler.REQUEST_PROPERTY, BROKER_ENTITY);
		return port.ping(message);	
	}
	
	public JobView requestJob(String origin, String destination, int price)
		throws BadLocationFault_Exception, BadPriceFault_Exception {
		requestContext.put(SignatureHandler.REQUEST_PROPERTY, BROKER_ENTITY);
		return port.requestJob(origin, destination, price);
	}
	
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		requestContext.put(SignatureHandler.REQUEST_PROPERTY, BROKER_ENTITY);
		return port.decideJob(id, accept);
	}
	
	public List<JobView> listJobs() {
		requestContext.put(SignatureHandler.REQUEST_PROPERTY, BROKER_ENTITY);
		return port.listJobs();
	}
	
	public JobView jobStatus(String id) {
		requestContext.put(SignatureHandler.REQUEST_PROPERTY, BROKER_ENTITY);
		return port.jobStatus(id);
	}
	
	public void clearJobs() {
		requestContext.put(SignatureHandler.REQUEST_PROPERTY, BROKER_ENTITY);
		port.clearJobs();
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}
