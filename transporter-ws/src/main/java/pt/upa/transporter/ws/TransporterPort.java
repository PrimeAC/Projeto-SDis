package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
	
	static public int cnt=0;
	
	private List<JobView> Trabalhos = new ArrayList<>();
	
	public TransporterPort(String identifier) {
		Id=String.valueOf(identifier.charAt(14));
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
		job.setCompanyName("UpaTransporter"+getId()); //talvez mal
		job.setJobOrigin(origin);
		job.setJobDestination(destination);
		job.setJobIdentifier(getId()+Integer.toString(cnt++));  //talvez mal
		job.setJobState(JobStateView.PROPOSED);
		job.setJobPrice(offer);
		Trabalhos.add(job);
		System.out.println(job.getJobPrice());

		return job;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		for( JobView i : Trabalhos) {
			if(i.getJobIdentifier().equals(id)) {
				if(accept){
					i.setJobState(JobStateView.ACCEPTED);
					new Repeater(i);
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

	public class Repeater {
		
		Timer timer;
		Timer timer2;
		Timer timer3;
		
		public Repeater(JobView job) {
			
			timer = new Timer();
			timer2 = new Timer();
			timer3 = new Timer();
	
			int temp = (ThreadLocalRandom.current().nextInt(1000,5001));
			int temp2 = temp + ThreadLocalRandom.current().nextInt(1000,5001);
			int temp3 = temp2 + ThreadLocalRandom.current().nextInt(1000,5001);
			
			timer.schedule(new StatusChanger(job),temp);
			timer2.schedule(new StatusChanger(job), temp2);
			timer3.schedule(new StatusChanger(job), temp3);
		}
		class StatusChanger extends TimerTask {
			
			private JobView job;
			
			StatusChanger(JobView arg) {
				job=arg;
			}
			
			public void run() {
				switch(job.getJobState()) {
				
					case ONGOING: 
						job.setJobState(JobStateView.COMPLETED);
						timer3.cancel();
						break;
					
					case HEADING: 
						job.setJobState(JobStateView.ONGOING);
						timer2.cancel();
						break;
					
					case ACCEPTED: 
						job.setJobState(JobStateView.HEADING);
						timer.cancel();
						break;
					
					default: break;
				}
			}
		}
		
			
		}

}
