package pt.upa.broker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPort;
import pt.upa.broker.ws.cli.BrokerClient;
import pt.upa.transporter.ws.cli.TransporterClient;

public class BrokerApplication {
	
	private static List<TransporterClient> transporters = new ArrayList<>();
	private static BrokerClient brokerBackup;
	public static final String BROKER1_ENTITY = "UpaBroker1";
	public static final String BROKER2_ENTITY = "UpaBroker2";
	public static boolean alive = false;
	public static int flag = 0;
	public static int flag1 = 0;
	
	public static List<TransporterClient> getTransportersList(){
		return transporters;
	}
	
	public static BrokerClient getBrokerBackup(){
		return brokerBackup;
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
				final TransporterClient tc = new TransporterClient(i);
				if(name.equals("UpaBroker1")){
					tc.ping("ola");
				}
				transporters.add(tc);
			}

			if(name.equals(BROKER1_ENTITY)){
				
				// connecting with BrokerBackup
				System.out.printf("Looking for '%s'%n", "UpaBrokerBackup");
				String endpoint1 = uddiNaming.lookup(BROKER2_ENTITY);
				BrokerClient bc = new BrokerClient(endpoint1);
				brokerBackup = bc;
				thread = new Thread(new Runnable() {
					public void run() {
						while(true) {
							bc.imAlive();
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								System.out.printf("Caught exception: %s%n", e);
								e.printStackTrace();		
							}
						}	
					}
				});
			}
			else {
				System.out.println("Standing by...");
				while(true) {
					Thread.sleep(1);
					if(flag==1){
						if(alive != false) {
							alive = false;
							Thread.sleep(600);
						}
						else {
							System.out.println("Primary Server Failure Detected!");
							uddiNaming.unbind(name);
							name = BROKER1_ENTITY;
							uddiNaming.rebind(name, url);
							port.Id=name;
							System.out.println("Backup Server Online");
							break;
						}
					}
				}
			}
			if(thread != null) {
				thread.start();
			}
			// wait
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();

		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		} finally {
			if(name.equals(BROKER1_ENTITY) && thread != null){
				thread.stop();
			}
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
