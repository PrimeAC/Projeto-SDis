package pt.upa.broker.ws.it;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.cli.BrokerClient;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import static org.junit.Assert.*;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

/**
 *  Integration Test example
 *  
 *  Invoked by Maven in the "verify" life-cycle phase
 *  Should invoke "live" remote servers 
 */
public class AbstractIT {

    // static members
	
	protected static BrokerPortType port;
    // one-time initialization and clean-up

    @BeforeClass
    public static void oneTimeSetUp() throws JAXRException, InterruptedException {
    	String uddiURL = "http://localhost:9090" ;
		String name = "UpaBroker1";

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

		System.out.println("Creating stubs ...");
		BrokerService service = new BrokerService();
		port = service.getBrokerPort();
		
		System.out.println("Setting endpoint address for Broker 1 ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		
    }

    @AfterClass
    public static void oneTimeTearDown() {
    	port.clearTransports();
    	port=null;
    }


    // members


    // initialization and clean-up for each test

    @Before
    public void setUp() {
    	
    }

    @After
    public void tearDown() {
    }



}