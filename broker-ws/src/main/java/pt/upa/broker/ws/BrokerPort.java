package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	/*
	static private List<TransporterClient> transporters = new ArrayList<>();
	
	public BrokerPort(String uddi){
		
		UDDINaming uddiNaming = new UDDINaming(uddi);
		
		// connecting with transporter
		System.out.printf("Looking for '%s'%n", "UpaTransporters");
		Collection<String> endpoints = uddiNaming.list("UpaTransporter%");
		
		System.out.println("Creating stub(s) ...");
		for(String i : endpoints) {
			TransporterClient tc = new TransporterClient(i);
			transporters.add(tc);
		}
	}*/
	
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
		TransporterClient transporter = null;
		JobView result = null;
		String jobId = null;
		
		for( TransportView i : Transportes) {
			if(i.getId().equals(id)) {
				if(i.getState().equals(TransportStateView.REQUESTED) || i.getState().equals(TransportStateView.BUDGETED)
					|| i.getState().equals(TransportStateView.FAILED) || i.getState().equals(TransportStateView.BOOKED)){
					
					continue;
				}
				else{
					
				}
			}
		}
		
		for(TransporterClient i : BrokerApplication.getTransportersList()){
			
			for(JobView j : i.listJobs()){
				
				if(j.getJobIdentifier().equals(Identificadores.get(id))){
					
					result = i.jobStatus(j.getJobIdentifier());
					
					if(result.getJobState().equals(JobStateView.PROPOSED)){
						transport.setState(TransportStateView.BUDGETED);
					}
					else if(result.getJobState().equals(JobStateView.ACCEPTED)){
						transport.setState(TransportStateView.BOOKED);
					}
					else if(result.getJobState().equals(JobStateView.REJECTED)){
						transport.setState(TransportStateView.FAILED);
					}
					else if(result.getJobState().equals(JobStateView.HEADING)){
						transport.setState(TransportStateView.HEADING);
					}
					else if(result.getJobState().equals(JobStateView.ONGOING)){
						transport.setState(TransportStateView.ONGOING);
					}
					else{
						transport.setState(TransportStateView.COMPLETED);
					}
				}
			}
		}
		
		if(transport == null) {
			UnknownTransportFault faultInfo = new UnknownTransportFault();
			faultInfo.setId(id);
			throw new UnknownTransportFault_Exception("Id não encontrado!", faultInfo);
		}*/
		TransportView ola = new TransportView();
		return ola;
	}

	@Override
	public List<TransportView> listTransports() {
		/*
		for( TransportView i : Transportes) {
			viewTransport(i.getId());
		}
		*/
		return Transportes;
	}

	@Override
	public void clearTransports() {
		Transportes.clear();	
	}
}
	