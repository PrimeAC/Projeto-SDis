package example.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.security.cert.Certificate;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import example.ws.Ca;
import example.ws.CaImplService;
import example.ws.Exception_Exception;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class CaClient {
	
	private static Ca port;
	
	public CaClient() throws JAXRException, Exception_Exception, IOException, ClassNotFoundException {
		
		String uddiURL = "http://localhost:8090/ca-ws/endpoint";
		String name = "Ca";

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
		CaImplService service = new CaImplService();
		port = service.getCaImplPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

		System.out.println("Remote call ...");
		String result = sayHello("friend");
		System.out.println(result);
		byte[] result1 = getCertificates("UpaBroker");
		System.out.println("bytes:"+result1);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(result1);
		ObjectInput in = null;
		in = new ObjectInputStream(bis);
		Certificate certificate = (Certificate) in.readObject();
		System.out.println(certificate);
	}
	
	
	public String sayHello(String message){
		return port.sayHello(message);	
	}
	
	public byte[] getCertificates(String message) throws Exception_Exception{
		return port.getCertificates(message);	
	}
	
	
	public static void main(String[] args) throws Exception {
		
	}
	
	
	

}
