package pt.upa.broker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.registry.JAXRException;
import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPort;
import pt.upa.broker.ws.cli.BrokerClient;
import pt.upa.transporter.ws.cli.TransporterClient;

public class BrokerApplication extends Thread{
	
	private static List<TransporterClient> transporters = new ArrayList<>();
	private static BrokerClient brokerBackup;
	public static boolean alive = true;
	private final static String UPA_BROKER1_NAME = "UpaBroker1";
	private final static String UPA_BROKER1_URL = "http://localhost:8091/broker-ws/endpoint";
	private final static String UPA_BROKER2_NAME = "UpaBroker2";
	private final static String UDDI_URL = "http://localhost:9090";
	
	public static List<TransporterClient> getTransportersList(){
		return transporters;
	}
	
	public static BrokerClient getBrokerBackup(){
		return brokerBackup;
	}
	
	public static void BrokerReplace() throws JAXRException{
		
		UDDINaming uddiNaming = new UDDINaming(UDDI_URL);
		uddiNaming.rebind(UPA_BROKER1_NAME, UPA_BROKER1_URL);
	}
	
	public void run(){
		
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(BrokerApplication.class.getSimpleName() + " starting...");
		
		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName wsURL%n", BrokerApplication.class.getName());
			return;
		}
		
		String uddiURL = args[0];
		String name = args[1];
		String url = args[2];

		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		Thread thread = null;
		
		try {
			BrokerPort port = new BrokerPort(name);
			endpoint = Endpoint.create(port);

			// publish endpoint
			System.out.printf("Starting %s%n", url);
			endpoint.publish(url);

			// publish to UDDI
			System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
			uddiNaming = new UDDINaming(uddiURL);
			uddiNaming.rebind(name, url);
			
			// connecting with transporter
			System.out.printf("Looking for '%s'%n", "UpaTransporters");
			Collection<String> endpoints = uddiNaming.list("UpaTransporter%");
			
			System.out.println("Creating stub(s) ...");
			for(String i : endpoints) {
				TransporterClient tc = new TransporterClient(i);
				tc.ping("ola");
				transporters.add(tc);
			}
			
			if(name.equals("UpaBroker1")){
				// connecting with BrokerBackup
				System.out.printf("Looking for '%s'%n", "UpaBrokerBackup");
				String endpoint1 = uddiNaming.lookup("UpaBroker2");
				System.out.println("endpoint" + endpoint1);
				System.out.println("novo print");
				BrokerClient bc = new BrokerClient(endpoint1);
			}
			
			// wait
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();

		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		} finally {
			try {
				if (endpoint != null) {
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
					
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null) {
					// delete from UDDI
					uddiNaming.unbind(name);
					System.out.printf("Deleted '%s' from UDDI%n", name);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}
	}

}
