package pt.upa.transporter.ws;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.upa.transporter.TransporterApplication;

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
        // assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testDefaultGetId() {
    	assertEquals("1", localPort.getId());
        // assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    
    @Test
    public void testPriceBellow10() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((1 < (localPort.requestJob("Lisboa", "Faro", 9).getJobPrice())) && ((localPort.requestJob("Lisboa", "Faro", 9).getJobPrice())  <= 10));
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testPriceOver100() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertNull((localPort.requestJob("Lisboa", "Faro", 101)));
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testPrice0Or1() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((0 == (localPort.requestJob("Lisboa", "Faro", 1).getJobPrice())) && ((localPort.requestJob("Lisboa", "Faro", 0).getJobPrice())  == 0));
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    @Test(expected = BadPriceFault_Exception.class)
    public void testBadPriceFault() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort.requestJob("Lisboa", "Faro", -1);
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testBadOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort.requestJob("Tagus", "Faro", 4);
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test(expected = BadLocationFault_Exception.class)
    public void testBadDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort.requestJob("Faro", "Park", 4);
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testNorthNotImpar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertNull((localPort.requestJob("Porto", "Santarem", 10)));
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testSouthhNotPar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertNull((localPort1.requestJob("Lisboa", "Faro", 10)));
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testImparImpar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((1 <= (localPort.requestJob("Lisboa", "Faro", 21).getJobPrice())) && ((localPort.requestJob("Lisboa", "Faro", 21).getJobPrice()) <=20));
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testParPar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((1 <= (localPort1.requestJob("Lisboa", "Porto", 20).getJobPrice())) && ((localPort1.requestJob("Lisboa", "Porto", 20).getJobPrice()) <=19));
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testImparPar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((101 >= (localPort.requestJob("Lisboa", "Faro", 20).getJobPrice())) && ((localPort.requestJob("Lisboa", "Faro", 20).getJobPrice()) >=21));
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testParImpar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((101 >= (localPort1.requestJob("Lisboa", "Porto", 21).getJobPrice())) && ((localPort1.requestJob("Lisboa", "Porto", 21).getJobPrice()) >=22));
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test(expected = BadJobFault_Exception.class)
    public void testAbsentId() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
    	localPort.requestJob("Lisboa", "Faro", 25);
    	localPort.decideJob("21", true);
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testDefaultId() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
    	localPort.requestJob("Lisboa", "Faro", 25);
    	localPort.decideJob("11", true);
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testAccept() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
    	localPort.requestJob("Lisboa", "Faro", 25);
    	assertEquals(JobStateView.ACCEPTED,localPort.decideJob("11", true).getJobState() );
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testRejected() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
    	localPort.requestJob("Lisboa", "Faro", 25);
    	assertEquals(JobStateView.REJECTED,localPort.decideJob("11", false).getJobState() );
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testDefaultJobStatus() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	final JobView job = localPort.requestJob("Lisboa", "Faro", 25);
    	assertEquals(job,localPort.jobStatus("11"));
    	
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    @Test
    public void testListJobbs() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	final List<JobView> job = new ArrayList<>();
    	final JobView x = localPort.requestJob("Lisboa", "Faro", 25);
    	job.add(x);
    	assertEquals(job, localPort.listJobs());
   		// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    
    
}