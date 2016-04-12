package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.jws.WebService;



@WebService(
	    endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	    wsdlLocation="transporter.1_0.wsdl",
	    name="TransporterWebService",
	    portName="TransporterPort",
	    targetNamespace="http://ws.transporter.upa.pt/",
	    serviceName="TransporterService"
	)

public class TransporterPort implements TransporterPortType{
	
	public String Id;
	
	static public Map<String, String> Locais = new HashMap<>(); 
	
	private List<JobView> Trabalhos = new ArrayList<>();
	
	public TransporterPort(String identifier) {
		Id=identifier;
	}
	
	public String getId() {
		return Id;
	}
	
	@Override
	public String ping(String name) {
		return "Pong " + name + "!";
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		/*COMO OBTER AS TRANSPORTADORAS???????????????*/	
		int offer=0;
		if((Locais.get(origin).equals("Norte") || Locais.get(origin).equals("Centro") 
				|| Locais.get(origin).equals("Sul"))){
			
			if((Locais.get(destination).equals("Norte") || Locais.get(destination).equals("Centro") 
					|| Locais.get(destination).equals("Sul"))){
				
				/*Origem e destino válidos*/
				if(price<=0){
					BadPriceFault faultInfo = new BadPriceFault();
					faultInfo.setPrice(price);
					throw new BadPriceFault_Exception("Preço inválido",faultInfo);
				}
				
				else if (((Locais.get(origin).equals("Norte") || Locais.get(destination).equals("Norte")) 
								&& Integer.parseInt(getId())%2!=0) 
						|| ((Locais.get(origin).equals("Sul") || Locais.get(destination).equals("Sul")) 
								&& Integer.parseInt(getId())%2==0)){
					return null;
				}
				
				else if (price>100){
					return null;
				}
				
				else if (price <=10){
					offer=ThreadLocalRandom.current().nextInt(1,10);
				}
				
				else{
					if ((price%2!=0 && Integer.parseInt(getId())%2!=0) || (price%2==0 && Integer.parseInt(getId())%2==0)) {
						/*nome impar preço impar ou nome par preço par*/
						offer=ThreadLocalRandom.current().nextInt(1,price);
					}
					else {
						/*nome par preço impar ou nome impar preço par*/
						offer = ThreadLocalRandom.current().nextInt(price+1,101);
					}
				}
			}
			else {
				BadLocationFault faultInfo = new BadLocationFault();
				faultInfo.setLocation(destination);
				throw new BadLocationFault_Exception("Destino inexistente", faultInfo);
			}
		}
		else {
			BadLocationFault faultInfo = new BadLocationFault();
			faultInfo.setLocation(origin);
			throw new BadLocationFault_Exception("Origem inexistente", faultInfo);
		}
		
		JobView job = new JobView();
		job.setCompanyName("UpaTransporter"+getId());
		job.setJobOrigin(origin);
		job.setJobDestination(destination);
		job.setJobIdentifier("" +getId());
		job.setJobState(JobStateView.PROPOSED);
		job.setJobPrice(offer);
		Trabalhos.add(job);
		return job;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		for( JobView i : Trabalhos) {
			if(i.getJobIdentifier().equals(id)) {
				if(accept){
					i.setJobState(JobStateView.ACCEPTED);
				}
				else {
					i.setJobState(JobStateView.REJECTED);	
				}
				return i;
			}
		}
		BadJobFault faultInfo = new BadJobFault();
		faultInfo.setId(id);
		throw new BadJobFault_Exception("Identificação inexistente", faultInfo);
		
	}

	@Override
	public JobView jobStatus(String id) {
		for( JobView i : Trabalhos) {
			if(i.getJobIdentifier().equals(id)) {
				return i;
			}
		}
		return null;
	}

	@Override
	public List<JobView> listJobs() {
		return Trabalhos;
	}

	@Override
	public void clearJobs() {
			Trabalhos.clear();
	}

	

}
