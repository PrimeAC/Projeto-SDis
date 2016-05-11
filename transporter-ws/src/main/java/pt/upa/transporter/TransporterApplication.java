package pt.upa.transporter;



import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.TransporterPort;

public class TransporterApplication {
	
	public static void main(String[] args) throws Exception {
		System.out.println(TransporterApplication.class.getSimpleName() + " starting...");
		
		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName wsURL%n", TransporterApplication.class.getName());
			return;
		}
	
		
		String uddiURL = args[0];
		String Id = args[1];
		String url = args[2];
	
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		
		try {
			TransporterPort port = new TransporterPort(Id);
			endpoint = Endpoint.create(port);
			
			// publish endpoint
			System.out.printf("Starting %s%n", url);
			endpoint.publish(url);
	
			// publish to UDDI
			System.out.printf("Publishing '%s' to UDDI at %s%n", args[1], uddiURL);
			uddiNaming = new UDDINaming(uddiURL);
			uddiNaming.rebind(args[1], url);
	
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
					uddiNaming.unbind(args[1]);
					System.out.printf("Deleted '%s' from UDDI%n", args[1]);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}
	}

}
