package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
	
	private List<JobView> Trabalhos = new ArrayList<>();
	private ArrayList<String> Norte = new ArrayList<>(Arrays.asList("Porto","Braga","Viana do Castelo","Vila Real","Bragança"));
	private ArrayList<String> Centro = new ArrayList<>(Arrays.asList("Lisboa", "Leiria","Santarem","Castelo Branco","Coimbra","Aveiro","Viseu","Guarda"));
	private ArrayList<String> Sul = new ArrayList<>(Arrays.asList("Setubal","Evora","Portalegre","Beja","Faro"));
	private Random random =new Random();
	
	@Override
	public String ping(String name) {
		return "Pong " + name + "!";
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		/*COMO OBTER AS TRANSPORTADORAS???????????????*/	
		if((Norte.contains(origin) || Centro.contains(origin) || Sul.contains(origin))){
			if((Norte.contains(destination) || Centro.contains(destination) || Sul.contains(destination))){
				/*Origem e destino válidos*/
				if(price<0){
					BadPriceFault faultInfo = new BadPriceFault();
					faultInfo.setPrice(price);
					throw new BadPriceFault_Exception("Preço inválido",faultInfo);
				}
				else if (price>100){
					return null;
				}
				else if (price <=10){
					int offer=random.nextInt(10);
					JobView job = new JobView();
					job.setJobPrice(offer);
					return job;
					
				}
				else{
					JobView job= new JobView();
					if (price %2!=0) {
						/*nome impar preço impar*/
						/*COMPANYNAME OU GETCOMPANYNAME????*/
						if(Integer.parseInt(job.companyName.substring(job.companyName.length() - 1))%2!=0 ){
							int offer=random.nextInt(price);
							job.setJobPrice(offer);
							return job;
						}
						else {
							int High = 100;
							int offer = random.nextInt(High-price) + price;
							job.setJobPrice(offer);
							return job;
						}
					}
					else{
						/*nome par preço par*/
						if(Integer.parseInt(job.companyName.substring(job.companyName.length() - 1))%2==0 ){
							int offer=random.nextInt(price);
							job.setJobPrice(offer);
							return job;
						}
						else {
							int High = 100;
							int offer = random.nextInt(High-price) + price;
							job.setJobPrice(offer);
							return job;
						}
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
