package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

public class DecideJobIT extends AbstractIT{
	
	@Test(expected = BadJobFault_Exception.class)
    public void testAbsentId() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
    	localPort1.requestJob("Lisboa", "Faro", 25);
    	localPort1.decideJob("21", true);
    		
    }
    
    @Test
    public void testAccept() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
    	localPort1.requestJob("Lisboa", "Faro", 25);
    	assertEquals(JobStateView.ACCEPTED,localPort1.decideJob("11", true).getJobState() );
    	
   		
    }
    
    @Test
    public void testRejected() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
    	localPort1.requestJob("Lisboa", "Faro", 25);
    	assertEquals(JobStateView.REJECTED,localPort1.decideJob("11", false).getJobState() );
    	
    }
    
    @Test
    public void testTimer() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception, InterruptedException {
    	int cont=0;
    	localPort1.requestJob("Lisboa", "Faro", 25);
    	localPort1.decideJob("11", true);
    	
    	while((!localPort1.jobStatus("11").getJobState().equals(JobStateView.COMPLETED)) ||(System.currentTimeMillis())<16000){
    		
    		if((localPort1.jobStatus("11").getJobState().equals(JobStateView.ACCEPTED)) && cont==0){
    			cont++;
    		}
    		else if((localPort1.jobStatus("11").getJobState().equals(JobStateView.HEADING)) && cont==1){
    			cont++;
    		}
    		else if((localPort1.jobStatus("11").getJobState().equals(JobStateView.ONGOING)) && cont==2){
    			cont++;
    		}
    		
    	}
    	assertEquals(JobStateView.COMPLETED, localPort1.jobStatus("11").getJobState());
    }

}
