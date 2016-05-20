package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;


public class listTransportsIT extends AbstractIT {
	
	@Test
    public void testlistTransports() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, 
    	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception{
    	final List<TransportView> trans = new ArrayList<>();
    	System.out.println(port);
    	final String id = port.requestTransport("Lisboa", "Faro", 25);
    	trans.add(port.viewTransport(id));
    	assertEquals(trans.get(0).getTransporterCompany(), port.listTransports().get(0).getTransporterCompany());
    	assertEquals(trans.get(0).getDestination() , port.listTransports().get(0).getDestination());
    	assertEquals(trans.get(0).getId(), port.listTransports().get(0).getId());
    	assertEquals(trans.get(0).getOrigin(), port.listTransports().get(0).getOrigin());
    	assertEquals(trans.get(0).getPrice(), port.listTransports().get(0).getPrice());
    	assertEquals(trans.get(0).getState(), port.listTransports().get(0).getState());
    }
}
