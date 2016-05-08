package pt.upa.broker.ws.it;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportStateView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class requestTransportIT extends AbstractIT {
	
	@Test(expected=UnavailableTransportFault_Exception.class)
    public void testOutOfServiceTransport() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	port.requestTransport("Lisboa", "Faro", 125);
 
    }
	
	@Test(expected=UnavailableTransportPriceFault_Exception.class)
    public void testBadBudgetTransport() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, 
    	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception  {
    	port.requestTransport("Porto", "Lisboa", 15);
 
    }
	
	@Test
    public void testDefaultTransport() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, 
    	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
    	String id = port.requestTransport("Faro", "Lisboa", 7);
    	
    	assertEquals(TransportStateView.BOOKED, port.viewTransport(id).getState());
    }
	
}
