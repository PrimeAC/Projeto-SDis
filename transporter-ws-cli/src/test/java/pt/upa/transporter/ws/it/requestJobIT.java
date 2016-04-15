package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;

public class requestJobIT extends AbstractIT{
	
	@Test
    public void testPriceBellow10() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((1 < (localPort1.requestJob("Lisboa", "Faro", 9).getJobPrice())) && ((localPort1.requestJob("Lisboa", "Faro", 9).getJobPrice())  <= 10));
    	assertTrue((1 < (localPort2.requestJob("Lisboa", "Porto", 9).getJobPrice())) && ((localPort2.requestJob("Lisboa", "Porto", 9).getJobPrice())  <= 10));
    }
    
    @Test
    public void testPriceOver100() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertNull((localPort1.requestJob("Lisboa", "Faro", 101)));
    	assertNull((localPort2.requestJob("Lisboa", "Porto", 101)));
    }
    
    @Test
    public void testPrice0Or1() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((0 == (localPort1.requestJob("Lisboa", "Faro", 1).getJobPrice())) && ((localPort1.requestJob("Lisboa", "Faro", 0).getJobPrice())  == 0));
    	assertTrue((0 == (localPort2.requestJob("Lisboa", "Porto", 1).getJobPrice())) && ((localPort2.requestJob("Lisboa", "Porto", 0).getJobPrice())  == 0));
    }
    
    @Test(expected = BadPriceFault_Exception.class)
    public void testBadPriceFault() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort1.requestJob("Lisboa", "Leiria", -1);
   	
    }
    
    @Test(expected = BadLocationFault_Exception.class)
    public void testEmptyOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort1.requestJob("", "Lisboa", 4);
    	  		
    }
    
    @Test(expected = BadLocationFault_Exception.class)
    public void testEmptyDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort1.requestJob("Lisboa", "", 4);
    	  		
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testNullOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort1.requestJob(null, "Leiria", 4);
    	  		
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testNullDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort1.requestJob("Lisboa", null, 4);
    	  		
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testBadOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort1.requestJob("Tagus", "Faro", 4);
    	  		
    }
    
    @Test(expected = BadLocationFault_Exception.class)
    public void testBadDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	localPort1.requestJob("Faro", "Park", 4);
    
    }
    
    @Test
    public void testNorthNotImpar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertNull((localPort1.requestJob("Porto", "Santarem", 10)));
    
    }
    
    @Test
    public void testSouthhNotPar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertNull((localPort2.requestJob("Lisboa", "Faro", 10)));
    	
   		
    }
    
    @Test
    public void testImparImpar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((1 <= (localPort1.requestJob("Lisboa", "Faro", 21).getJobPrice())) && ((localPort1.requestJob("Lisboa", "Faro", 21).getJobPrice()) <=20));
    	
   	
    }
    
    @Test
    public void testParPar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((1 <= (localPort2.requestJob("Lisboa", "Porto", 20).getJobPrice())) && ((localPort2.requestJob("Lisboa", "Porto", 20).getJobPrice()) <=19));
    	
    }
    
    @Test
    public void testImparPar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((101 >= (localPort1.requestJob("Lisboa", "Faro", 20).getJobPrice())) && ((localPort1.requestJob("Lisboa", "Faro", 20).getJobPrice()) >=21));
    	
   	
    }
    
    @Test
    public void testParImpar() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	assertTrue((101 >= (localPort2.requestJob("Lisboa", "Porto", 21).getJobPrice())) && ((localPort2.requestJob("Lisboa", "Porto", 21).getJobPrice()) >=22));
    	
   		
    }

}
