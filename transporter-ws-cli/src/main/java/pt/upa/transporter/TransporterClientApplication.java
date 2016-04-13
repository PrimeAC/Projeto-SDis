package pt.upa.transporter;

import pt.upa.transporter.ws.cli.TransporterClient;

public class TransporterClientApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(TransporterClientApplication.class.getSimpleName() + " starting...");
		
		
		TransporterClient c = new TransporterClient("http://localhost:8081/transporter-ws/endpoint");
		System.out.println(c.ping("friend2"));
		System.out.println(c.requestJob("Lisboa", "Porto", 5));

	}
}
