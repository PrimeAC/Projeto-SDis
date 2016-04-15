package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class pingIT extends AbstractIT{
	
	@Test
    public void testDefaultPing() {
    	assertEquals("Pong test1!", localPort1.ping("test1"));
    	assertEquals("Pong test2!", localPort2.ping("test2"));
    }
}
