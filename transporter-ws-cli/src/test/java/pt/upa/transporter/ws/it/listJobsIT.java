package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;

public class listJobsIT extends AbstractIT{
	
	@Test
    public void testListJobs() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	final List<JobView> jobs = new ArrayList<>();
    	final JobView job = localPort1.requestJob("Lisboa", "Faro", 25);
    	jobs.add(job);
    	assertEquals(jobs.get(0).getCompanyName(), localPort1.listJobs().get(0).getCompanyName());
    	assertEquals(jobs.get(0).getJobDestination() , localPort1.listJobs().get(0).getJobDestination());
    	assertEquals(jobs.get(0).getJobIdentifier(), localPort1.listJobs().get(0).getJobIdentifier());
    	assertEquals(jobs.get(0).getJobOrigin(), localPort1.listJobs().get(0).getJobOrigin());
    	assertEquals(jobs.get(0).getJobPrice(), localPort1.listJobs().get(0).getJobPrice());
    	assertEquals(jobs.get(0).getJobState(), localPort1.listJobs().get(0).getJobState());
    }
}
