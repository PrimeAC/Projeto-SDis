package pt.upa.transporter.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import java.util.Map;
import javax.xml.ws.BindingProvider;

import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;

public class TransporterClient {
	private TransporterPortType port;
	
	public TransporterClient(String endpointAddress) {
		
		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}
		
		//System.out.println("Creating stub ...");
		TransporterService service = new TransporterService();
		port = service.getTransporterPort();
	
		//System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	}
	
	public String ping(String message){
		return port.ping(message);	
	}
	
	public static void main(String[] args) throws Exception {
		/*
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n", TransporterClient.class.getName());
			return;
		}
	
		String uddiURL = args[0];
		String name = args[1];
	
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
	
		System.out.printf("Looking for '%s'%n", name);
		String endpointAddress = uddiNaming.lookup(name);
	
		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}
	
		System.out.println("Creating stub ...");
		TransporterService service = new TransporterService();
		TransporterPortType port = service.getTransporterPort();
	
		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	
		String result = port.ping("friend");
		System.out.println(result);
		*/
	}
}
