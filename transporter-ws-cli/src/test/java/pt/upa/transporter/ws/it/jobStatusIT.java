package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;

public class jobStatusIT extends AbstractIT {
	
	@Test
    public void testDefaultJobStatus() throws BadLocationFault_Exception, BadPriceFault_Exception {
		final JobView job = localPort1.requestJob("Lisboa", "Faro", 25);
		assertEquals(job.getCompanyName(), localPort1.listJobs().get(0).getCompanyName());
    	assertEquals(job.getJobDestination() , localPort1.listJobs().get(0).getJobDestination());
    	assertEquals(job.getJobIdentifier(), localPort1.listJobs().get(0).getJobIdentifier());
    	assertEquals(job.getJobOrigin(), localPort1.listJobs().get(0).getJobOrigin());
    	assertEquals(job.getJobPrice(), localPort1.listJobs().get(0).getJobPrice());
    	assertEquals(job.getJobState(), localPort1.listJobs().get(0).getJobState());
 
    }
    
    @Test
    public void testBadIdJobStatus() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	localPort1.requestJob("Lisboa", "Faro", 25);
    	assertNull(localPort1.jobStatus("9"));
 
    }
}
