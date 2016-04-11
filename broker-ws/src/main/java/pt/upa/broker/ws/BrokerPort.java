package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jws.WebService;
import pt.upa.transporter.ws.BadPriceFault_Exception;
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
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
			
		if(Norte.contains(origin)){
			if(Norte.contains(destination) || Centro.contains(destination)){
				
			}
			else if(Sul.contains(destination)){
				UnavailableTransportFault faultInfo = new UnavailableTransportFault();
				faultInfo.setOrigin(origin);
				faultInfo.setDestination(destination);
				throw new UnavailableTransportFault_Exception("Destino inalcançável", faultInfo);
			}
		}
		
		else if(Centro.contains(origin)){
			if(Norte.contains(destination) || Centro.contains(destination) || Sul.contains(destination)){
				
			}
		}
		
		else if(Sul.contains(origin)){
			if(Sul.contains(destination) || Centro.contains(destination)){
				/*SC SS*/
			}
			else if(Norte.contains(destination)){
				UnavailableTransportFault faultInfo = new UnavailableTransportFault();
				faultInfo.setOrigin(origin);
				faultInfo.setDestination(destination);
				throw new UnavailableTransportFault_Exception("Destino inalcançável", faultInfo);
			}
			
		}
		/*
		try{
			
			
			
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
		*/
		return "ola";
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
	