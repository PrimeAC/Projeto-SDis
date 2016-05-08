package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;


public class viewTransportIT extends AbstractIT {
	/*
	@Test
    public void testDefaultViewTransport() {
		final String id = port.requestTransport("Lisboa", "Faro", 25);
		assertEquals(job.getCompanyName(), localPort1.listJobs().get(0).getCompanyName());
    	assertEquals(job.getJobDestination() , localPort1.listJobs().get(0).getJobDestination());
    	assertEquals(job.getJobIdentifier(), localPort1.listJobs().get(0).getJobIdentifier());
    	assertEquals(job.getJobOrigin(), localPort1.listJobs().get(0).getJobOrigin());
    	assertEquals(job.getJobPrice(), localPort1.listJobs().get(0).getJobPrice());
    	assertEquals(job.getJobState(), localPort1.listJobs().get(0).getJobState());
 
    }
    */
    @Test(expected=UnknownTransportFault_Exception.class)
    public void testBadIdViewTransport() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, 
    	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
    	port.requestTransport("Lisboa", "Faro", 25);
    	port.viewTransport("-1");
 
    }
}
