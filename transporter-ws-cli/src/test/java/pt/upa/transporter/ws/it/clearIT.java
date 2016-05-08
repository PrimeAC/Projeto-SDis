package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;

public class clearIT extends AbstractIT{
	
	@Test
    public void testClearList() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	final List<JobView> jobs = new ArrayList<>();
    	localPort1.requestJob("Lisboa", "Faro", 25);
    	localPort1.clearJobs();
    	assertEquals(jobs, localPort1.listJobs());
   		
    }

}
