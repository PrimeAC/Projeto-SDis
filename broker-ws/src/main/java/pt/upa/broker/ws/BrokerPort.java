package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.broker.BrokerApplication;
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
			
		int update=price; //meu inicialização do preço máximo definido pelo cliente
		int aux=100;	//meu inicialização do preço máximo possivel
		String id="";		//meu inicialização do id inexistente
			try{
				for(TransporterClient i : BrokerApplication.getTransportersList()){
					TransportView trans = new TransportView();
					trans.setOrigin(origin);
					trans.setDestination(destination);
					trans.setPrice(price);
					trans.setState(TransportStateView.REQUESTED);
					
					Transportes.add(trans);
					
					JobView job = i.requestJob(origin, destination, price);
					
					if( job == null) {
						UnavailableTransportFault faultInfo = new UnavailableTransportFault();
						faultInfo.setOrigin(origin);
						faultInfo.setDestination(destination);
						throw new UnavailableTransportFault_Exception("Transportes fora de serviço",faultInfo);
					}
					trans.setTransporterCompany(job.getCompanyName());
					trans.setPrice(job.getJobPrice());
					if ((job.getJobPrice()) < update){ //meu vê se o preço é menor que o atual
						update= job.getJobPrice(); //meu atualizar o valor minimo encontrado
						id=job.getJobIdentifier(); //meu conseguir o id da transportadora menor , n sei se está certo
					}
					else {  //serve para ver o BestPriceFound para a excepção
						if((job.getJobPrice()) < aux){ //vê se o preço é menor que o atual
							aux=job.getJobPrice();    //atualiza o melhor preço que se arranjou (excepção)
						}
					}
					trans.setState(TransportStateView.BUDGETED); //n sei se é o posicionamento correto
					/*atualizar na lista*/
				}
				if (update==price){
					trans.setState(TransportStateView.FAILED); //mal posicionado
					UnavailableTransportPriceFault faultInfo = new UnavailableTransportPriceFault();
					faultInfo.setBestPriceFound(aux);
					throw new UnavailableTransportPriceFault_Exception("Orçamento demasiado baixo",faultInfo);
				}
				trans.setState(TransportStateView.BOOKED); //mal posicionado
				return id;  //retorna o id da transportadora melhor , talvez esteja errado
				
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
		
		for( TransportView i : Transportes) {
			if(i.getId().equals(id)) {
				return i;
			}
		}
		UnknownTransportFault faultInfo = new UnknownTransportFault();
		faultInfo.setId(id);
		throw new UnknownTransportFault_Exception("Id não encontrado!", faultInfo);
	}

	@Override
	public List<TransportView> listTransports() {
	
		return Transportes;
	}

	@Override
	public void clearTransports() {
		Transportes.clear();	
	}
}
	