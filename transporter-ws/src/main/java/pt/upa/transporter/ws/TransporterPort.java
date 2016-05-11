package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import example.ws.handler.SignatureHandler;

import javax.annotation.Resource;
import javax.jws.HandlerChain;

@WebService(
	    endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	    wsdlLocation="transporter.1_0.wsdl",
	    name="TransporterWebService",
	    portName="TransporterPort",
	    targetNamespace="http://ws.transporter.upa.pt/",
	    serviceName="TransporterService"
	)

@HandlerChain(file="/handler-chain.xml")

public class TransporterPort implements TransporterPortType{
	
	public String Id;
	
	public Map<String, String> Locais = new HashMap<>();
	
	public int cnt=0;
	
	private List<JobView> Trabalhos = new ArrayList<>();
	
	@Resource
	private WebServiceContext webServiceContext;
	
	public static final String TRANSPORTER_ENTITY = "UpaTransporter";
	
	public TransporterPort(String identifier) {
		Id=String.valueOf(identifier.charAt(14));
		
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
		
	}
	
	public String getId() {
		return Id;
	}
	
	public void setSender(){
		MessageContext messageContext = webServiceContext.getMessageContext();
		String newValue = TRANSPORTER_ENTITY+getId();
		messageContext.put(SignatureHandler.CONTEXT_PROPERTY, newValue);
	}
	
	@Override
	public String ping(String name) {
		setSender();
		return "Pong " + name + "!";
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		setSender();
		
		int offer=0;
		if(Locais.containsKey(origin) && Locais.containsKey(destination)) {
			if((Locais.get(origin).equals("Norte") || Locais.get(origin).equals("Centro") 
					|| Locais.get(origin).equals("Sul"))){
				
				if((Locais.get(destination).equals("Norte") || Locais.get(destination).equals("Centro") 
						|| Locais.get(destination).equals("Sul"))){
					
					/*Origem e destino válidos*/
			
			
					if(price<0){
						BadPriceFault faultInfo = new BadPriceFault();
						faultInfo.setPrice(price);
						throw new BadPriceFault_Exception("Preço inválido",faultInfo);
					}
					
					else if(price==0 || price ==1) {
						offer=0;
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
						offer=ThreadLocalRandom.current().nextInt(2,10);
					}
					
					else{
						if ((price%2!=0 && Integer.parseInt(getId())%2!=0) || (price%2==0 && Integer.parseInt(getId())%2==0)) {
							/*nome impar preço impar ou nome par preço par*/
			
							offer=ThreadLocalRandom.current().nextInt(1,price);
						}
						else {
							/*nome par preço impar ou nome impar preço par*/
			
							offer = ThreadLocalRandom.current().nextInt(price+1,102);
						}
					}
				}
			}
		}
		else {
			BadLocationFault faultInfo = new BadLocationFault();
			faultInfo.setLocation(origin);
			throw new BadLocationFault_Exception("Origem/destino inexistente", faultInfo);
		}
		
		JobView job = new JobView();
		job.setCompanyName("UpaTransporter"+getId());
		job.setJobOrigin(origin);
		job.setJobDestination(destination);
		job.setJobIdentifier(getId()+Integer.toString(++cnt));  
		job.setJobState(JobStateView.PROPOSED);
		job.setJobPrice(offer);
		Trabalhos.add(job);

		return job;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		
		setSender();
		
		for( JobView i : Trabalhos) {
			if(i.getJobIdentifier().equals(id) && i.getJobState()==JobStateView.PROPOSED) {
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
		
		setSender();
		
		for( JobView i : Trabalhos) {
			if(i.getJobIdentifier().equals(id)) {
				return i;
			}
		}
		return null;
	}

	@Override
	public List<JobView> listJobs() {
		
		setSender();
		
		return Trabalhos;
	}

	@Override
	public void clearJobs() {
		
		setSender();
		
		Trabalhos.clear();
		cnt=0;
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
