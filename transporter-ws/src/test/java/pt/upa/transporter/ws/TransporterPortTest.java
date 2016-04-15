package pt.upa.transporter.ws;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.upa.transporter.TransporterApplication;
import pt.upa.transporter.ws.TransporterPort.Repeater.StatusChanger;

/**
 *  Unit Test example
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */
public class TransporterPortTest {

    // static members
	private TransporterPort localPort;
	private TransporterPort localPort1;

    // one-time initialization and clean-up

    @BeforeClass
    public static void oneTimeSetUp() {
    	
    }

    @AfterClass
    public static void oneTimeTearDown() {
    	
    }


    // members


    // initialization and clean-up for each test

    @Before
    public void setUp() {
    	localPort = new TransporterPort("UpaTransporter1");
    	localPort1 = new TransporterPort("UpaTransporter2");
    }

    @After
    public void tearDown() {
    	localPort = null;
    	localPort1 = null;
    }


    // tests

    @Test
    public void testDefaultPing() {
    	assertEquals("Pong test!", localPort.ping("test"));
       
    }
    
    @Test
    public void testDefaultGetId() {
    	assertEquals("1", localPort.getId());
       
    }
    
    
    @Test
    public void testPriceBellow10() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = localPort.requestJob("Lisboa", "Faro", 9).getJobPrice();
    	
    	assertTrue(1<price);
    	assertTrue(price<=10);
    	
    }
    
    @Test
    public void testPriceOver100() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertNull((localPort.requestJob("Lisboa", "Faro", 101)));
    	
   		
    }
    
    @Test
    public void testPrice0Or1() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((0 == (localPort.requestJob("Lisboa", "Faro", 1).getJobPrice())) && ((localPort.requestJob("Lisboa", "Faro", 0).getJobPrice())  == 0));
    	
    }
    @Test(expected = BadPriceFault_Exception.class)
    public void testBadPriceFault() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort.requestJob("Lisboa", "Faro", -1);
    	
   	
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testEmptyOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort.requestJob("", "Faro", 4);
    	  		
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testEmptyDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort.requestJob("Faro", "", 4);
    	  		
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testNullOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort.requestJob(null, "Faro", 4);
    	  		
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testNullDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort.requestJob("Faro", null, 4);
    	  		
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testBadOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort.requestJob("Tagus", "Faro", 4);
    	  		
    }
    
    @Test(expected = BadLocationFault_Exception.class)
    public void testBadDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort.requestJob("Faro", "Park", 4);
    
    }
    
    @Test
    public void testNorthNotImpar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertNull((localPort.requestJob("Porto", "Santarem", 10)));
    
    }
    
    @Test
    public void testSouthhNotPar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertNull((localPort1.requestJob("Lisboa", "Faro", 10)));
    	
   		
    }
    
    @Test
    public void testImparImpar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((1 <= (localPort.requestJob("Lisboa", "Faro", 21).getJobPrice())) && ((localPort.requestJob("Lisboa", "Faro", 21).getJobPrice()) <=20));
    	
   	
    }
    
    @Test
    public void testParPar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((1 <= (localPort1.requestJob("Lisboa", "Porto", 20).getJobPrice())) && ((localPort1.requestJob("Lisboa", "Porto", 20).getJobPrice()) <=19));
    	
    }
    
    @Test
    public void testImparPar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((101 >= (localPort.requestJob("Lisboa", "Faro", 20).getJobPrice())) && ((localPort.requestJob("Lisboa", "Faro", 20).getJobPrice()) >=21));
    	
   	
    }
    
    @Test
    public void testParImpar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((101 >= (localPort1.requestJob("Lisboa", "Porto", 21).getJobPrice())) && ((localPort1.requestJob("Lisboa", "Porto", 21).getJobPrice()) >=22));
    	
   		
    }
    
    @Test(expected = BadJobFault_Exception.class)
    public void testAbsentId() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
    	localPort.requestJob("Lisboa", "Faro", 25);
    	localPort.decideJob("21", true);
    	
   		
    }
  
    @Test
    public void testAccept() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
    	localPort.requestJob("Lisboa", "Faro", 25);
    	assertEquals(JobStateView.ACCEPTED,localPort.decideJob("11", true).getJobState() );
    	
   		
    }
    
    @Test
    public void testRejected() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
    	localPort.requestJob("Lisboa", "Faro", 25);
    	assertEquals(JobStateView.REJECTED,localPort.decideJob("11", false).getJobState() );
    	
    }
    
    @Test
    public void testDefaultJobStatus() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	final JobView job = localPort.requestJob("Lisboa", "Faro", 25);
    	assertEquals(job,localPort.jobStatus("11"));
 
    }
    
    @Test
    public void testBadIdJobStatus() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	localPort.requestJob("Lisboa", "Faro", 25);
    	assertNull(localPort.jobStatus("9"));
 
    }
    
    @Test
    public void testListJobs() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	final List<JobView> job = new ArrayList<>();
    	final JobView x = localPort.requestJob("Lisboa", "Faro", 25);
    	job.add(x);
    	assertEquals(job, localPort.listJobs());
   		
    }
    
    @Test
    public void testClearList() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	final List<JobView> job = new ArrayList<>();
    	localPort.requestJob("Lisboa", "Faro", 25);
    	localPort.clearJobs();
    	assertEquals(job, localPort.listJobs());
   		
    }
    @Test
    public void testTimer() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception, InterruptedException {
    	int cont=0;
    	localPort.requestJob("Lisboa", "Faro", 25);
    	localPort.decideJob("11", true);
    	
    	while((!localPort.jobStatus("11").getJobState().equals(JobStateView.COMPLETED)) ||(System.currentTimeMillis())<16000){
    		
    		if((localPort.jobStatus("11").getJobState().equals(JobStateView.ACCEPTED)) && cont==0){
    			cont++;
    		}
    		else if((localPort.jobStatus("11").getJobState().equals(JobStateView.HEADING)) && cont==1){
    			cont++;
    		}
    		else if((localPort.jobStatus("11").getJobState().equals(JobStateView.ONGOING)) && cont==2){
    			cont++;
    		}
    	}
    	assertEquals(JobStateView.COMPLETED, localPort.jobStatus("11").getJobState());
    }
    
}