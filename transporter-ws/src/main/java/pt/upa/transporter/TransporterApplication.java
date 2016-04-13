package pt.upa.transporter;

import java.util.HashMap;
import java.util.Map;

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
		
		Map<String, String> Locais = new HashMap<>();
		
		Locais.put("Porto","Norte");
		Locais.put("Braga","Norte");
		Locais.put("Viana do Castelo","Norte");
		Locais.put("Vila Real","Norte");
		Locais.put("Bragança","Norte");
		
		Locais.put("Lisboa","Centro");
		Locais.put("Leiria","Centro");
		Locais.put("Santarem","Centro");
		Locais.put("Castelo Branco","Centro");
		Locais.put("Coimbra","Centro");
		Locais.put("Aveiro","Centro");
		Locais.put("Viseu","Centro");
		Locais.put("Guarda","Centro");
		
		Locais.put("Setubal","Sul");
		Locais.put("Evora","Sul");
		Locais.put("Portalegre","Sul");
		Locais.put("Beja","Sul");
		Locais.put("Faro","Sul");
		
		TransporterPort.Locais = Locais;
		
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
