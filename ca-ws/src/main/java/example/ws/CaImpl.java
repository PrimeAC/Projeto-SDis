package example.ws;

import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.jws.WebService;

@WebService(endpointInterface = "example.ws.Ca")
public class CaImpl implements Ca {

	public String sayHello(String name) {
		return "Hello " + name + "!";
	}
	
	public Certificate getCertificates(String name) throws Exception {
		final String CERTIFICATE_BROKER_FILE = "keys/UpaBroker/UpaBroker.cer";
		final String CERTIFICATE_TRANSPORTER1_FILE = "/keys/UpaTransporter1/UpaTransporter1.cer";
		final String CERTIFICATE_TRANSPORTER2_FILE = "/keys/UpaTransporter2/UpaTransporter2.cer";
		Certificate certificate = null;
		
		if(name.equals("UpaBroker")){
			System.out.println("entrei na broker");
			certificate = readCertificateFile(CERTIFICATE_BROKER_FILE);
			return certificate;
		}
		else if(name.equals("UpaTransporter1")){
			System.out.println("entrei na t1");
			certificate = readCertificateFile(CERTIFICATE_TRANSPORTER1_FILE);
			return certificate;
		}
		else if(name.equals("UpaTransporter2")){
			System.out.println("entrei na t2");
			certificate = readCertificateFile(CERTIFICATE_TRANSPORTER2_FILE);
			return certificate;
		}
		else {
			System.out.println("entrei");
			return certificate;
		}
		
	}
	
	/**
	 * Reads a certificate from a file
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Certificate readCertificateFile(String certificateFilePath) throws Exception {
		FileInputStream fis;

		try {
			fis = new FileInputStream(certificateFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file <" + certificateFilePath + "> not fount.");
			return null;
		}
		BufferedInputStream bis = new BufferedInputStream(fis);

		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		if (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			return cert;
			// It is possible to print the content of the certificate file:
			// System.out.println(cert.toString());
		}
		bis.close();
		fis.close();
		return null;
	}

}
