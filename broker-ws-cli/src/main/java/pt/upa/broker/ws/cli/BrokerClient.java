package pt.upa.broker.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

//import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

// classes generated from WSDL
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class BrokerClient {
	
	private BrokerPortType port;
	
	private String name;
	
	private Map<String, Object> requestContext;
	
	public BrokerClient(String endpointAddress) {
	
		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}
		
		name = "UpaBroker" + endpointAddress.charAt(20);
		
		
		System.out.println("Creating stub ...");
		BrokerService service = new BrokerService();
		port = service.getBrokerPort();
	
		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	}
	
	public String ping(String message){
		return port.ping(message);	
	}
	
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
		return port.requestTransport(origin, destination, price);
	}
	
	public List<TransportView> listTransports() {
		return port.listTransports();
	}
	
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		return port.viewTransport(id);
	}
	
	public void receiveUpdate(TransportView arg1,String arg2, int arg3) {
		port.receiveUpdate(arg1, arg2, arg3);
	}
	
	public void imAlive() {
		port.imAlive();
	}
	
	public void clearTransports() {
		port.clearTransports();
	}
	
	public static void main(String[] args) throws Exception {
		
	}
	

}

