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

public class clearTransportsIT extends AbstractIT {
	
	@Test
    public void testClearList() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, 
    	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception{
    	final List<TransportView> trans = new ArrayList<>();
    	port.requestTransport("Lisboa", "Faro", 25);
    	port.clearTransports();
    	assertEquals(trans, port.listTransports());
    }

}
