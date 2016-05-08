package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class pingIT extends AbstractIT {
	@Test
    public void testDefaultPing() {
    	assertEquals("Pong test1!", port.ping("test1"));
    }
}
