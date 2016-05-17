package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.jws.WebService;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.broker.BrokerApplication;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;

@WebService(
	    endpointInterface="pt.upa.broker.ws.BrokerPortType",
	    wsdlLocation="broker.1_0.wsdl",
	    name="BrokerWebService",
	    portName="BrokerPort",
	    targetNamespace="http://ws.broker.upa.pt/",
	    serviceName="BrokerService"
	)


public class BrokerPort implements BrokerPortType {
	
	private List<TransportView> Transportes = new ArrayList<>();
	
	private Map<String, String> Identificadores = new HashMap<>();
	
	static private int generator = 0;
	
	public String Id;
	
	public BrokerPort(String id) {
		Id=id; 
	}
	
	public Map<String,String> getMap(){
		return Identificadores;
	}
	
	@Override
	public String ping(String name) {
		return "Pong " + name + "!";
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
			
			try{
				TransportView trans = new TransportView();	//cria-se um transporte, acho que se devia criar lá fora
				trans.setOrigin(origin); //acho que é fora do try
				trans.setDestination(destination); //acho que é fora do try
				trans.setPrice(price); //acho que é fora do try
				trans.setState(TransportStateView.REQUESTED); //acho que é fora do try
				trans.setId(String.valueOf(generator++));
				
				JobView finalJob = null;
				TransporterClient finalCompany = null;
				
				for(TransporterClient i : BrokerApplication.getTransportersList()){ //percorro as transportadoras
					
					JobView job = i.requestJob(origin, destination, price); //cada transportadora oferece um preço/trabalho

					if( job == null) {
						continue;
					}
					else {
						if(finalJob == null) {
							trans.setState(TransportStateView.BUDGETED);
							finalJob=job;
							finalCompany=i;
						}
						else {
							if(job.getJobPrice()<finalJob.getJobPrice()) {
								finalCompany.decideJob(finalJob.getJobIdentifier(), false);
								finalJob=job;
								finalCompany=i;
							}
						}
					}
				}
				
				if(finalJob == null){
					UnavailableTransportFault faultInfo = new UnavailableTransportFault();
					faultInfo.setOrigin(origin);
					faultInfo.setDestination(destination);
					throw new UnavailableTransportFault_Exception("Transportes fora de serviço",faultInfo);
				}
				
				trans.setPrice(finalJob.getJobPrice());
				trans.setTransporterCompany(finalJob.getCompanyName());
				Identificadores.put(trans.getId(), finalJob.getJobIdentifier());
				Transportes.add(trans);
				
				updateBackup(trans,finalJob.getJobIdentifier());
				
				
				if(trans.getPrice()>price){//verifica se a melhor oferta foi ACIMA do preço pedido
					finalCompany.decideJob(finalJob.getJobIdentifier(), false);
					trans.setState(TransportStateView.FAILED);
			
					UnavailableTransportPriceFault faultInfo = new UnavailableTransportPriceFault();
					faultInfo.setBestPriceFound(finalJob.getJobPrice());
					throw new UnavailableTransportPriceFault_Exception("Orçamento demasiado baixo",faultInfo);
				}
				
				else {
					finalCompany.decideJob(finalJob.getJobIdentifier(), true);
					trans.setState(TransportStateView.BOOKED);
				}
				
				
				
				return trans.getId();
				
			}catch(BadJobFault_Exception e){
				UnavailableTransportFault faultInfo = new UnavailableTransportFault();
				faultInfo.setOrigin(origin);
				faultInfo.setDestination(destination);
				throw new UnavailableTransportFault_Exception("Id de transporte inválido",faultInfo);	
				
			}catch (BadPriceFault_Exception e) {
				InvalidPriceFault faultInfo = new InvalidPriceFault();
				faultInfo.setPrice(price);
				throw new InvalidPriceFault_Exception("Preço inválido",faultInfo);
			}
			catch (BadLocationFault_Exception e) {
				UnknownLocationFault faultInfo = new UnknownLocationFault();
				faultInfo.setLocation(origin);
				throw new UnknownLocationFault_Exception("Origem/Destino inexistente", faultInfo);
			}
	
	}
		
	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		
		TransportView transport = null;
		String jobId = null;
		
		jobId = Identificadores.get(id);
		
		for( TransportView i : Transportes) {
			if(i.getId().equals(id)) {
				transport=i;
				break;
			}
		}
		
		if(transport == null) {
			UnknownTransportFault faultInfo = new UnknownTransportFault();
			faultInfo.setId(id);
			throw new UnknownTransportFault_Exception("Id não encontrado!", faultInfo);
		}
		for(TransporterClient i : BrokerApplication.getTransportersList()){
			if(i.getCompanyName().equals(transport.getTransporterCompany())){

				if(i.jobStatus(jobId).getJobState().equals(JobStateView.HEADING)){
					transport.setState(TransportStateView.HEADING);
				}
				else if(i.jobStatus(jobId).getJobState().equals(JobStateView.ONGOING)){
					transport.setState(TransportStateView.ONGOING);
				}
				else if(i.jobStatus(jobId).getJobState().equals(JobStateView.COMPLETED)){
					transport.setState(TransportStateView.COMPLETED);
				}
			}
		}
		updateBackup(transport,jobId);
		return transport;
	}

	@Override
	public List<TransportView> listTransports() {
		
		List<TransportView> result = new ArrayList<>();
		
		try{
			for( TransportView i : Transportes) {
				result.add(viewTransport(i.getId()));
			}
			
		}catch(UnknownTransportFault_Exception e){
			e.printStackTrace();
		}
		
		Transportes = result;
		
		return Transportes;
	}

	@Override
	public void clearTransports() {
		for(TransporterClient i : BrokerApplication.getTransportersList()) {
			i.clearJobs();
		}
		Transportes.clear();
		if(Id.equals("UpaBroker1")){
			BrokerApplication.getBrokerBackup().clearTransports();
		}
	}
	
	@Override
	public void updateBackup(TransportView arg1, String arg2) {
		if(Id.equals("UpaBroker1")){
			BrokerApplication.getBrokerBackup().receiveUpdate(arg1,arg2, generator);
		}
	}
	
	@Override
	public void receiveUpdate(TransportView arg1,String arg2, int arg3){
		System.out.println("RECEIVE UPDATE");
		Transportes.add(arg1);
		Identificadores.put(arg1.getId(), arg2);
		generator = arg3;
	}
	
	@Override
	public void imAlive() {
		BrokerApplication.alive = true;
		BrokerApplication.flag = 1;
	}

}
	