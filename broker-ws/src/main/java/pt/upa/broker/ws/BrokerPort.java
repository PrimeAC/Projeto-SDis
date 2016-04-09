package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import javax.jws.WebService;

@WebService(
	    endpointInterface="pt.upa.broker.ws.BrokerPortType",
	    wsdlLocation="broker.1_0.wsdl",
	    name="BrokerWebService",
	    portName="BrokerPort",
	    targetNamespace="http://ws.broker.upa.pt/",
	    serviceName="BrokerService"
	)

public class BrokerPort implements BrokerPortType {
	
	private ArrayList<String> Norte = new ArrayList<>(Arrays.asList("Porto","Braga","Viana do Castelo","Vila Real","Bragança"));
	private ArrayList<String> Centro = new ArrayList<>(Arrays.asList("Lisboa", "Leiria","Santarem","Castelo Branco","Coimbra","Aveiro","Viseu","Guarda"));
	private ArrayList<String> Sul = new ArrayList<>(Arrays.asList("Setubal","Evora","Portalegre","Beja","Faro"));
	
	@Override
	public String ping(String name) {
		return "Pong " + name + "!";
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		
			if(Norte.contains(origin)){
				if(Norte.contains(destination) || Centro.contains(destination)){
					
				}
				else if(Sul.contains(destination)){
					UnavailableTransportFault faultInfo = new UnavailableTransportFault();
					faultInfo.setOrigin(origin);
					faultInfo.setDestination(destination);
					throw new UnavailableTransportFault_Exception("Destino inalcançável", faultInfo);
				}
				else {
					UnknownLocationFault faultInfo = new UnknownLocationFault();
					faultInfo.setLocation(destination);
					throw new UnknownLocationFault_Exception("Destino inexistente", faultInfo);
				}
			}
			
			if(Centro.contains(origin)){
				if(Norte.contains(destination) || Centro.contains(destination) || Sul.contains(destination)){
					
				}
	
				else {
					UnknownLocationFault faultInfo = new UnknownLocationFault();
					faultInfo.setLocation(destination);
					throw new UnknownLocationFault_Exception("Destino inexistente", faultInfo);
				}
			}
			
			if(Sul.contains(origin)){
				if(Sul.contains(destination) || Centro.contains(destination)){
					
				}
				else if(Norte.contains(destination)){
					UnavailableTransportFault faultInfo = new UnavailableTransportFault();
					faultInfo.setOrigin(origin);
					faultInfo.setDestination(destination);
					throw new UnavailableTransportFault_Exception("Destino inalcançável", faultInfo);
				}
				else {
					UnknownLocationFault faultInfo = new UnknownLocationFault();
					faultInfo.setLocation(destination);
					throw new UnknownLocationFault_Exception("Destino inexistente", faultInfo);
				}
				
			}
			
			else {
				if (Norte.contains(destination) || Centro.contains(destination) || Sul.contains(destination)){
					UnknownLocationFault faultInfo = new UnknownLocationFault();
					faultInfo.setLocation(destination);
					throw new UnknownLocationFault_Exception("Origem inexistente", faultInfo);
				}
				else {
					UnknownLocationFault faultInfo = new UnknownLocationFault();
					faultInfo.setLocation(destination);
					throw new UnknownLocationFault_Exception("Origem e destino inexistentes", faultInfo);
				}
					
			}
			
			
			return "ola";
			
		
		
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportView> listTransports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearTransports() {
		// TODO Auto-generated method stub	
	}
}
	