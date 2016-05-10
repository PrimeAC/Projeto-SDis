package example.ws;

import java.security.cert.Certificate;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface Ca {

	@WebMethod
	String sayHello(String name);
	
	@WebMethod
	Certificate getCertificates(String name) throws Exception;

}
